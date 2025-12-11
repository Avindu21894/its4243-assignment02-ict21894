package smart_travel_platform.booking_service.dtos.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingRequestDto {
    private Long userId;
    private Long flightId;
    private Long hotelId;
    private LocalDate travelDate;
}
