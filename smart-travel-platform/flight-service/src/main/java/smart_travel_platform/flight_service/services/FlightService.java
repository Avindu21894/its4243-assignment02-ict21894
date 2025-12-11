package smart_travel_platform.flight_service.services;

import org.springframework.data.domain.Page;
import smart_travel_platform.flight_service.dtos.requests.CreateFlightRequestDto;
import smart_travel_platform.flight_service.dtos.requests.UpdateFlightRequestDto;
import smart_travel_platform.flight_service.dtos.response.BasicResponseDto;
import smart_travel_platform.flight_service.dtos.response.FlightDetailsResponseDto;
import smart_travel_platform.flight_service.exceptions.ApplicationException;


public interface FlightService {
    BasicResponseDto createFlight(CreateFlightRequestDto requestDto) throws ApplicationException;

    Page<FlightDetailsResponseDto> getAllFlights(int page, int size, String sortBy, String direction) throws ApplicationException;

    FlightDetailsResponseDto getFlightById(Long id) throws ApplicationException;

    BasicResponseDto updateFlightById(Long id, UpdateFlightRequestDto requestDto) throws ApplicationException;

    BasicResponseDto deleteFlight(Long id) throws ApplicationException;

    BasicResponseDto updateReservedSeats(Long flightId, int seatCount) throws ApplicationException;

    boolean isSeatAvailable(Long flightId) throws ApplicationException;
}
