package com.Backend.Projects.AirBnb.service;

import com.Backend.Projects.AirBnb.entities.Room;

public interface InventoryService {
    void InitialiseInventory(Room room);
    void deleteAllInventory(Room room);
}
