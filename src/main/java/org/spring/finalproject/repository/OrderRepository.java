package org.spring.finalproject.repository;

import org.spring.finalproject.entity.Client;
import org.spring.finalproject.entity.OrderStatus;
import org.spring.finalproject.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository
        extends JpaRepository<Order, Long> {

    boolean existsByIdAndClient_Email(Long id, String email);
    List<Order> findByClientId(Long clientId);
    Page<Order> findByClientId(Long clientId, Pageable pageable);

    List<Order> findByStatus(OrderStatus status);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    //Search order by email
    @Query("SELECT o FROM Order o JOIN o.client c "
            + "WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<Order> findByClientEmailContaining(
            @Param("email") String email,
            Pageable pageable);

    boolean existsByClient_Id(Long clientId);

    @Query("""
    SELECT o
    FROM Order o
    JOIN o.client c
    WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Order> search(
            @Param("search") String search,
            Pageable pageable);

    @Query("""
    SELECT o
    FROM Order o
    JOIN o.client c
    WHERE o.client.id = :clientId
      AND (
            LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))
         OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
         OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
      )
""")
    Page<Order> searchByClientId(
            @Param("clientId") Long clientId,
            @Param("search") String search,
            Pageable pageable);
}
