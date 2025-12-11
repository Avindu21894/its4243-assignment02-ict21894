package smart_travel_platform.notification_service.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smart_travel_platform.notification_service.dtos.requests.CreateNotificationRequestDto;
import smart_travel_platform.notification_service.dtos.responses.BasicResponseDto;
import smart_travel_platform.notification_service.exceptions.ApplicationException;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {
    @PostMapping
    public ResponseEntity<BasicResponseDto> notifyUser(@RequestBody CreateNotificationRequestDto request) throws ApplicationException {
        log.info("Received notification request for user {}", request.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(new BasicResponseDto("Notification sent successfully"));
    }
}
