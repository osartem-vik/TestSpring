package org.codegym.springbootdemo.repository;

import java.util.List;
import org.codegym.springbootdemo.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByUserId(Long userId);
}
