package SkillBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    @Autowired
    NotificationRepository notificationRepository;


    @PostMapping("/notification/add")
    public String addNotification(
            @RequestBody Notification notification) {

        if (notification.getUsername() == null ||
                notification.getUsername()
                        .trim()
                        .isEmpty()) {

            return "Username is required";
        }


        if (notification.getMessage() == null ||
                notification.getMessage()
                        .trim()
                        .isEmpty()) {

            return "Notification message is required";
        }


        notification.setStatus(
                "UNREAD"
        );


        notificationRepository.save(
                notification
        );


        return "Notification Added Successfully";
    }


    @GetMapping(
            "/notification/{username}"
    )
    public List<Notification>
    getNotifications(
            @PathVariable String username) {

        return notificationRepository
                .findByUsername(username);
    }


    @PutMapping(
            "/notification/read/{id}"
    )
    public String markAsRead(
            @PathVariable int id) {

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElse(null);


        if (notification == null) {

            return "Notification Not Found";
        }


        notification.setStatus(
                "READ"
        );


        notificationRepository.save(
                notification
        );


        return "Notification Marked As Read";
    }


    @DeleteMapping(
            "/notification/{id}"
    )
    public String deleteNotification(
            @PathVariable int id) {

        if (!notificationRepository
                .existsById(id)) {

            return "Notification Not Found";
        }


        notificationRepository
                .deleteById(id);


        return "Notification Deleted Successfully";
    }
}