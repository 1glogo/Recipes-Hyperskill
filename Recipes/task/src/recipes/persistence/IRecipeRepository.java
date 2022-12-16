package recipes.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recipes.persistence.Recipe;

import java.util.List;

@Repository
public interface IRecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findRecipeById(long id);

    List<Recipe> findRecipeByNameContainingIgnoreCase(String Name);

    List<Recipe> findRecipeByCategoryIgnoreCase(String Name);
}
