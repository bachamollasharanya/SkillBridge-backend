package SkillBridge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository
        extends JpaRepository<Skill, Integer> {


    List<Skill> findByUsername(
            String username
    );


    List<Skill>
    findBySkillNameIgnoreCaseAndSkillType(
            String skillName,
            String skillType
    );


    List<Skill>
    findBySkillNameContainingIgnoreCase(
            String skillName
    );


    List<Skill>
    findBySkillType(
            String skillType
    );


    List<Skill>
    findBySkillNameContainingIgnoreCaseAndSkillType(
            String skillName,
            String skillType
    );


    long countBySkillType(
            String skillType
    );
}