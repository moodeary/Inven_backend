package com.basic.template.backend_template.domain.inventory.controller;

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
    public ResponseEntity<InventoryItemResDto> createItem(
            @Valid @RequestBody InventoryItemReqDto request) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        InventoryItemResDto response = inventoryItemService.createItem(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<InventoryItemResDto>> getMyItems(
            InventoryItemSearchReqDto searchRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Page<InventoryItemResDto> items = inventoryItemService.getMyItems(userId, searchRequest);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<InventoryItemResDto> getItem(
            @PathVariable Long itemId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        InventoryItemResDto response = inventoryItemService.getItem(userId, itemId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<InventoryItemResDto> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody InventoryItemReqDto request) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        InventoryItemResDto response = inventoryItemService.updateItem(userId, itemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long itemId) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        inventoryItemService.deleteItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<InventoryItemResDto>> getItemsByCategory(
            @PathVariable ItemCategory category) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<InventoryItemResDto> items = inventoryItemService.getItemsByCategory(userId, category);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getMyItemCount() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        long count = inventoryItemService.getMyItemCount(userId);
        return ResponseEntity.ok(count);
    }
}