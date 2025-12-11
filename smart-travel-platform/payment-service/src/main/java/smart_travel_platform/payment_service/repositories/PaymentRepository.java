package smart_travel_platform.payment_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smart_travel_platform.payment_service.entities.PaymentModel;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {
}
