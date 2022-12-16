package recipes.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipes.business.Recipe;
import java.time.LocalDateTime;

//Recipe class as per clients requirements
@Data
@NoArgsConstructor
@AllArgsConstructor
//Recipe for client format
public class RecipeClient {

    private String name;
    private String description;
    private String[] ingredients;
    private String[] directions;
    private String category;
    private LocalDateTime date;

    public RecipeClient(Recipe recipe) {
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.ingredients = recipe.getIngredients();
        this.directions = recipe.getDirections();
        this.category = recipe.getCategory();
        this.date = recipe.getDate();
    }
}
