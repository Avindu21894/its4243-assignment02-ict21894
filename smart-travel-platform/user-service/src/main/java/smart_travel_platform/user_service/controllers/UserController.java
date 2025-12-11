package smart_travel_platform.user_service.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smart_travel_platform.user_service.dtos.requests.CreateUserRequestDto;
import smart_travel_platform.user_service.dtos.requests.UpdateUserRequestDto;
import smart_travel_platform.user_service.dtos.response.BasicResponseDto;
import smart_travel_platform.user_service.dtos.response.UserDetailsResponseDto;
import smart_travel_platform.user_service.exceptions.ApplicationException;
import smart_travel_platform.user_service.services.UserService;


@RestController
@RequestMapping("/api/users")
@Tag(name = "userController", description = "Controller for managing user operations")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<BasicResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto userRequestDto)
            throws ApplicationException {
        log.info("Received request to create user");
        BasicResponseDto response = userService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<UserDetailsResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) throws ApplicationException {
        log.info("Received request to get all users");
        Page<UserDetailsResponseDto> users = userService.getAllUsers(page, size, sortBy, direction);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponseDto> getUserById(@PathVariable("id") Long userId)
            throws ApplicationException {
        log.info("Received request to get user by id: {}", userId);
        UserDetailsResponseDto user = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicResponseDto> updateUserById(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UpdateUserRequestDto updateRequestDto
    ) throws ApplicationException {
        log.info("Received request to update user by id: {}", userId);
        BasicResponseDto response = userService.updateUserById(userId, updateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponseDto> deleteUserById(@PathVariable("id") Long userId)
            throws ApplicationException {
        log.info("Received request to delete user by id: {}", userId);
        BasicResponseDto response = userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
