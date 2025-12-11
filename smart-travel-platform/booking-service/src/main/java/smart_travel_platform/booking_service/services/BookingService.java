package smart_travel_platform.booking_service.services;

import org.springframework.data.domain.Page;
import smart_travel_platform.booking_service.dtos.requests.CreateBookingRequestDto;
import smart_travel_platform.booking_service.dtos.response.BasicResponseDto;
import smart_travel_platform.booking_service.dtos.response.BookingDetailsResponseDto;
import smart_travel_platform.booking_service.exceptions.ApplicationException;

public interface BookingService {
    BasicResponseDto createBooking(CreateBookingRequestDto request) throws ApplicationException;
    BasicResponseDto confirmBooking(Long bookingId) throws ApplicationException;
    BookingDetailsResponseDto getBookingDetailsById(Long bookingId) throws ApplicationException;
    Page<BookingDetailsResponseDto> getAllBookings(int page, int size) throws ApplicationException;
}
