package com.basic.template.backend_template.domain.inventory.dto;

import com.basic.template.backend_template.domain.inventory.entity.ItemCategory;
import com.basic.template.backend_template.domain.inventory.entity.ItemStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InventoryItemSearchReqDto {

    private String keyword;
    private ItemCategory category;
    private ItemStatus status;
    private int page = 0;
    private int size = 20;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
}