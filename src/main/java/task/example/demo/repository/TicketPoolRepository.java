package task.example.demo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import task.example.demo.entity.TicketPool;

@Repository
public interface TicketPoolRepository extends JpaRepository<TicketPool, Long> {
    @Modifying
    @Transactional
    @Query("""
        update TicketPool p
           set p.availableTickets = p.availableTickets - :qty
         where p.id = :id
           and p.availableTickets >= :qty
    """)
    int tryDecrementTickets(@Param("id") Long id, @Param("qty") int qty);
}
