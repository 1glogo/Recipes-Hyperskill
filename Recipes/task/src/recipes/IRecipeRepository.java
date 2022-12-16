package recipes;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findRecipeById(long id);

    List<Recipe> findRecipeByNameContainingIgnoreCase(String Name);

    List<Recipe> findRecipeByCategoryIgnoreCase(String Name);
}
