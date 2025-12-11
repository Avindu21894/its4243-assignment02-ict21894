package smart_travel_platform.payment_service.services;


import smart_travel_platform.payment_service.dtos.requests.CreatePaymentRequestDto;
import smart_travel_platform.payment_service.dtos.response.BasicResponseDto;
import smart_travel_platform.payment_service.exceptions.ApplicationException;

public interface PaymentService {
    BasicResponseDto processPayment(CreatePaymentRequestDto request) throws ApplicationException;
}
