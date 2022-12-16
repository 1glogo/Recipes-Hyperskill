package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@org.springframework.stereotype.Controller
public class RecipeController {
    List<Recipe> allRecipes = new ArrayList<>();
    //setting up repository
    @Autowired
    IRecipeRepository recipeRepository;

    @Autowired
    IUserRepository userRepo;

    @Autowired
    public RecipeController(IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    //Primary Methods used in the API's below
    public Recipe save(Recipe toSave) {
        return recipeRepository.save(toSave);
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

    public List<RecipeClient> updateArrayToClientVersion (List<Recipe> list) {
        List<RecipeClient> updatedList = new ArrayList<>();
        for (Recipe i : list) {
            updatedList.add(new RecipeClient(i));
        }
        Collections.reverse(updatedList);
        return updatedList;
    }

    @PostMapping ("/api/recipe/new")
    public ResponseEntity<RecipeId> recipePostNew(@Valid @RequestBody Recipe newRecipe, @AuthenticationPrincipal UserDetails details) {
        //find relevant user
        String userEmail = details.getUsername();
        System.out.println("API email: "+userEmail);
        //get User object relevant
        User currentUser = userRepo.findByEmailIgnoreCase(userEmail);
        newRecipe.setUser(currentUser);
        //System.out.println("newRecipe: "+newRecipe.toString());
        save(newRecipe);
        /*currentUser.getRecipesAuthored().add(newRecipe);
        userRepo.save(currentUser);*/
        RecipeId response = new RecipeId(newRecipe.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping ("/api/recipe/{id}")
    public ResponseEntity<RecipeClient> recipeGetId (@PathVariable Long id){

        if(recipeRepository.existsById(id)) {
            //System.out.println(findRecipeByIdS(id).toString());
            return new ResponseEntity<>(new RecipeClient(findRecipeByIdS(id)), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping ("/api/recipe/{id}")
    public ResponseEntity recipeDeleteById (@PathVariable Long id, @AuthenticationPrincipal UserDetails details) {
        String userEmail = details.getUsername();
        User currentUser = userRepo.findByEmailIgnoreCase(userEmail);

        if(recipeRepository.existsById(id) && !currentUser.equals(findRecipeByIdS(id).getUser())){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }else if(recipeRepository.existsById(id) && currentUser.equals(findRecipeByIdS(id).getUser())) {
            recipeRepository.delete(findRecipeByIdS(id));
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping ("/api/recipe/{id}")
    public ResponseEntity recipePutById (@Valid @RequestBody Recipe newRecipe, @PathVariable Long id, @AuthenticationPrincipal UserDetails details) {
        String userEmail = details.getUsername();
        User currentUser = userRepo.findByEmailIgnoreCase(userEmail);

        if(recipeRepository.existsById(id) && !currentUser.equals(findRecipeByIdS(id).getUser())){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }else if(recipeRepository.existsById(id) && currentUser.equals(findRecipeByIdS(id).getUser())) {

            Recipe recipeUpdate = findRecipeByIdS(id);

            recipeUpdate.setCategory(newRecipe.getCategory());
            recipeUpdate.setIngredients(newRecipe.getIngredients());
            recipeUpdate.setDescription(newRecipe.getDescription());
            recipeUpdate.setName(newRecipe.getName());
            recipeUpdate.setDate(LocalDateTime.now());
            recipeUpdate.setDirections(newRecipe.getDirections());

            save(recipeUpdate);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping ("/api/recipe/search")
    public ResponseEntity<List<RecipeClient>> searchForRecipe (@RequestParam(required = false) String name, @RequestParam(required = false) String category) {
        System.out.println("name: " + name);
        System.out.println("category:" + category);
        List<Recipe> outputArray = new ArrayList<>();
        if (category == null && name == null || category != null && name != null) {
            outputArray= Collections.emptyList();
            return new ResponseEntity<>(updateArrayToClientVersion(outputArray), HttpStatus.BAD_REQUEST);
        }
        else if (category==null) {
            System.out.println("entered if for category == null");
            outputArray = findRecipeByNameS(name);
            return new ResponseEntity<>(updateArrayToClientVersion(outputArray), HttpStatus.OK);
        } else if (name == null) {
            System.out.println("entered if for name == null");
            outputArray = findRecipeByCategoryS(category);
            return new ResponseEntity<>(updateArrayToClientVersion(outputArray), HttpStatus.OK);
        } else {
            outputArray= Collections.emptyList();
            return new ResponseEntity<>(updateArrayToClientVersion(outputArray), HttpStatus.BAD_REQUEST);
        }
    }



}
