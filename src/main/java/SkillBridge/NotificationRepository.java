package SkillBridge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Integer> {


    List<Notification> findByUsername(
            String username
    );


    long countByUsernameAndStatus(
            String username,
            String status
    );


    List<Notification> findByUsernameAndStatus(
            String username,
            String status
    );


    long countByType(
            String type
    );
}