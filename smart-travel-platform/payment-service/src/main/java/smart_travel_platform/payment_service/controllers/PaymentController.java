package smart_travel_platform.payment_service.controllers;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smart_travel_platform.payment_service.dtos.requests.CreatePaymentRequestDto;
import smart_travel_platform.payment_service.dtos.response.BasicResponseDto;
import smart_travel_platform.payment_service.exceptions.ApplicationException;
import smart_travel_platform.payment_service.services.PaymentService;



@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<BasicResponseDto> processPayment(
            @Valid @RequestBody CreatePaymentRequestDto request
    ) throws ApplicationException {
        log.info("Received payment request for booking {} by user {}", request.getBookingId(), request.getUserId());
        BasicResponseDto response = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
