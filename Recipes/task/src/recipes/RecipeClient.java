package recipes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

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
