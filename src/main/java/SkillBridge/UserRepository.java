package SkillBridge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository
        extends JpaRepository<Users, Integer> {


    Users findByUsername(
            String username
    );


    List<Users> findByRole(
            String role
    );


    long countByRole(
            String role
    );


    boolean existsByEmail(
            String email
    );


    boolean existsByUsername(
            String username
    );


    List<Users> findByRoleAndUsernameContainingIgnoreCase(
            String role,
            String username
    );


    List<Users> findByUsernameContainingIgnoreCase(
            String username
    );
}