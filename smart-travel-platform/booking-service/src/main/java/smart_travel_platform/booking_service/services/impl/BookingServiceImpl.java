package smart_travel_platform.booking_service.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import smart_travel_platform.booking_service.clients.FlightClient;
import smart_travel_platform.booking_service.clients.HotelClient;
import smart_travel_platform.booking_service.dtos.requests.CreateBookingRequestDto;
import smart_travel_platform.booking_service.dtos.requests.CreateNotificationRequestDto;
import smart_travel_platform.booking_service.dtos.response.*;
import smart_travel_platform.booking_service.entities.BookingModel;
import smart_travel_platform.booking_service.enums.BookingStatus;
import smart_travel_platform.booking_service.exceptions.ApplicationException;
import smart_travel_platform.booking_service.repositories.BookingRepository;
import smart_travel_platform.booking_service.services.BookingService;


@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final WebClient userWebClient;
    private final WebClient notificationWebClient;
    private final FlightClient flightClient;
    private final HotelClient hotelClient;
    public BookingServiceImpl(BookingRepository flightRepository, WebClient userWebClient, WebClient notificationWebClient, FlightClient flightClient, HotelClient hotelClient) {
        this.bookingRepository = flightRepository;
        this.userWebClient = userWebClient;
        this.notificationWebClient = notificationWebClient;
        this.flightClient = flightClient;
        this.hotelClient = hotelClient;
    }

    @Override
    public BasicResponseDto createBooking(CreateBookingRequestDto request) throws ApplicationException {
        try {
            UserDetailsResponseDto user = userWebClient.get()
                    .uri("/api/users/{id}", request.getUserId())
                    .retrieve()
                    .bodyToMono(UserDetailsResponseDto.class)
                    .block();
            if (user == null) {
                throw new ApplicationException(HttpStatus.NOT_FOUND, "User not found");
            }
            log.info("User {} validated successfully", request.getUserId());

            FlightDetailsResponseDto flight = flightClient.getFlightById(request.getFlightId());
            Boolean flightAvailable = flightClient.checkSeatAvailability(request.getFlightId());
            if (!flightAvailable) {
                throw new ApplicationException(HttpStatus.BAD_REQUEST, "Flight has no available seats");
            }
            log.info("Flight {} available", request.getFlightId());

            HotelDetailsResponseDto hotel = hotelClient.getHotelById(request.getHotelId());
            Boolean hotelAvailable = hotelClient.checkRoomAvailability(request.getHotelId());
            if (!hotelAvailable) {
                throw new ApplicationException(HttpStatus.BAD_REQUEST, "Hotel has no available rooms");
            }
            log.info("Hotel {} available", request.getHotelId());


            Double totalCost = flight.getPrice() + hotel.getPricePerNight();
            log.info("Total cost calculated: {}", totalCost);

            BookingModel booking = BookingModel.builder()
                    .userId(request.getUserId())
                    .flightId(request.getFlightId())
                    .hotelId(request.getHotelId())
                    .travelDate(request.getTravelDate())
                    .status(BookingStatus.PENDING)
                    .totalCost(totalCost)
                    .build();
            bookingRepository.save(booking);
            log.info("Booking {} saved as PENDING", booking.getId());

            CreateNotificationRequestDto notification = new CreateNotificationRequestDto(
                    "Booking " + booking.getId() + " confirmed",
                    request.getUserId()
            );
            notificationWebClient.post()
                    .uri("/api/notifications")
                    .bodyValue(notification)
                    .retrieve()
                    .bodyToMono(BasicResponseDto.class)
                    .block();
            log.info("Notification sent for booking {}", booking.getId());


            return new BasicResponseDto("Booking created");

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create booking: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create booking");
        }
    }


    @Override
    public BasicResponseDto confirmBooking(Long bookingId) throws ApplicationException {
        try{
            BookingModel booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                            "Booking not found"));
            FlightDetailsResponseDto flight = flightClient.getFlightById(booking.getFlightId());
            if (flight==null) {
                throw new ApplicationException(HttpStatus.BAD_REQUEST, "Flight has no available seats");
            }
            HotelDetailsResponseDto hotel = hotelClient.getHotelById(booking.getHotelId());
            if (hotel==null) {
                throw new ApplicationException(HttpStatus.BAD_REQUEST, "Hotel has no available rooms");
            }

            flightClient.updateReservedSeats(flight.getId(), 1);
            hotelClient.reserveHotelRooms(hotel.getId(), 1);
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

        CreateNotificationRequestDto notification = new CreateNotificationRequestDto(
                "Booking " + booking.getId() + " confirmed",
                booking.getUserId()
        );

        notificationWebClient.post()
                .uri("/api/notifications")
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(BasicResponseDto.class)
                .block();

            log.info("Booking {} confirmed and notification sent", booking.getId());
            return new BasicResponseDto("Booking confirmed");
        } catch (Exception e) {
            log.error("Failed to confirm booking: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,  e.getMessage());
        }
    }

    @Override
    public BookingDetailsResponseDto getBookingDetailsById(Long bookingId) throws ApplicationException {
        try {
            BookingModel booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, "Booking not found"));

            return BookingDetailsResponseDto.builder()
                    .id(booking.getId())
                    .userId(booking.getUserId())
                    .flightId(booking.getFlightId())
                    .hotelId(booking.getHotelId())
                    .travelDate(booking.getTravelDate())
                    .status(booking.getStatus())
                    .totalCost(booking.getTotalCost())
                    .build();
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching booking details: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch booking details");
        }
    }

    @Override
    public Page<BookingDetailsResponseDto> getAllBookings(int page, int size) throws ApplicationException {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BookingModel> bookings = bookingRepository.findAll(pageable);

            return bookings.map(b -> BookingDetailsResponseDto.builder()
                    .id(b.getId())
                    .userId(b.getUserId())
                    .flightId(b.getFlightId())
                    .hotelId(b.getHotelId())
                    .travelDate(b.getTravelDate())
                    .status(b.getStatus())
                    .totalCost(b.getTotalCost())
                    .build());
        } catch (Exception e) {
            log.error("Error fetching bookings: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch bookings");
        }
    }
}
