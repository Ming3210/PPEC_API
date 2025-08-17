package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("""
           SELECT o FROM Order o
           WHERE FUNCTION('MONTH', o.orderDate) = :month
             AND FUNCTION('YEAR',  o.orderDate) = :year
           """)
    List<Order> findByMonthAndYear(@Param("month") int month,
                                   @Param("year") int year);

    @Query("""
           SELECT o FROM Order o
           WHERE (:start IS NULL OR o.orderDate >= :start)
             AND (:end   IS NULL OR o.orderDate <= :end)
             AND (:keyword IS NULL OR LOWER(o.course.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
           ORDER BY o.orderDate DESC
           """)
    List<Order> searchOrders(@Param("start") LocalDate start,
                             @Param("end")   LocalDate end,
                             @Param("keyword") String keyword);
}
