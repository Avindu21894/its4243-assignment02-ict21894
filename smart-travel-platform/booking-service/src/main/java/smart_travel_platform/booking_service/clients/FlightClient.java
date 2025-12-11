package smart_travel_platform.booking_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import smart_travel_platform.booking_service.dtos.response.BasicResponseDto;
import smart_travel_platform.booking_service.dtos.response.FlightDetailsResponseDto;

@FeignClient(name = "flight-service", url = "${flight-service.base-url}")
public interface FlightClient {

    @GetMapping("/api/flights/{id}")
    FlightDetailsResponseDto getFlightById(@PathVariable Long id);

    @GetMapping("/api/flights/{id}/availability")
    Boolean checkSeatAvailability(@PathVariable Long id);

    @PutMapping("/api/flights/{id}/reserve-seats")
    BasicResponseDto updateReservedSeats(
            @PathVariable Long id,
            @RequestParam int seatCount
    );
}