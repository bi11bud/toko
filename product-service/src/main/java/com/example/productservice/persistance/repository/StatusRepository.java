package com.example.productservice.persistance.repository;

import com.example.productservice.persistance.model.Status;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends CrudRepository<Status, String> {

    @Cacheable(cacheNames = { "status_findByTypeAndCode" })
    Status findByTypeAndCode(String type, String code);
}
