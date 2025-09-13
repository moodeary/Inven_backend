package com.basic.template.backend_template.domain.inventory.dto;

import com.basic.template.backend_template.domain.inventory.entity.ItemCategory;
import com.basic.template.backend_template.domain.inventory.entity.ItemStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class InventoryItemReqDto {

    @NotBlank(message = "아이템 이름은 필수입니다")
    @Size(max = 100, message = "아이템 이름은 100자 이하여야 합니다")
    private String name;

    @Size(max = 500, message = "설명은 500자 이하여야 합니다")
    private String description;

    @NotNull(message = "카테고리는 필수입니다")
    private ItemCategory category;

    @NotNull(message = "수량은 필수입니다")
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다")
    private Integer quantity;

    @NotNull(message = "상태는 필수입니다")
    private ItemStatus status;

    @DecimalMin(value = "0.0", inclusive = false, message = "구매가격은 0보다 커야 합니다")
    private BigDecimal purchasePrice;

    @DecimalMin(value = "0.0", inclusive = false, message = "현재가격은 0보다 커야 합니다")
    private BigDecimal currentValue;

    @Size(max = 100, message = "위치는 100자 이하여야 합니다")
    private String location;

    @Size(max = 255, message = "이미지 URL은 255자 이하여야 합니다")
    private String imageUrl;
}