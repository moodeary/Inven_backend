package com.basic.template.backend_template.domain.inventory.service;

import com.basic.template.backend_template.domain.inventory.dto.InventoryItemReqDto;
import com.basic.template.backend_template.domain.inventory.dto.InventoryItemResDto;
import com.basic.template.backend_template.domain.inventory.dto.InventoryItemSearchReqDto;
import com.basic.template.backend_template.domain.inventory.entity.ItemCategory;
import org.springframework.data.domain.Page;

import java.util.List;


public interface InventoryItemService {

    InventoryItemResDto createItem(Long userId, InventoryItemReqDto request);

    InventoryItemResDto updateItem(Long userId, Long itemId, InventoryItemReqDto request);

    void deleteItem(Long userId, Long itemId);

    InventoryItemResDto getItem(Long userId, Long itemId);

    Page<InventoryItemResDto> getMyItems(Long userId, InventoryItemSearchReqDto searchRequest);

    List<InventoryItemResDto> getItemsByCategory(Long userId, ItemCategory category);

    long getMyItemCount(Long userId);
}