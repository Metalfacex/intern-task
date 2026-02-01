package task.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "ticket_pool_id")
    private TicketPool ticketPool;

    private int quantity;

    private double totalPrice;

    private LocalDateTime bookedAt;

    public Booking(String username, TicketPool ticketPool, int quantity) {
        this.username = username;
        this.ticketPool = ticketPool;
        this.quantity = quantity;
        this.totalPrice = ticketPool.getPrice() * quantity;
        this.bookedAt = LocalDateTime.now();
    }
}
