package com.foodrush.api.repository;

import com.foodrush.api.model.ComplementItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplementItemRepository
  extends JpaRepository<ComplementItem, UUID>
{
  List<ComplementItem> findAllByGroupIdAndAvailableTrue(UUID groupId);
}
