package com.basic.template.backend_template.domain.inventory.repository;

import com.basic.template.backend_template.domain.inventory.entity.InventoryItem;
import com.basic.template.backend_template.domain.inventory.entity.ItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Page<InventoryItem> findByUserId(Long userId, Pageable pageable);

    Optional<InventoryItem> findByIdAndUserId(Long id, Long userId);

    List<InventoryItem> findByUserIdAndCategory(Long userId, ItemCategory category);

    @Query("SELECT i FROM InventoryItem i WHERE i.userId = :userId AND " +
           "(i.name LIKE %:keyword% OR i.description LIKE %:keyword%)")
    Page<InventoryItem> findByUserIdAndNameOrDescriptionContaining(
            @Param("userId") Long userId,
            @Param("keyword") String keyword,
            Pageable pageable);

    long countByUserId(Long userId);
}