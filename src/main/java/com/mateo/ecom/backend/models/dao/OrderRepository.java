package com.mateo.ecom.backend.models.dao;

import com.mateo.ecom.backend.models.AppOrder;
import com.mateo.ecom.backend.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<AppOrder, Long> {

    List<AppOrder> findByUser(AppUser user);
}
