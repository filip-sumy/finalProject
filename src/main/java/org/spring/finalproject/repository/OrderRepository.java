package org.spring.finalproject.repository;

import org.spring.finalproject.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByIdAndClient_Email(Long id, String email);

    Page<Order> findByClientId(Long clientId, Pageable pageable);

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
