package com.basic.template.backend_template.domain.inventory.entity;

import com.basic.template.backend_template.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inventory_items")
public class InventoryItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ItemCategory category;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ItemStatus status;

    @Column(name = "purchase_price", precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "current_value", precision = 10, scale = 2)
    private BigDecimal currentValue;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Builder
    public InventoryItem(Long userId, String name, String description, ItemCategory category,
                         Integer quantity, ItemStatus status, BigDecimal purchasePrice,
                         BigDecimal currentValue, String location, String imageUrl) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.status = status;
        this.purchasePrice = purchasePrice;
        this.currentValue = currentValue;
        this.location = location;
        this.imageUrl = imageUrl;
    }
}