
package SkillBridge;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository
        extends JpaRepository<Favorite, Integer> {

    List<Favorite> findByUsername(String username);

    Optional<Favorite> findByUsernameAndSkillId(
            String username, int skillId);

    boolean existsByUsernameAndSkillId(
            String username, int skillId);

    void deleteByUsernameAndSkillId(
            String username, int skillId);
}
