package smart_travel_platform.payment_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentModel {
    @Id
    @GeneratedValue
    private Long id;
    private Long bookingId;
    private Long userId;
    private Double amount;
}