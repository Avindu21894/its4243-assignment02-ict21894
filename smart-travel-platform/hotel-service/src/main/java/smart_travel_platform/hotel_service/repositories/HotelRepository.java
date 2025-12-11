package smart_travel_platform.hotel_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import smart_travel_platform.hotel_service.entities.HotelModel;

public interface HotelRepository extends JpaRepository<HotelModel, Long> {
}
