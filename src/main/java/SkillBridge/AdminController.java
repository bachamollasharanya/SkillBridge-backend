
package SkillBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private LearningRequestRepository learningRequestRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping("/admin/users")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/admin/students")
    public List<Users> getAllStudents() {
        return userRepository.findByRole("STUDENT");
    }

    @GetMapping("/admin/mentors")
    public List<Users> getAllMentors() {
        return userRepository.findByRole("MENTOR");
    }

    @GetMapping("/admin/skills")
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @GetMapping("/admin/requests")
    public List<LearningRequest> getAllRequests() {
        return learningRequestRepository.findAll();
    }

    @GetMapping("/admin/sessions")
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @GetMapping("/admin/statistics")
    public Map<String, Long> getStatistics() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("totalSkills", skillRepository.count());
        stats.put("totalRequests", learningRequestRepository.count());
        stats.put("totalSessions", sessionRepository.count());

        return stats;
    }
}
