package smart_travel_platform.booking_service.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smart_travel_platform.booking_service.dtos.requests.CreateBookingRequestDto;
import smart_travel_platform.booking_service.dtos.response.BasicResponseDto;
import smart_travel_platform.booking_service.dtos.response.BookingDetailsResponseDto;
import smart_travel_platform.booking_service.exceptions.ApplicationException;
import smart_travel_platform.booking_service.services.BookingService;

@RestController
@RequestMapping("/api/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BasicResponseDto> createBooking(@RequestBody CreateBookingRequestDto request)
            throws ApplicationException {
        log.info("Received booking request for user {}", request.getUserId());
        BasicResponseDto booking = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<BasicResponseDto> confirmBooking(@PathVariable Long id) throws ApplicationException {
        log.info("Received request to confirm booking {}", id);
        BasicResponseDto booking = bookingService.confirmBooking(id);
        return ResponseEntity.ok(booking);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingDetailsResponseDto> getBookingDetails(@PathVariable Long id)
            throws ApplicationException {
        log.info("Fetching booking details for id {}", id);
        BookingDetailsResponseDto booking = bookingService.getBookingDetailsById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping
    public ResponseEntity<Page<BookingDetailsResponseDto>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws ApplicationException {
        log.info("Fetching all bookings page {} size {}", page, size);
        Page<BookingDetailsResponseDto> bookings = bookingService.getAllBookings(page, size);
        return ResponseEntity.ok(bookings);
    }
}