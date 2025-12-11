package smart_travel_platform.hotel_service.services;

import org.springframework.data.domain.Page;
import smart_travel_platform.hotel_service.dtos.requests.CreateHotelRequestDto;
import smart_travel_platform.hotel_service.dtos.requests.UpdateHotelRequestDto;
import smart_travel_platform.hotel_service.dtos.responses.BasicResponseDto;
import smart_travel_platform.hotel_service.dtos.responses.HotelDetailsResponseDto;
import smart_travel_platform.hotel_service.exceptions.ApplicationException;


public interface HotelService {
    BasicResponseDto createHotel(CreateHotelRequestDto request) throws ApplicationException;

    Page<HotelDetailsResponseDto> getAllHotels(int page, int size, String sortBy, String direction) throws ApplicationException;

    HotelDetailsResponseDto getHotelById(Long id) throws ApplicationException;

    BasicResponseDto updateHotelById(Long id, UpdateHotelRequestDto request) throws ApplicationException;

    BasicResponseDto deleteHotel(Long id) throws ApplicationException;

    BasicResponseDto reserveHotelRooms(Long id, int rooms) throws ApplicationException;

    boolean isRoomsAvailable(Long hotelId) throws ApplicationException;
}
