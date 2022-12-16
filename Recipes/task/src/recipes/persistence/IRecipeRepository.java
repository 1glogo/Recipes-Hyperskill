package recipes.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recipes.business.Recipe;

import java.util.List;
//relevant queries have been created or overridden using JPA for RECIPES
@Repository
public interface IRecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findRecipeById(long id);
    List<Recipe> findRecipeByNameContainingIgnoreCase(String Name);
    List<Recipe> findRecipeByCategoryIgnoreCase(String Name);
}
