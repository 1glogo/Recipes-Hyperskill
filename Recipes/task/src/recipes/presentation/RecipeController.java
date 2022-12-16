package recipes.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import recipes.persistence.IRecipeRepository;
import recipes.persistence.IUserRepository;
import recipes.business.Recipe;
import recipes.business.User;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@org.springframework.stereotype.Controller
public class RecipeController {
    //setting up repositories
    private final IRecipeRepository recipeRepository;
    private final IUserRepository userRepo;
    public RecipeController(IRecipeRepository recipeRepository, IUserRepository userRepo) {
        this.recipeRepository = recipeRepository;
        this.userRepo = userRepo;
    }

    //Primary Methods used in the API's below
    public void save(Recipe toSave) {
        recipeRepository.save(toSave);
    }
    public Recipe findRecipeByIdS (long id){
        return recipeRepository.findRecipeById(id);
    }
    public List<Recipe> findRecipeByNameS (String name) {
        return recipeRepository.findRecipeByNameContainingIgnoreCase(name);
    }
    private List<Recipe> findRecipeByCategoryS(String category) {
        return recipeRepository.findRecipeByCategoryIgnoreCase(category);
    }

    //Changing of all raw Recipe database data to the client format
    public List<RecipeClient> updateArrayToClientVersion (List<Recipe> list) {
        List<RecipeClient> updatedList = new ArrayList<>();
        for (Recipe i : list) {
            updatedList.add(new RecipeClient(i));
        }
        //reverse the list in order to show the old items first
        Collections.reverse(updatedList);
        return updatedList;
    }

    //Post new recipe
    // Recipe is posted in the body
    //authentication required in order to get the Recipe author ("user")
    @PostMapping ("/api/recipe/new")
    public ResponseEntity<RecipeId> recipePostNew(@Valid @RequestBody Recipe newRecipe,
                                                  @AuthenticationPrincipal UserDetails details) {
        //With the use of the username from the authentication
        //the recipe's author ("use") is set
        String userEmail = details.getUsername();
        User currentUser = userRepo.findByEmailIgnoreCase(userEmail);
        newRecipe.setUser(currentUser);

        //recipe is saved on the database
        save(newRecipe);

        //id is returned along with HttpStatus.OK
        RecipeId response = new RecipeId(newRecipe.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Get Response with recipe based on the url id.
    @GetMapping ("/api/recipe/{id}")
    public ResponseEntity<RecipeClient> recipeGetId (@PathVariable Long id){
        if(recipeRepository.existsById(id)) {
            return new ResponseEntity<>(new RecipeClient(findRecipeByIdS(id)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Delete response for recipe with id matching the patch (url) variable.
    //Only the author can delete or amend a recipe.
    @DeleteMapping ("/api/recipe/{id}")
    public HttpStatus recipeDeleteById (@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetails details) {
        //Getting user information
        String userEmail = details.getUsername();
        User currentUser = userRepo.findByEmailIgnoreCase(userEmail);

        //checking that the user is the author in order to delete
        if(recipeRepository.existsById(id) && !currentUser.equals(findRecipeByIdS(id).getUser())){
            return HttpStatus.FORBIDDEN;
        }else if(recipeRepository.existsById(id) && currentUser.equals(findRecipeByIdS(id).getUser())) {
            recipeRepository.delete(findRecipeByIdS(id));
            return HttpStatus.NO_CONTENT;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    //same with delete API above
    //Assuming all Recipe fields, except of time.
    //TIme is set to current time
    @PutMapping ("/api/recipe/{id}")
    public HttpStatus recipePutById (@Valid @RequestBody Recipe newRecipe,
                                         @PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails details) {
        //Getting user information
        String userEmail = details.getUsername();
        User currentUser = userRepo.findByEmailIgnoreCase(userEmail);

        //checking that the user is the author in order to update ("put")
        if(recipeRepository.existsById(id) && !currentUser.equals(findRecipeByIdS(id).getUser())){
            return HttpStatus.FORBIDDEN;
        }else if(recipeRepository.existsById(id) && currentUser.equals(findRecipeByIdS(id).getUser())) {
            //selecting current recipe to be updated
            Recipe recipeUpdate = findRecipeByIdS(id);

            //setting new properties
            recipeUpdate.setCategory(newRecipe.getCategory());
            recipeUpdate.setIngredients(newRecipe.getIngredients());
            recipeUpdate.setDescription(newRecipe.getDescription());
            recipeUpdate.setName(newRecipe.getName());
            recipeUpdate.setDate(LocalDateTime.now());
            recipeUpdate.setDirections(newRecipe.getDirections());

            //saving back to the database
            save(recipeUpdate);
            return HttpStatus.NO_CONTENT;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    //Either a name or category parameter are provided in order to search recipe in the database
    @GetMapping ("/api/recipe/search")
    public ResponseEntity<List<RecipeClient>> searchForRecipe (@RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String category){
        List<Recipe> outputArray;
        //Ensure that category or name are specified. Not both at the same time
        //Empty list is provided along with BAD_Request
        if (category == null && name == null || category != null && name != null) {
            outputArray= Collections.emptyList();
            return new ResponseEntity<>(updateArrayToClientVersion(outputArray), HttpStatus.BAD_REQUEST);
        }
        else if (category==null) {
            outputArray = findRecipeByNameS(name);
            return new ResponseEntity<>(updateArrayToClientVersion(outputArray), HttpStatus.OK);
        }
        else if (name==null) {
            outputArray = findRecipeByCategoryS(category);
            return new ResponseEntity<>(updateArrayToClientVersion(outputArray), HttpStatus.OK);
        } else {
            outputArray= Collections.emptyList();
            return new ResponseEntity<>(updateArrayToClientVersion(outputArray), HttpStatus.BAD_REQUEST);
        }
    }

}
