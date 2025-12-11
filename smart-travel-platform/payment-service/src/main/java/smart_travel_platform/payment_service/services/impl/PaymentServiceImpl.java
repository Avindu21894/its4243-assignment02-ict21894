package smart_travel_platform.payment_service.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import smart_travel_platform.payment_service.dtos.requests.CreatePaymentRequestDto;
import smart_travel_platform.payment_service.dtos.response.BasicResponseDto;
import smart_travel_platform.payment_service.dtos.response.BookingDetailsResponseDto;
import smart_travel_platform.payment_service.dtos.response.UserDetailsResponseDto;
import smart_travel_platform.payment_service.entities.PaymentModel;
import smart_travel_platform.payment_service.exceptions.ApplicationException;
import smart_travel_platform.payment_service.repositories.PaymentRepository;
import smart_travel_platform.payment_service.services.PaymentService;


@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final WebClient userWebClient;
    private final WebClient bookingWebClient;
    private final PaymentRepository paymentRepository;
    public PaymentServiceImpl(WebClient userWebClient, WebClient bookingWebClient, PaymentRepository paymentRepository) {
        this.userWebClient = userWebClient;
        this.bookingWebClient = bookingWebClient;
        this.paymentRepository = paymentRepository;
    }


    @Override
    public BasicResponseDto processPayment(CreatePaymentRequestDto request)throws ApplicationException {

        try{
            UserDetailsResponseDto user = userWebClient.get()
                    .uri("/api/users/{id}", request.getUserId())
                    .retrieve()
                    .bodyToMono(UserDetailsResponseDto.class)
                    .block();
            if (user == null) {
                throw new ApplicationException(HttpStatus.NOT_FOUND, "User not found");
            }

            BookingDetailsResponseDto booking= bookingWebClient.get()
                    .uri("/api/bookings/{id}",request.getBookingId())
                    .retrieve()
                    .bodyToMono(BookingDetailsResponseDto.class)
                    .block();
            if(booking ==null){
                throw new ApplicationException(HttpStatus.NOT_FOUND, "User not found");
            }
            PaymentModel payment=PaymentModel.builder()
                    .userId(user.getId())
                    .bookingId(booking.getId())
                    .amount(request.getAmount())
                    .build();
            paymentRepository.save(payment);
            bookingWebClient.patch()
                    .uri("/api/bookings/{id}/confirm", booking.getId())
                    .retrieve()
                    .bodyToMono(BasicResponseDto.class)
                    .block();
            log.info("Booking {} confirmed after payment", booking.getId());
            return new BasicResponseDto("Payment processed and booking confirmed successfully");

        } catch (Exception e) {
            log.error("Failed to proceed payment: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }


    }
}
