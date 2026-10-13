package SkillBridge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningRequestRepository
        extends JpaRepository<LearningRequest, Integer> {


    List<LearningRequest> findByMentorUsername(
            String mentorUsername
    );


    List<LearningRequest> findByStudentUsername(
            String studentUsername
    );


    LearningRequest
    findByStudentUsernameAndMentorUsernameAndSkillName(
            String studentUsername,
            String mentorUsername,
            String skillName
    );


    long countByStatus(
            String status
    );


    long countByMentorUsernameAndStatus(
            String mentorUsername,
            String status
    );


    long countByStudentUsernameAndStatus(
            String studentUsername,
            String status
    );


    List<LearningRequest>
    findByMentorUsernameAndStatus(
            String mentorUsername,
            String status
    );


    List<LearningRequest>
    findByStudentUsernameAndStatus(
            String studentUsername,
            String status
    );
}