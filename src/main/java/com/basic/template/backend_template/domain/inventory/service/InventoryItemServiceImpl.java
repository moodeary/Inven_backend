package com.basic.template.backend_template.domain.inventory.service;

import com.basic.template.backend_template.domain.inventory.dto.InventoryItemReqDto;
import com.basic.template.backend_template.domain.inventory.dto.InventoryItemResDto;
import com.basic.template.backend_template.domain.inventory.dto.InventoryItemSearchReqDto;
import com.basic.template.backend_template.domain.inventory.entity.InventoryItem;
import com.basic.template.backend_template.domain.inventory.entity.ItemCategory;
import com.basic.template.backend_template.domain.inventory.repository.InventoryItemRepository;
import com.basic.template.backend_template.domain.user.entity.User;
import com.basic.template.backend_template.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final UserRepository userRepository;

    @Override
    public InventoryItemResDto createItem(Long userId, InventoryItemReqDto request) {
        // 사용자 존재 여부만 확인
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        InventoryItem item = InventoryItem.builder()
                .userId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .quantity(request.getQuantity())
                .status(request.getStatus())
                .purchasePrice(request.getPurchasePrice())
                .currentValue(request.getCurrentValue())
                .location(request.getLocation())
                .imageUrl(request.getImageUrl())
                .build();

        InventoryItem savedItem = inventoryItemRepository.save(item);
        return InventoryItemResDto.from(savedItem);
    }

    @Override
    public InventoryItemResDto updateItem(Long userId, Long itemId, InventoryItemReqDto request) {
        InventoryItem item = inventoryItemRepository.findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        // 업데이트 로직 (엔티티에 업데이트 메소드 추가 필요)
        InventoryItem updatedItem = InventoryItem.builder()
                .userId(item.getUserId())
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .quantity(request.getQuantity())
                .status(request.getStatus())
                .purchasePrice(request.getPurchasePrice())
                .currentValue(request.getCurrentValue())
                .location(request.getLocation())
                .imageUrl(request.getImageUrl())
                .build();

        InventoryItem savedItem = inventoryItemRepository.save(updatedItem);
        return InventoryItemResDto.from(savedItem);
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        InventoryItem item = inventoryItemRepository.findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        inventoryItemRepository.delete(item);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryItemResDto getItem(Long userId, Long itemId) {
        InventoryItem item = inventoryItemRepository.findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        return InventoryItemResDto.from(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryItemResDto> getMyItems(Long userId, InventoryItemSearchReqDto searchRequest) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(searchRequest.getSortDirection())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC,
                searchRequest.getSortBy()
        );

        Pageable pageable = PageRequest.of(
                searchRequest.getPage(),
                searchRequest.getSize(),
                sort
        );

        Page<InventoryItem> itemsPage;

        if (searchRequest.getKeyword() != null && !searchRequest.getKeyword().trim().isEmpty()) {
            itemsPage = inventoryItemRepository.findByUserIdAndNameOrDescriptionContaining(
                    userId, searchRequest.getKeyword(), pageable
            );
        } else {
            itemsPage = inventoryItemRepository.findByUserId(userId, pageable);
        }

        return itemsPage.map(InventoryItemResDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemResDto> getItemsByCategory(Long userId, ItemCategory category) {
        List<InventoryItem> items = inventoryItemRepository.findByUserIdAndCategory(userId, category);
        return items.stream()
                .map(InventoryItemResDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getMyItemCount(Long userId) {
        return inventoryItemRepository.countByUserId(userId);
    }
}