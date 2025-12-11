package smart_travel_platform.user_service.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import smart_travel_platform.user_service.dtos.requests.CreateUserRequestDto;
import smart_travel_platform.user_service.dtos.requests.UpdateUserRequestDto;
import smart_travel_platform.user_service.dtos.response.BasicResponseDto;
import smart_travel_platform.user_service.dtos.response.UserDetailsResponseDto;
import smart_travel_platform.user_service.entities.UserModel;
import smart_travel_platform.user_service.exceptions.ApplicationException;
import smart_travel_platform.user_service.repositories.UserRepository;
import smart_travel_platform.user_service.services.UserService;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BasicResponseDto createUser(CreateUserRequestDto requestDto) throws ApplicationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ApplicationException(HttpStatus.CONFLICT,
                    "user with email " + requestDto.getEmail() + " already exists");
        }

        UserModel user=UserModel.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .country(requestDto.getCountry())
                .city(requestDto.getCity())
                .address(requestDto.getAddress())
                .build();
        try{
            userRepository.save(user);
        } catch (Exception e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,"An error occurred while creating the user");
        }
        log.info("User Created");
        return new BasicResponseDto("User Created Successfully");
    }

    @Override
    public Page<UserDetailsResponseDto> getAllUsers(int page, int size, String sortBy, String direction) throws ApplicationException {
        Sort sort = Sort.by(direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        try {
            Page<UserModel> users = userRepository.findAll(pageable);

            if (users.isEmpty()) {
                throw new ApplicationException(HttpStatus.NOT_FOUND, "No users found");
            }
            return users.map(user -> UserDetailsResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .country(user.getCountry())
                    .city(user.getCity())
                    .address(user.getAddress())
                    .build());

        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while retrieving users");
        }
    }

    @Override
    public UserDetailsResponseDto getUserById(Long userId) throws ApplicationException {
        try {
            UserModel user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                            "User with id " + userId + " not found"));

            return UserDetailsResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .country(user.getCountry())
                    .city(user.getCity())
                    .address(user.getAddress())
                    .build();

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving user: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while fetching the user");
        }
    }

    @Override
    public BasicResponseDto updateUserById(Long userId, UpdateUserRequestDto requestDto) throws ApplicationException {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND,
                        "User with id " + userId + " not found"));

        user.setName(requestDto.getName());
        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setCountry(requestDto.getCountry());
        user.setCity(requestDto.getCity());
        user.setAddress(requestDto.getAddress());

        try {
            userRepository.save(user);
            log.info("User updated successfully: {}", userId);
            return new BasicResponseDto("User Updated Successfully");
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while updating the user");
        }
    }

    @Override
    public BasicResponseDto deleteUser(Long userId) throws ApplicationException {
        if (!userRepository.existsById(userId)) {
            throw new ApplicationException(HttpStatus.NOT_FOUND,
                    "User with id " + userId + " not found");
        }

        try {
            userRepository.deleteById(userId);
            log.info("User deleted successfully: {}", userId);
            return new BasicResponseDto("User Deleted Successfully");
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while deleting the user");
        }
    }
}
