package smart_travel_platform.booking_service.clients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import smart_travel_platform.booking_service.dtos.response.BasicResponseDto;
import smart_travel_platform.booking_service.dtos.response.HotelDetailsResponseDto;

@FeignClient(name = "hotel-service", url = "${hotel-service.base-url}")
public interface HotelClient {

    @GetMapping("/api/hotels/{id}")
    HotelDetailsResponseDto getHotelById(@PathVariable Long id);

    @GetMapping("/api/hotels/{id}/availability")
    Boolean checkRoomAvailability(@PathVariable Long id);

    @PutMapping("/api/hotels/{id}/reserve")
    BasicResponseDto reserveHotelRooms(
            @PathVariable Long id,
            @RequestParam int rooms
    );

}
