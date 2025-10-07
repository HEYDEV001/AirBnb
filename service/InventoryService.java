package com.Backend.Projects.AirBnb.service;

import com.Backend.Projects.AirBnb.entities.Inventory;
import com.Backend.Projects.AirBnb.entities.Room;

public interface InventoryService {
    void createInventory(Room room);
    void deleteInventory();
}
