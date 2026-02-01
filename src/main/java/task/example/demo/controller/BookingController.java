package task.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.example.demo.config.OpenApiConfig;
import task.example.demo.entity.Booking;
import task.example.demo.entity.TicketPool;
import task.example.demo.service.BookingService;

import java.util.List;
import java.util.Map;

@Tag(name = "Events & Bookings", description = "Create events, list events, book tickets, and view bookings")
@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME) // shows lock + Authorize usage
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @Operation(
            summary = "Create an event",
            description = "Creates a new event (ticket pool) with total ticket count and ticket price. Requires JWT Bearer token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Event created successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "QA Nightmare Concert",
                                      "totalTickets": 50,
                                      "availableTickets": 50,
                                      "price": 25.5
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing/invalid token",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Invalid or expired token\"}")
                    )
            )
    })
    @PostMapping("/events")
    public ResponseEntity<TicketPool> createEvent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Event creation payload",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "QA Nightmare Concert",
                                      "totalTickets": 50,
                                      "price": 25.5
                                    }
                                    """)
                    )
            )
            @RequestBody Map<String, Object> request
    ) {
        String name = (String) request.get("name");
        int totalTickets = (Integer) request.get("totalTickets");
        double price = ((Number) request.get("price")).doubleValue();

        TicketPool pool = bookingService.createEvent(name, totalTickets, price);
        return ResponseEntity.ok(pool);
    }

    @Operation(
            summary = "List all events",
            description = "Returns all available events (ticket pools). Requires JWT Bearer token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of events returned"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing/invalid token",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Invalid or expired token\"}")
                    )
            )
    })
    @GetMapping("/events")
    public ResponseEntity<List<TicketPool>> getAllEvents() {
        return ResponseEntity.ok(bookingService.getAllEvents());
    }

    @Operation(
            summary = "Get event by ID",
            description = "Returns one event (ticket pool) by its ID. Requires JWT Bearer token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event returned"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing/invalid token",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Invalid or expired token\"}")
                    )
            )
    })
    @GetMapping("/events/{id}")
    public ResponseEntity<TicketPool> getEvent(@PathVariable Long id) {
        TicketPool pool = bookingService.getEvent(id);
        if (pool == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pool);
    }

    @Operation(
            summary = "Book tickets",
            description = "Books tickets for an event. Requires JWT Bearer token. "
                    + "Send eventId, username, and quantity in request body."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Booking confirmed",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 10,
                                      "username": "gio",
                                      "quantity": 2,
                                      "ticketPool": {
                                        "id": 1,
                                        "name": "QA Nightmare Concert"
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Booking rejected (e.g., not enough tickets)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Not enough tickets available\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing/invalid token",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Invalid or expired token\"}")
                    )
            )
    })
    @PostMapping("/book")
    public ResponseEntity<?> bookTickets(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Booking payload",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "eventId": 1,
                                      "username": "gio",
                                      "quantity": 2
                                    }
                                    """)
                    )
            )
            @RequestBody Map<String, Object> request
    ) {
        try {
            Long ticketPoolId = ((Number) request.get("eventId")).longValue();
            String username = (String) request.get("username");
            int quantity = (Integer) request.get("quantity");

            Booking booking = bookingService.bookTickets(ticketPoolId, username, quantity);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "List bookings for an event",
            description = "Returns all bookings for a given event ID. Requires JWT Bearer token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings returned"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing/invalid token",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\":\"Invalid or expired token\"}")
                    )
            )
    })
    @GetMapping("/events/{id}/bookings")
    public ResponseEntity<List<Booking>> getBookings(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingsForEvent(id));
    }
}
