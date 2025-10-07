package com.Backend.Projects.AirBnb.repository;

import com.Backend.Projects.AirBnb.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
