package smart_travel_platform.flight_service.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import smart_travel_platform.flight_service.dtos.requests.CreateFlightRequestDto;
import smart_travel_platform.flight_service.dtos.requests.UpdateFlightRequestDto;
import smart_travel_platform.flight_service.dtos.response.BasicResponseDto;
import smart_travel_platform.flight_service.dtos.response.FlightDetailsResponseDto;
import smart_travel_platform.flight_service.entities.FlightModel;
import smart_travel_platform.flight_service.exceptions.ApplicationException;
import smart_travel_platform.flight_service.repositories.FlightRepository;
import smart_travel_platform.flight_service.services.FlightService;


@Service
@Slf4j
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;


    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public BasicResponseDto createFlight(CreateFlightRequestDto requestDto) throws ApplicationException {
        try {
            FlightModel flight = FlightModel.builder()
                    .flightNumber(requestDto.getFlightNumber())
                    .airline(requestDto.getAirline())
                    .departureAirport(requestDto.getDepartureAirport())
                    .arrivalAirport(requestDto.getArrivalAirport())
                    .departureTime(requestDto.getDepartureTime())
                    .arrivalTime(requestDto.getArrivalTime())
                    .price(requestDto.getPrice())
                    .seatsCount(requestDto.getSeatsCount())
                    .seatsReservedCount(0)
                    .build();

            flightRepository.save(flight);
        } catch (Exception e) {
            log.error("Error creating flight: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while creating the flight");
        }

        log.info("Flight created successfully");
        return new BasicResponseDto("Flight created successfully");
    }

    @Override
    public Page<FlightDetailsResponseDto> getAllFlights(int page, int size, String sortBy, String direction) throws ApplicationException {

        Sort sort = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        try {
            Page<FlightModel> flights = flightRepository.findAll(pageable);
            return flights.map(flight -> FlightDetailsResponseDto.builder()
                    .id(flight.getId())
                    .flightNumber(flight.getFlightNumber())
                    .airline(flight.getAirline())
                    .departureAirport(flight.getDepartureAirport())
                    .arrivalAirport(flight.getArrivalAirport())
                    .departureTime(flight.getDepartureTime())
                    .arrivalTime(flight.getArrivalTime())
                    .price(flight.getPrice())
                    .seatsCount(flight.getSeatsCount())
                    .seatsReservedCount(flight.getSeatsReservedCount())
                    .build());
        } catch (Exception e) {
            log.error("Failed to fetch flight list: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch flights");
        }
    }

    @Override
    public FlightDetailsResponseDto getFlightById(Long id) throws ApplicationException {
        try {
            FlightModel flight = flightRepository.findById(id)
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                            "Flight with id " + id + " not found"));

            return FlightDetailsResponseDto.builder()
                    .id(flight.getId())
                    .flightNumber(flight.getFlightNumber())
                    .airline(flight.getAirline())
                    .departureAirport(flight.getDepartureAirport())
                    .arrivalAirport(flight.getArrivalAirport())
                    .departureTime(flight.getDepartureTime())
                    .arrivalTime(flight.getArrivalTime())
                    .price(flight.getPrice())
                    .seatsCount(flight.getSeatsCount())
                    .seatsReservedCount(flight.getSeatsReservedCount())
                    .build();
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving flight: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving flight details");
        }
    }

    @Override
    public BasicResponseDto updateFlightById(Long id, UpdateFlightRequestDto requestDto) throws ApplicationException {

        FlightModel flight = flightRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                        "Flight with id " + id + " not found"));

        try {
            flight.setFlightNumber(requestDto.getFlightNumber());
            flight.setAirline(requestDto.getAirline());
            flight.setDepartureAirport(requestDto.getDepartureAirport());
            flight.setArrivalAirport(requestDto.getArrivalAirport());
            flight.setDepartureTime(requestDto.getDepartureTime());
            flight.setArrivalTime(requestDto.getArrivalTime());
            flight.setPrice(requestDto.getPrice());
            flight.setSeatsCount(requestDto.getSeatsCount());

            flightRepository.save(flight);
        } catch (Exception e) {
            log.error("Error updating flight: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update flight");
        }

        return new BasicResponseDto("Flight updated successfully");
    }

    @Override
    public BasicResponseDto deleteFlight(Long id) throws ApplicationException {
        FlightModel flight = flightRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                        "Flight with id " + id + " not found"));

        try {
            flightRepository.delete(flight);
        } catch (Exception e) {
            log.error("Error deleting flight: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete flight");
        }

        return new BasicResponseDto("Flight deleted successfully");
    }

    @Override
    public BasicResponseDto updateReservedSeats(Long flightId, int seatCount) throws ApplicationException {
        FlightModel flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                        "Flight with id " + flightId + " not found"));

        if (flight.getSeatsReservedCount() + seatCount > flight.getSeatsCount()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Not enough available seats");
        }

        flight.setSeatsReservedCount(flight.getSeatsReservedCount() + seatCount);
        flightRepository.save(flight);

        return new BasicResponseDto("Reserved seats updated successfully");
    }

    @Override
    public boolean isSeatAvailable(Long flightId) throws ApplicationException {
        FlightModel flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                        "Flight with id " + flightId + " not found"));

        return flight.getSeatsReservedCount() < flight.getSeatsCount();
    }
}
