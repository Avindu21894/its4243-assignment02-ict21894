package smart_travel_platform.flight_service.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smart_travel_platform.flight_service.dtos.requests.CreateFlightRequestDto;
import smart_travel_platform.flight_service.dtos.requests.UpdateFlightRequestDto;
import smart_travel_platform.flight_service.dtos.response.BasicResponseDto;
import smart_travel_platform.flight_service.dtos.response.FlightDetailsResponseDto;
import smart_travel_platform.flight_service.exceptions.ApplicationException;
import smart_travel_platform.flight_service.services.FlightService;

@RestController
@RequestMapping("/api/flights")
@Tag(name = "FlightController", description = "Controller for managing flight operations")
@Slf4j
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }


    @PostMapping
    public ResponseEntity<BasicResponseDto> createFlight(
            @Valid @RequestBody CreateFlightRequestDto requestDto) throws ApplicationException {

        log.info("Received request to create a flight");
        BasicResponseDto response = flightService.createFlight(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<FlightDetailsResponseDto>> getAllFlights(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) throws ApplicationException {
        log.info("Received request to get all flights");
        Page<FlightDetailsResponseDto> flights = flightService.getAllFlights(page,size,sortBy,direction);
        return ResponseEntity.status(HttpStatus.OK).body(flights);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDetailsResponseDto> getFlightById(@PathVariable Long id) throws ApplicationException {
        log.info("Received request to get flight by id: {}", id);
        FlightDetailsResponseDto flight = flightService.getFlightById(id);
        return ResponseEntity.status(HttpStatus.OK).body(flight);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicResponseDto> updateFlightById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFlightRequestDto requestDto) throws ApplicationException {

        log.info("Received request to update flight with id: {}", id);
        BasicResponseDto response = flightService.updateFlightById(id, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponseDto> deleteFlight(@PathVariable Long id) throws ApplicationException {
        log.info("Received request to delete flight with id: {}", id);
        BasicResponseDto response = flightService.deleteFlight(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/reserve-seats")
    public ResponseEntity<BasicResponseDto> updateReservedSeats(
            @PathVariable Long id,
            @RequestParam int seatCount) throws ApplicationException {

        log.info("Received request to update seat reservation count for flight: {}", id);
        BasicResponseDto response = flightService.updateReservedSeats(id, seatCount);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> checkSeatAvailability(@PathVariable Long id) throws ApplicationException {
        log.info("Checking seat availability for flight {}", id);
        boolean available = flightService.isSeatAvailable(id);
        return ResponseEntity.status(HttpStatus.OK).body(available);
    }
}
