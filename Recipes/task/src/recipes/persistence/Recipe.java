package recipes.persistence;


import lombok.AllArgsConstructor;
import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@Entity
@Table (name="recipe")
public class Recipe {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String name;
    @Column
    @NotBlank
    private String description;
    @Column
    @NotNull
    @Size(min=1)
    private String[] ingredients;
    @Column
    @NotNull
    @Size(min=1)
    private String[] directions;

    @Column
    @NotBlank
    private String category;

    @Column
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private User user;



    public Recipe(Recipe newRecipe) {
        this.id = newRecipe.getId();
        this.name = newRecipe.getName();
        this.description= newRecipe.getDescription();
        this.ingredients = newRecipe.getIngredients();
        this.directions= newRecipe.getDirections();
        this.category=newRecipe.getCategory();
        this.date = LocalDateTime.now();
    }
    public Recipe () {
        this.date = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", directions=" + Arrays.toString(directions) +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", user=" + user +
                '}';
    }
}
