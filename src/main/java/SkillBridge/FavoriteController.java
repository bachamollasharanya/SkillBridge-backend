
package SkillBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private SkillRepository skillRepository;

    // Get all favorite skills for a user
    @GetMapping("/favorites/{username}")
    public List<Favorite> getFavorites(
            @PathVariable String username) {

        return favoriteRepository.findByUsername(username);
    }

    // Save a skill as favorite
    @PostMapping("/favorites/add/{username}/{skillId}")
    public String addFavorite(
            @PathVariable String username,
            @PathVariable int skillId) {

        Skill skill = skillRepository.findById(skillId).orElse(null);

        if (skill == null) {
            return "Skill Not Found";
        }

        boolean alreadySaved =
                favoriteRepository.existsByUsernameAndSkillId(
                        username, skillId);

        if (alreadySaved) {
            return "Already Added to Favorites";
        }

        Favorite favorite = new Favorite();
        favorite.setUsername(username);
        favorite.setSkillId(skillId);
        favorite.setSkillName(skill.getSkillName());

        favoriteRepository.save(favorite);

        return "Added to Favorites Successfully";
    }

    // Remove a skill from favorites
    @DeleteMapping("/favorites/remove/{username}/{skillId}")
    public String removeFavorite(
            @PathVariable String username,
            @PathVariable int skillId) {

        if (!favoriteRepository.existsByUsernameAndSkillId(
                username, skillId)) {
            return "Favorite Not Found";
        }

        favoriteRepository.deleteByUsernameAndSkillId(
                username, skillId);

        return "Removed from Favorites Successfully";
    }
}

