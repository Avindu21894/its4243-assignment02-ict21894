package smart_travel_platform.hotel_service.dtos.requests;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateHotelRequestDto {
    private String name;
    private String location;
    @Min(value = 1, message = "Price must be greater than 0")
    private Double pricePerNight;
    @Min(value = 1, message = "Rooms count must be greater than 0")
    private Integer roomsCount;
}
