package smart_travel_platform.flight_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smart_travel_platform.flight_service.entities.FlightModel;

@Repository
public interface FlightRepository extends JpaRepository<FlightModel, Long> {
}
