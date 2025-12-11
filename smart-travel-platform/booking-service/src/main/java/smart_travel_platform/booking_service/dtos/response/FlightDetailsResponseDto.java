package smart_travel_platform.booking_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightDetailsResponseDto {
    private Long id;
    private String flightNumber;
    private String airline;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDate departureTime;
    private String arrivalTime;
    private Double price;
    private int seatsCount;
    private int seatsReservedCount;
}
