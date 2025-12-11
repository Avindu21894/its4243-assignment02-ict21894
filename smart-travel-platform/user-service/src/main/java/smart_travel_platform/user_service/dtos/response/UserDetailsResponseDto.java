package smart_travel_platform.user_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String country;
    private String city;
    private String address;
}
