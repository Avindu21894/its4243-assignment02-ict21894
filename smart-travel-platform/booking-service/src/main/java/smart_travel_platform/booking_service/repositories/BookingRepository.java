package smart_travel_platform.booking_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smart_travel_platform.booking_service.entities.BookingModel;

@Repository
public interface BookingRepository extends JpaRepository<BookingModel, Long> {
}
