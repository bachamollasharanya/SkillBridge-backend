package SkillBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class SessionController {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    NotificationRepository notificationRepository;


    @PostMapping("/session/add")
    public String addSession(
            @RequestBody Session session) {

        if (session.getStudentUsername() == null ||
                session.getStudentUsername().trim().isEmpty()) {

            return "Student username is required";
        }


        if (session.getMentorUsername() == null ||
                session.getMentorUsername().trim().isEmpty()) {

            return "Mentor username is required";
        }


        if (session.getSkillName() == null ||
                session.getSkillName().trim().isEmpty()) {

            return "Skill name is required";
        }


        if (session.getDate() == null ||
                session.getDate().trim().isEmpty()) {

            return "Date is required";
        }


        if (session.getTime() == null ||
                session.getTime().trim().isEmpty()) {

            return "Time is required";
        }


        try {

            LocalDate date =
                    LocalDate.parse(
                            session.getDate()
                    );

            LocalTime time =
                    LocalTime.parse(
                            session.getTime()
                    );

            LocalDateTime sessionDateTime =
                    LocalDateTime.of(
                            date,
                            time
                    );

            if (sessionDateTime.isBefore(
                    LocalDateTime.now()
            )) {

                return "Session date and time must be in the future";
            }

        } catch (DateTimeParseException e) {

            return "Invalid date or time";
        }


        Session existingSession =
                sessionRepository
                        .findByStudentUsernameAndMentorUsernameAndSkillNameAndDateAndTime(
                                session.getStudentUsername(),
                                session.getMentorUsername(),
                                session.getSkillName(),
                                session.getDate(),
                                session.getTime()
                        );


        if (existingSession != null) {

            return "This session is already scheduled";
        }


        Session mentorSession =
                sessionRepository
                        .findByMentorUsernameAndDateAndTimeAndStatus(
                                session.getMentorUsername(),
                                session.getDate(),
                                session.getTime(),
                                "SCHEDULED"
                        );


        if (mentorSession != null) {

            return "Mentor is already busy at this time";
        }


        session.setStatus(
                "SCHEDULED"
        );


        sessionRepository.save(
                session
        );


        Notification notification =
                new Notification();


        notification.setUsername(
                session.getMentorUsername()
        );


        notification.setMessage(
                "New learning session scheduled by "
                + session.getStudentUsername()
                + " for "
                + session.getSkillName()
                + " on "
                + session.getDate()
                + " at "
                + session.getTime()
        );


        notification.setType(
                "SESSION"
        );


        notification.setStatus(
                "UNREAD"
        );


        notificationRepository.save(
                notification
        );


        return "Session Scheduled Successfully";
    }


    @GetMapping("/session/student/{username}")
    public List<Session> getStudentSessions(
            @PathVariable String username) {

        return sessionRepository
                .findByStudentUsername(
                        username
                );
    }


    @GetMapping("/session/mentor/{username}")
    public List<Session> getMentorSessions(
            @PathVariable String username) {

        return sessionRepository
                .findByMentorUsername(
                        username
                );
    }


    @PutMapping("/session/{id}/{status}")
    public String updateSessionStatus(
            @PathVariable int id,
            @PathVariable String status) {

        Session session =
                sessionRepository
                        .findById(id)
                        .orElse(null);


        if (session == null) {

            return "Session Not Found";
        }


        String newStatus =
                status.toUpperCase();


        if (!newStatus.equals("COMPLETED") &&
                !newStatus.equals("CANCELLED") &&
                !newStatus.equals("SCHEDULED")) {

            return "Invalid session status";
        }


        if (session.getStatus().equals("CANCELLED")) {

            return "Cancelled session cannot be changed";
        }


        if (session.getStatus().equals("COMPLETED") &&
                !newStatus.equals("COMPLETED")) {

            return "Completed session cannot be changed";
        }


        session.setStatus(
                newStatus
        );


        sessionRepository.save(
                session
        );


        Notification studentNotification =
                new Notification();


        studentNotification.setUsername(
                session.getStudentUsername()
        );


        if (newStatus.equals("COMPLETED")) {

            studentNotification.setMessage(
                    "Your learning session for "
                    + session.getSkillName()
                    + " with "
                    + session.getMentorUsername()
                    + " has been completed."
            );

        } else if (
                newStatus.equals("CANCELLED")
        ) {

            studentNotification.setMessage(
                    "Your learning session for "
                    + session.getSkillName()
                    + " with "
                    + session.getMentorUsername()
                    + " has been cancelled."
            );

        } else {

            studentNotification.setMessage(
                    "Your session status has been updated."
            );
        }


        studentNotification.setType(
                "SESSION"
        );


        studentNotification.setStatus(
                "UNREAD"
        );


        notificationRepository.save(
                studentNotification
        );


        if (newStatus.equals("CANCELLED")) {

            Notification mentorNotification =
                    new Notification();


            mentorNotification.setUsername(
                    session.getMentorUsername()
            );


            mentorNotification.setMessage(
                    "Your session with "
                    + session.getStudentUsername()
                    + " for "
                    + session.getSkillName()
                    + " on "
                    + session.getDate()
                    + " at "
                    + session.getTime()
                    + " has been cancelled."
            );


            mentorNotification.setType(
                    "SESSION"
            );


            mentorNotification.setStatus(
                    "UNREAD"
            );


            notificationRepository.save(
                    mentorNotification
            );
        }


        return "Session "
                + newStatus
                + " Successfully";
    }


    @DeleteMapping("/session/{id}")
    public String deleteSession(
            @PathVariable int id) {

        Session session =
                sessionRepository
                        .findById(id)
                        .orElse(null);


        if (session == null) {

            return "Session Not Found";
        }


        session.setStatus(
                "CANCELLED"
        );


        sessionRepository.save(
                session
        );


        return "Session Cancelled Successfully";
    }
}