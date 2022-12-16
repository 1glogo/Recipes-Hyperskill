package recipes.business;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import recipes.business.Recipe;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    @NotBlank
    @Pattern(regexp = ".+@.+\\..+")
    private String email;

    @Column
    @NotBlank
    @Length(min=8)
    private String password;

    //JSON ignore provided to bew ignored in API's
    @JsonIgnore
    //One user can have various recipes
    @OneToMany(mappedBy = "user")
    private List<Recipe> recipesAuthored = new ArrayList<>();
}
