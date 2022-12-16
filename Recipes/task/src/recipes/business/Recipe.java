package recipes.business;


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

    //Various recipes can belong to one user
    @ManyToOne
    //Only one user per recipe can be provided
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private User user;

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
