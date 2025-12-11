package smart_travel_platform.hotel_service.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import smart_travel_platform.hotel_service.dtos.requests.CreateHotelRequestDto;
import smart_travel_platform.hotel_service.dtos.requests.UpdateHotelRequestDto;
import smart_travel_platform.hotel_service.dtos.responses.BasicResponseDto;
import smart_travel_platform.hotel_service.dtos.responses.HotelDetailsResponseDto;
import smart_travel_platform.hotel_service.exceptions.ApplicationException;
import smart_travel_platform.hotel_service.services.HotelService;

@RestController
@RequestMapping("/api/hotels")
@Tag(name = "HotelController", description = "Controller for managing hotel operations")
@Slf4j
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    public ResponseEntity<BasicResponseDto> createHotel(@Valid @RequestBody CreateHotelRequestDto request) throws ApplicationException {
        log.info("Received request to create hotel: {}", request.getName());
        BasicResponseDto response = hotelService.createHotel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<HotelDetailsResponseDto>> getAllHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) throws ApplicationException {
        log.info("Received request to get all hotels");
        Page<HotelDetailsResponseDto> hotels = hotelService.getAllHotels(page, size, sortBy, direction);
        return ResponseEntity.status(HttpStatus.OK).body(hotels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDetailsResponseDto> getHotelById(@PathVariable Long id) throws ApplicationException {
        log.info("Received request to get hotel by id: {}", id);
        HotelDetailsResponseDto hotel = hotelService.getHotelById(id);
        return ResponseEntity.status(HttpStatus.OK).body(hotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicResponseDto> updateHotelById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateHotelRequestDto request
    ) throws ApplicationException {
        log.info("Received request to update hotel id: {}", id);
        BasicResponseDto response = hotelService.updateHotelById(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponseDto> deleteHotel(@PathVariable Long id) throws ApplicationException {
        log.info("Received request to delete hotel id: {}", id);
        BasicResponseDto response = hotelService.deleteHotel(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/reserve")
    public ResponseEntity<BasicResponseDto> reserveHotelRooms(
            @PathVariable Long id,
            @RequestParam int rooms
    ) throws ApplicationException {
        log.info("Received request to reserve {} rooms for hotel id: {}", rooms, id);
        BasicResponseDto response = hotelService.reserveHotelRooms(id, rooms);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> checkRoomAvailability(@PathVariable Long id) throws ApplicationException {
        log.info("Received request to check availability for hotel id: {}", id);
        boolean available = hotelService.isRoomsAvailable(id);
        return ResponseEntity.status(HttpStatus.OK).body(available);
    }
}
