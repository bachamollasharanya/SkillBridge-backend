
package SkillBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class LearningRequestController {

    @Autowired
    LearningRequestRepository lrr;

    @Autowired
    NotificationRepository notificationRepository;

    // Send a learning request
    @PostMapping("/request/send")
    public String sendRequest(@RequestBody LearningRequest request) {

        if (request.getStudentUsername() == null ||
                request.getStudentUsername().trim().isEmpty()) {
            return "Student username is required";
        }

        if (request.getMentorUsername() == null ||
                request.getMentorUsername().trim().isEmpty()) {
            return "Mentor username is required";
        }

        if (request.getSkillName() == null ||
                request.getSkillName().trim().isEmpty()) {
            return "Skill name is required";
        }

        LearningRequest existingRequest =
                lrr.findByStudentUsernameAndMentorUsernameAndSkillName(
                        request.getStudentUsername(),
                        request.getMentorUsername(),
                        request.getSkillName()
                );

        if (existingRequest != null) {

            if ("PENDING".equalsIgnoreCase(existingRequest.getStatus())) {
                return "Request Already Sent";
            }

            if ("ACCEPTED".equalsIgnoreCase(existingRequest.getStatus())) {
                return "You Are Already Learning This Skill From This Mentor";
            }

            if ("REJECTED".equalsIgnoreCase(existingRequest.getStatus())) {
                existingRequest.setStatus("PENDING");
                lrr.save(existingRequest);

                createNotification(
                        request.getMentorUsername(),
                        "New learning request from "
                                + request.getStudentUsername()
                                + " for "
                                + request.getSkillName(),
                        "LEARNING_REQUEST"
                );

                return "Learning Request Sent Successfully";
            }
        }

        request.setStatus("PENDING");

        if (request.getProgress() == null ||
                request.getProgress().trim().isEmpty()) {
            request.setProgress("Not Started");
        }

        lrr.save(request);

        createNotification(
                request.getMentorUsername(),
                "New learning request from "
                        + request.getStudentUsername()
                        + " for "
                        + request.getSkillName(),
                "LEARNING_REQUEST"
        );

        return "Learning Request Sent Successfully";
    }

    // Get requests received by a mentor
    @GetMapping("/request/mentor/{username}")
    public List<LearningRequest> getMentorRequests(
            @PathVariable String username) {

        return lrr.findByMentorUsername(username);
    }

    // Get requests sent by a student
    @GetMapping("/request/student/{username}")
    public List<LearningRequest> getStudentRequests(
            @PathVariable String username) {

        return lrr.findByStudentUsername(username);
    }

    // Update learning request status: ACCEPTED, REJECTED or PENDING
    @PutMapping("/request/{id}/{status}")
    public String updateRequestStatus(
            @PathVariable int id,
            @PathVariable String status) {

        LearningRequest request = lrr.findById(id).orElse(null);

        if (request == null) {
            return "Request Not Found";
        }

        String newStatus = status.toUpperCase();

        if (!newStatus.equals("ACCEPTED") &&
                !newStatus.equals("REJECTED") &&
                !newStatus.equals("PENDING")) {
            return "Invalid request status";
        }

        request.setStatus(newStatus);
        lrr.save(request);

        if (newStatus.equals("ACCEPTED")) {
            createNotification(
                    request.getStudentUsername(),
                    "Your learning request for "
                            + request.getSkillName()
                            + " has been accepted by "
                            + request.getMentorUsername(),
                    "LEARNING_REQUEST"
            );

        } else if (newStatus.equals("REJECTED")) {
            createNotification(
                    request.getStudentUsername(),
                    "Your learning request for "
                            + request.getSkillName()
                            + " has been rejected by "
                            + request.getMentorUsername(),
                    "LEARNING_REQUEST"
            );

        } else {
            createNotification(
                    request.getStudentUsername(),
                    "Your learning request status has been updated.",
                    "LEARNING_REQUEST"
            );
        }

        return "Request " + newStatus + " Successfully";
    }

    // Update learning progress
    @PutMapping("/request/progress/{id}")
    public String updateLearningProgress(
            @PathVariable int id,
            @RequestParam String progress) {

        LearningRequest request = lrr.findById(id).orElse(null);

        if (request == null) {
            return "Learning Request Not Found";
        }

        String newProgress = progress.trim();

        if (!newProgress.equalsIgnoreCase("Not Started")
                && !newProgress.equalsIgnoreCase("In Progress")
                && !newProgress.equalsIgnoreCase("Completed")) {
            return "Invalid progress value";
        }

        if (!"ACCEPTED".equalsIgnoreCase(request.getStatus())) {
            return "Progress can only be updated for accepted requests";
        }

        if (newProgress.equalsIgnoreCase("Not Started")) {
            request.setProgress("Not Started");
        } else if (newProgress.equalsIgnoreCase("In Progress")) {
            request.setProgress("In Progress");
        } else {
            request.setProgress("Completed");
        }

        lrr.save(request);

        return "Learning Progress Updated Successfully";
    }

    // Create notifications
    private void createNotification(
            String username,
            String message,
            String type) {

        Notification notification = new Notification();

        notification.setUsername(username);
        notification.setMessage(message);
        notification.setType(type);
        notification.setStatus("UNREAD");

        notificationRepository.save(notification);
    }
}
