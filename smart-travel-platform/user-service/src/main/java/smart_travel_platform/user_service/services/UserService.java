package smart_travel_platform.user_service.services;

import org.springframework.data.domain.Page;
import smart_travel_platform.user_service.dtos.requests.CreateUserRequestDto;
import smart_travel_platform.user_service.dtos.requests.UpdateUserRequestDto;
import smart_travel_platform.user_service.dtos.response.BasicResponseDto;
import smart_travel_platform.user_service.dtos.response.UserDetailsResponseDto;
import smart_travel_platform.user_service.exceptions.ApplicationException;

public interface UserService {
    public BasicResponseDto createUser(CreateUserRequestDto requestDto)throws ApplicationException;
    public Page<UserDetailsResponseDto>getAllUsers(int page, int size, String sortBy, String direction) throws ApplicationException;
    public UserDetailsResponseDto getUserById(Long userId) throws ApplicationException;
    public BasicResponseDto updateUserById(Long userId, UpdateUserRequestDto requestDto)throws ApplicationException;
    public BasicResponseDto deleteUser(Long userId) throws ApplicationException;
}
