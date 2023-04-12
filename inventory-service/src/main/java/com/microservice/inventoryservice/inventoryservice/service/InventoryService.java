package com.microservice.inventoryservice.inventoryservice.service;

import com.microservice.inventoryservice.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        return this.inventoryRepository.findBySkuCode().isPresent();
    }


}
