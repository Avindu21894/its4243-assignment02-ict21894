package smart_travel_platform.flight_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightModel {
    @Id
    @GeneratedValue
    private Long id;
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
    private int seatsReservedCount;
}