package smart_travel_platform.booking_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import smart_travel_platform.booking_service.enums.BookingStatus;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingModel {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private Long flightId;
    private Long hotelId;
    private LocalDate travelDate;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private Double totalCost;
}