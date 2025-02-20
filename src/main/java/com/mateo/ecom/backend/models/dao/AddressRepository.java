package com.mateo.ecom.backend.models.dao;

import com.mateo.ecom.backend.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    // For getting a single address (used by PUT and PATCH endpoints)
    Optional<Address> findAddressByUser_Id(Long id);
    
    // For getting all addresses of a user (used by GET endpoint)
    List<Address> findAddressesByUser_Id(Long id);
}
