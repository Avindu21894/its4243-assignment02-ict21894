package smart_travel_platform.flight_service.dtos.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFlightRequestDto {
    @NotBlank(message = "Flight Number is required")
    private String flightNumber;
    @NotBlank(message = "Airline is required")
    private String airline;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDate departureTime;
    private String arrivalTime;
    private Double price;
    private int seatsCount;
}
