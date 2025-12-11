package smart_travel_platform.booking_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDetailsResponseDto {
    private Long id;
    private String name;
    private String location;
    private double pricePerNight;
    private int roomsCount;
    private int roomsReservedCount;
}
