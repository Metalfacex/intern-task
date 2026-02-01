package task.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.example.demo.entity.Booking;
import task.example.demo.entity.TicketPool;
import task.example.demo.repository.BookingRepository;
import task.example.demo.repository.TicketPoolRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final TicketPoolRepository ticketPoolRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public Booking bookTickets(Long ticketPoolId, String username, int quantity) {
        if (quantity <= 0) throw new RuntimeException("Quantity must be > 0");


        simulateProcessingDelay();

        int updated = ticketPoolRepository.tryDecrementTickets(ticketPoolId, quantity);
        if (updated == 0) {


            if (!ticketPoolRepository.existsById(ticketPoolId)) {
                throw new RuntimeException("Event not found");
            }
            throw new RuntimeException("Not enough tickets available");
        }


        TicketPool pool = ticketPoolRepository.findById(ticketPoolId)
                .orElseThrow(() -> new RuntimeException("Event not found"));


        Booking booking = new Booking(username, pool, quantity);
        return bookingRepository.save(booking);
    }

    public TicketPool createEvent(String name, int totalTickets, double price) {
        TicketPool pool = new TicketPool(name, totalTickets, price);
        return ticketPoolRepository.save(pool);
    }

    public TicketPool getEvent(Long id) {
        return ticketPoolRepository.findById(id).orElse(null);
    }

    public List<Booking> getBookingsForEvent(Long ticketPoolId) {
        return bookingRepository.findByTicketPoolId(ticketPoolId);
    }

    public List<TicketPool> getAllEvents() {
        return ticketPoolRepository.findAll();
    }

    private void simulateProcessingDelay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
