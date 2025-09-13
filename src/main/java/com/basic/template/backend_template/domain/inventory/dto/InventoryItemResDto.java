package com.basic.template.backend_template.domain.inventory.dto;

import com.basic.template.backend_template.domain.inventory.entity.InventoryItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemResDto {

    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer quantity;
    private String status;
    private BigDecimal purchasePrice;
    private BigDecimal currentValue;
    private String location;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InventoryItemResDto from(InventoryItem item) {
        return InventoryItemResDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .category(item.getCategory().name())
                .quantity(item.getQuantity())
                .status(item.getStatus().name())
                .purchasePrice(item.getPurchasePrice())
                .currentValue(item.getCurrentValue())
                .location(item.getLocation())
                .imageUrl(item.getImageUrl())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}