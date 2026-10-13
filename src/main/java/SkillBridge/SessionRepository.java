package SkillBridge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository
        extends JpaRepository<Session, Integer> {


    List<Session> findByStudentUsername(
            String username
    );


    List<Session> findByMentorUsername(
            String username
    );


    Session
    findByStudentUsernameAndMentorUsernameAndSkillNameAndDateAndTime(
            String studentUsername,
            String mentorUsername,
            String skillName,
            String date,
            String time
    );


    Session
    findByMentorUsernameAndDateAndTimeAndStatus(
            String mentorUsername,
            String date,
            String time,
            String status
    );


    long countByStatus(
            String status
    );


    long countByStudentUsernameAndStatus(
            String username,
            String status
    );


    long countByMentorUsernameAndStatus(
            String username,
            String status
    );


    List<Session>
    findByStudentUsernameAndStatus(
            String username,
            String status
    );


    List<Session>
    findByMentorUsernameAndStatus(
            String username,
            String status
    );


    List<Session>
    findByMentorUsernameAndDate(
            String username,
            String date
    );


    List<Session>
    findByStudentUsernameAndDate(
            String username,
            String date
    );
}