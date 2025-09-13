package com.basic.template.backend_template.domain.inventory.service;

import com.basic.template.backend_template.domain.inventory.dto.InventoryItemReqDto;
import com.basic.template.backend_template.domain.inventory.dto.InventoryItemResDto;
import com.basic.template.backend_template.domain.inventory.dto.InventoryItemSearchReqDto;
import com.basic.template.backend_template.domain.inventory.entity.InventoryItem;
import com.basic.template.backend_template.domain.inventory.entity.ItemCategory;
import com.basic.template.backend_template.domain.inventory.entity.ItemStatus;
import com.basic.template.backend_template.domain.inventory.repository.InventoryItemRepository;
import com.basic.template.backend_template.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventoryItemService 테스트")
class InventoryItemServiceImplTest {

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InventoryItemServiceImpl inventoryItemService;

    private InventoryItem testItem;
    private InventoryItemReqDto itemRequest;
    private InventoryItemSearchReqDto searchRequest;
    private Long userId;
    private Long itemId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        itemId = 1L;

        testItem = InventoryItem.builder()
                .userId(userId)
                .name("테스트 아이템")
                .description("테스트 설명")
                .category(ItemCategory.ELECTRONICS)
                .quantity(1)
                .status(ItemStatus.EXCELLENT)
                .purchasePrice(BigDecimal.valueOf(100000))
                .currentValue(BigDecimal.valueOf(80000))
                .location("서울")
                .imageUrl("http://example.com/image.jpg")
                .build();
        // 테스트를 위한 ID 설정은 리플렉션으로 처리하거나 mock에서 반환할 때 처리

        itemRequest = new InventoryItemReqDto();
        itemRequest.setName("테스트 아이템");
        itemRequest.setDescription("테스트 설명");
        itemRequest.setCategory(ItemCategory.ELECTRONICS);
        itemRequest.setQuantity(1);
        itemRequest.setStatus(ItemStatus.EXCELLENT);
        itemRequest.setPurchasePrice(BigDecimal.valueOf(100000));
        itemRequest.setCurrentValue(BigDecimal.valueOf(80000));
        itemRequest.setLocation("서울");
        itemRequest.setImageUrl("http://example.com/image.jpg");

        searchRequest = new InventoryItemSearchReqDto();
        searchRequest.setPage(0);
        searchRequest.setSize(10);
        searchRequest.setSortBy("createdAt");
        searchRequest.setSortDirection("desc");
        searchRequest.setKeyword("");
    }

    @Test
    @DisplayName("아이템 생성 성공")
    void createItem_Success() {
        // given
        given(userRepository.existsById(userId)).willReturn(true);
        given(inventoryItemRepository.save(any(InventoryItem.class))).willReturn(testItem);

        // when
        InventoryItemResDto result = inventoryItemService.createItem(userId, itemRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(testItem.getName());
        assertThat(result.getDescription()).isEqualTo(testItem.getDescription());
        assertThat(result.getCategory()).isEqualTo(testItem.getCategory().name());

        verify(userRepository).existsById(userId);
        verify(inventoryItemRepository).save(any(InventoryItem.class));
    }

    @Test
    @DisplayName("아이템 생성 실패 - 사용자 없음")
    void createItem_Fail_UserNotFound() {
        // given
        given(userRepository.existsById(userId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> inventoryItemService.createItem(userId, itemRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");

        verify(userRepository).existsById(userId);
        verify(inventoryItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("아이템 수정 성공")
    void updateItem_Success() {
        // given
        given(inventoryItemRepository.findByIdAndUserId(itemId, userId)).willReturn(Optional.of(testItem));
        given(inventoryItemRepository.save(any(InventoryItem.class))).willReturn(testItem);

        // when
        InventoryItemResDto result = inventoryItemService.updateItem(userId, itemId, itemRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(itemRequest.getName());

        verify(inventoryItemRepository).findByIdAndUserId(itemId, userId);
        verify(inventoryItemRepository).save(any(InventoryItem.class));
    }

    @Test
    @DisplayName("아이템 수정 실패 - 아이템 없음")
    void updateItem_Fail_ItemNotFound() {
        // given
        given(inventoryItemRepository.findByIdAndUserId(itemId, userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> inventoryItemService.updateItem(userId, itemId, itemRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아이템을 찾을 수 없습니다.");

        verify(inventoryItemRepository).findByIdAndUserId(itemId, userId);
        verify(inventoryItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("아이템 삭제 성공")
    void deleteItem_Success() {
        // given
        given(inventoryItemRepository.findByIdAndUserId(itemId, userId)).willReturn(Optional.of(testItem));

        // when
        inventoryItemService.deleteItem(userId, itemId);

        // then
        verify(inventoryItemRepository).findByIdAndUserId(itemId, userId);
        verify(inventoryItemRepository).delete(testItem);
    }

    @Test
    @DisplayName("아이템 삭제 실패 - 아이템 없음")
    void deleteItem_Fail_ItemNotFound() {
        // given
        given(inventoryItemRepository.findByIdAndUserId(itemId, userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> inventoryItemService.deleteItem(userId, itemId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아이템을 찾을 수 없습니다.");

        verify(inventoryItemRepository).findByIdAndUserId(itemId, userId);
        verify(inventoryItemRepository, never()).delete(any());
    }

    @Test
    @DisplayName("아이템 조회 성공")
    void getItem_Success() {
        // given
        given(inventoryItemRepository.findByIdAndUserId(itemId, userId)).willReturn(Optional.of(testItem));

        // when
        InventoryItemResDto result = inventoryItemService.getItem(userId, itemId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testItem.getId());
        assertThat(result.getName()).isEqualTo(testItem.getName());

        verify(inventoryItemRepository).findByIdAndUserId(itemId, userId);
    }

    @Test
    @DisplayName("아이템 조회 실패 - 아이템 없음")
    void getItem_Fail_ItemNotFound() {
        // given
        given(inventoryItemRepository.findByIdAndUserId(itemId, userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> inventoryItemService.getItem(userId, itemId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아이템을 찾을 수 없습니다.");

        verify(inventoryItemRepository).findByIdAndUserId(itemId, userId);
    }

    @Test
    @DisplayName("내 아이템 목록 조회 성공 - 키워드 없음")
    void getMyItems_Success_NoKeyword() {
        // given
        List<InventoryItem> items = Arrays.asList(testItem);
        Page<InventoryItem> itemsPage = new PageImpl<>(items);

        given(inventoryItemRepository.findByUserId(eq(userId), any(Pageable.class))).willReturn(itemsPage);

        // when
        Page<InventoryItemResDto> result = inventoryItemService.getMyItems(userId, searchRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo(testItem.getName());

        verify(inventoryItemRepository).findByUserId(eq(userId), any(Pageable.class));
    }

    @Test
    @DisplayName("내 아이템 목록 조회 성공 - 키워드 있음")
    void getMyItems_Success_WithKeyword() {
        // given
        searchRequest = new InventoryItemSearchReqDto();
        searchRequest.setPage(0);
        searchRequest.setSize(10);
        searchRequest.setSortBy("createdAt");
        searchRequest.setSortDirection("desc");
        searchRequest.setKeyword("테스트");

        List<InventoryItem> items = Arrays.asList(testItem);
        Page<InventoryItem> itemsPage = new PageImpl<>(items);

        given(inventoryItemRepository.findByUserIdAndNameOrDescriptionContaining(
                eq(userId), eq("테스트"), any(Pageable.class))).willReturn(itemsPage);

        // when
        Page<InventoryItemResDto> result = inventoryItemService.getMyItems(userId, searchRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        verify(inventoryItemRepository).findByUserIdAndNameOrDescriptionContaining(
                eq(userId), eq("테스트"), any(Pageable.class));
    }

    @Test
    @DisplayName("카테고리별 아이템 조회 성공")
    void getItemsByCategory_Success() {
        // given
        List<InventoryItem> items = Arrays.asList(testItem);
        given(inventoryItemRepository.findByUserIdAndCategory(userId, ItemCategory.ELECTRONICS)).willReturn(items);

        // when
        List<InventoryItemResDto> result = inventoryItemService.getItemsByCategory(userId, ItemCategory.ELECTRONICS);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo(ItemCategory.ELECTRONICS.name());

        verify(inventoryItemRepository).findByUserIdAndCategory(userId, ItemCategory.ELECTRONICS);
    }

    @Test
    @DisplayName("내 아이템 개수 조회 성공")
    void getMyItemCount_Success() {
        // given
        long expectedCount = 5L;
        given(inventoryItemRepository.countByUserId(userId)).willReturn(expectedCount);

        // when
        long result = inventoryItemService.getMyItemCount(userId);

        // then
        assertThat(result).isEqualTo(expectedCount);

        verify(inventoryItemRepository).countByUserId(userId);
    }
}