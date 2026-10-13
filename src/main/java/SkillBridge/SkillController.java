package SkillBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class SkillController {

    @Autowired
    SkillRepository sr;

    @Autowired
    UserRepository ur;


    @PostMapping("/skill/add")
    public String addSkill(
            @RequestBody Skill skill) {

        if (skill.getUsername() == null ||
                skill.getUsername().trim().isEmpty()) {

            return "Username is required";
        }


        if (skill.getSkillName() == null ||
                skill.getSkillName().trim().isEmpty()) {

            return "Skill name is required";
        }


        if (skill.getSkillType() == null ||
                skill.getSkillType().trim().isEmpty()) {

            return "Skill type is required";
        }


        String skillType =
                skill.getSkillType()
                        .toUpperCase();


        if (!skillType.equals("KNOW") &&
                !skillType.equals("LEARN")) {

            return "Invalid skill type";
        }


        skill.setSkillName(
                skill.getSkillName().trim()
        );


        skill.setSkillType(
                skillType
        );


        sr.save(skill);


        return "Skill Added Successfully";
    }


    @GetMapping("/skill/{username}")
    public List<Skill> getSkills(
            @PathVariable String username) {

        return sr.findByUsername(
                username
        );
    }


    @DeleteMapping("/skill/{id}")
    public String deleteSkill(
            @PathVariable int id) {

        if (!sr.existsById(id)) {

            return "Skill Not Found";
        }


        sr.deleteById(id);


        return "Skill Deleted Successfully";
    }


    @GetMapping(
            "/mentor/search/{skillName}"
    )
    public List<Skill> searchMentors(
            @PathVariable String skillName) {

        if (skillName == null ||
                skillName.trim().isEmpty()) {

            return new ArrayList<>();
        }


        List<Skill> skills =
                sr.findBySkillNameIgnoreCaseAndSkillType(
                        skillName.trim(),
                        "KNOW"
                );


        List<Skill> mentors =
                new ArrayList<>();


        for (Skill skill : skills) {

            Users user =
                    ur.findByUsername(
                            skill.getUsername()
                    );


            if (user != null &&
                    user.getRole() != null &&
                    user.getRole()
                            .equalsIgnoreCase(
                                    "MENTOR"
                            )) {

                mentors.add(skill);
            }
        }


        return mentors;
    }
}