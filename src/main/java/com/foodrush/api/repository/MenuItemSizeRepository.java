package com.foodrush.api.repository;

import com.foodrush.api.model.MenuItemSize;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemSizeRepository
  extends JpaRepository<MenuItemSize, UUID>
{
  List<MenuItemSize> findAllByMenuItemIdAndAvailableTrue(UUID menuItemId);

  /**
   * Locks the record with a SELECT FOR UPDATE to prevent race conditions (overselling)
   * during the checkout transaction.
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT s FROM MenuItemSize s WHERE s.id = :id")
  Optional<MenuItemSize> findByIdWithPessimisticLock(@Param("id") UUID id);
}
