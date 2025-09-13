package com.basic.template.backend_template.domain.inventory.controller;

import com.basic.template.backend_template.common.dto.ResponseApi;
import com.basic.template.backend_template.common.exception.BusinessException;
import com.basic.template.backend_template.common.util.SecurityUtil;
import com.basic.template.backend_template.domain.inventory.dto.InventoryItemReqDto;
import com.basic.template.backend_template.domain.inventory.dto.InventoryItemResDto;
import com.basic.template.backend_template.domain.inventory.dto.InventoryItemSearchReqDto;
import com.basic.template.backend_template.domain.inventory.entity.ItemCategory;
import com.basic.template.backend_template.domain.inventory.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;


    @PostMapping
    public ResponseEntity<ResponseApi<InventoryItemResDto>> createItem(
            @Valid @RequestBody InventoryItemReqDto request) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw BusinessException.unauthorized("인증이 필요합니다.");
        }

        InventoryItemResDto response = inventoryItemService.createItem(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseApi.success(response, "아이템이 생성되었습니다."));
    }

    @GetMapping
    public ResponseEntity<ResponseApi<Page<InventoryItemResDto>>> getMyItems(
            InventoryItemSearchReqDto searchRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw BusinessException.unauthorized("인증이 필요합니다.");
        }

        Page<InventoryItemResDto> items = inventoryItemService.getMyItems(userId, searchRequest);
        return ResponseEntity.ok(ResponseApi.success(items, "아이템 목록을 조회했습니다."));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ResponseApi<InventoryItemResDto>> getItem(
            @PathVariable Long itemId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw BusinessException.unauthorized("인증이 필요합니다.");
        }

        InventoryItemResDto response = inventoryItemService.getItem(userId, itemId);
        return ResponseEntity.ok(ResponseApi.success(response, "아이템을 조회했습니다."));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ResponseApi<InventoryItemResDto>> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody InventoryItemReqDto request) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw BusinessException.unauthorized("인증이 필요합니다.");
        }

        InventoryItemResDto response = inventoryItemService.updateItem(userId, itemId, request);
        return ResponseEntity.ok(ResponseApi.success(response, "아이템이 수정되었습니다."));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ResponseApi<Void>> deleteItem(
            @PathVariable Long itemId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw BusinessException.unauthorized("인증이 필요합니다.");
        }

        inventoryItemService.deleteItem(userId, itemId);
        return ResponseEntity.ok(ResponseApi.success(null, "아이템이 삭제되었습니다."));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseApi<List<InventoryItemResDto>>> getItemsByCategory(
            @PathVariable ItemCategory category) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw BusinessException.unauthorized("인증이 필요합니다.");
        }

        List<InventoryItemResDto> items = inventoryItemService.getItemsByCategory(userId, category);
        return ResponseEntity.ok(ResponseApi.success(items, "카테고리별 아이템을 조회했습니다."));
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseApi<Long>> getMyItemCount() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw BusinessException.unauthorized("인증이 필요합니다.");
        }

        long count = inventoryItemService.getMyItemCount(userId);
        return ResponseEntity.ok(ResponseApi.success(count, "보유 아이템 수를 조회했습니다."));
    }
}