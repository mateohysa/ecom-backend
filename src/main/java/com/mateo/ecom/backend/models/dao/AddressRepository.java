package com.mateo.ecom.backend.models.dao;

import com.mateo.ecom.backend.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAddressByUserId(Long userId);


}
