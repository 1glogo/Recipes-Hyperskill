package recipes.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Simple id class to convert Recipe into id in accordance with client requirements
public class RecipeId {
    long id;
}
