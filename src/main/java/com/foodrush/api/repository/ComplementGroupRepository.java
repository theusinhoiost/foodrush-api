package com.foodrush.api.repository;

import com.foodrush.api.model.ComplementGroup;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplementGroupRepository
  extends JpaRepository<ComplementGroup, UUID>
{
  List<ComplementGroup> findAllByMenuItemId(UUID menuItemId);
}
