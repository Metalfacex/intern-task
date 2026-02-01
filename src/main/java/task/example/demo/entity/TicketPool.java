package task.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_pools")
@Data
@NoArgsConstructor
public class TicketPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;

    private int totalTickets;

    private int availableTickets;

    private double price;

    public TicketPool(String eventName, int totalTickets, double price) {
        this.eventName = eventName;
        this.totalTickets = totalTickets;
        this.availableTickets = totalTickets;
        this.price = price;
    }
}
