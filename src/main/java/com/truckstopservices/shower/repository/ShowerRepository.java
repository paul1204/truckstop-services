package com.truckstopservices.shower.repository;

import com.truckstopservices.shower.entity.Shower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowerRepository extends JpaRepository<Shower, Long> {
    Optional<Shower> findByShowerNumber(String showerNumber);
    
    @Query("SELECT s FROM Shower s WHERE s.occupied = false AND s.cleaning = false")
    List<Shower> findAvailableShowers();
    
    @Query("SELECT s FROM Shower s WHERE s.customerName = ?1")
    Optional<Shower> findByCustomerName(String customerName);
    
    @Query("SELECT s FROM Shower s WHERE s.cleaning = true")
    List<Shower> findShowersInCleaning();
}