package smart_travel_platform.hotel_service.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import smart_travel_platform.hotel_service.dtos.requests.CreateHotelRequestDto;
import smart_travel_platform.hotel_service.dtos.requests.UpdateHotelRequestDto;
import smart_travel_platform.hotel_service.dtos.responses.BasicResponseDto;
import smart_travel_platform.hotel_service.dtos.responses.HotelDetailsResponseDto;
import smart_travel_platform.hotel_service.entities.HotelModel;
import smart_travel_platform.hotel_service.exceptions.ApplicationException;
import smart_travel_platform.hotel_service.repositories.HotelRepository;
import smart_travel_platform.hotel_service.services.HotelService;

@Service
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public BasicResponseDto createHotel(CreateHotelRequestDto request) throws ApplicationException {
        try {
            HotelModel hotel = HotelModel.builder()
                    .name(request.getName())
                    .location(request.getLocation())
                    .pricePerNight(request.getPricePerNight())
                    .roomsCount(request.getRoomsCount())
                    .roomsReservedCount(0)
                    .build();

            hotelRepository.save(hotel);
        } catch (Exception e) {
            log.error("Error creating hotel: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while creating the hotel");
        }

        log.info("Hotel created successfully");
        return new BasicResponseDto("Hotel created successfully");
    }

    @Override
    public Page<HotelDetailsResponseDto> getAllHotels(int page, int size, String sortBy, String direction) throws ApplicationException {
        Sort sort = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        try {
            Page<HotelModel> hotels = hotelRepository.findAll(pageable);

            return hotels.map(hotel -> HotelDetailsResponseDto.builder()
                    .id(hotel.getId())
                    .name(hotel.getName())
                    .location(hotel.getLocation())
                    .pricePerNight(hotel.getPricePerNight())
                    .roomsCount(hotel.getRoomsCount())
                    .roomsReservedCount(hotel.getRoomsReservedCount())
                    .build());
        } catch (Exception e) {
            log.error("Failed to fetch hotels: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch hotels");
        }
    }

    @Override
    public HotelDetailsResponseDto getHotelById(Long id) throws ApplicationException {
        try {
            HotelModel hotel = hotelRepository.findById(id)
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                            "Hotel with id " + id + " not found"));

            return HotelDetailsResponseDto.builder()
                    .id(hotel.getId())
                    .name(hotel.getName())
                    .location(hotel.getLocation())
                    .pricePerNight(hotel.getPricePerNight())
                    .roomsCount(hotel.getRoomsCount())
                    .roomsReservedCount(hotel.getRoomsReservedCount())
                    .build();

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving hotel: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving hotel details");
        }
    }

    @Override
    public BasicResponseDto updateHotelById(Long id, UpdateHotelRequestDto request) throws ApplicationException {
        HotelModel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                        "Hotel with id " + id + " not found"));

        try {
            if (request.getName() != null) hotel.setName(request.getName());
            if (request.getLocation() != null) hotel.setLocation(request.getLocation());
            if (request.getPricePerNight() != null) hotel.setPricePerNight(request.getPricePerNight());
            if (request.getRoomsCount() != null) hotel.setRoomsCount(request.getRoomsCount());

            hotelRepository.save(hotel);
        } catch (Exception e) {
            log.error("Error updating hotel: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to update hotel");
        }

        return new BasicResponseDto("Hotel updated successfully");
    }

    @Override
    public BasicResponseDto deleteHotel(Long id) throws ApplicationException {
        HotelModel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                        "Hotel with id " + id + " not found"));

        try {
            hotelRepository.delete(hotel);
        } catch (Exception e) {
            log.error("Error deleting hotel: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to delete hotel");
        }

        return new BasicResponseDto("Hotel deleted successfully");
    }

    @Override
    public BasicResponseDto reserveHotelRooms(Long id, int rooms) throws ApplicationException {
        HotelModel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                        "Hotel with id " + id + " not found"));

        if (hotel.getRoomsReservedCount() + rooms > hotel.getRoomsCount()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Not enough available rooms");
        }

        try {
            hotel.setRoomsReservedCount(hotel.getRoomsReservedCount() + rooms);
            hotelRepository.save(hotel);
        } catch (Exception e) {
            log.error("Error reserving rooms: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to reserve rooms");
        }

        return new BasicResponseDto("Rooms reserved successfully");
    }

    @Override
    public boolean isRoomsAvailable(Long hotelId) throws ApplicationException {
        HotelModel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                        "Hotel with id " + hotelId + " not found"));

        return hotel.getRoomsReservedCount() < hotel.getRoomsCount();
    }
}
