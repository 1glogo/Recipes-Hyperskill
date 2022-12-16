package recipes.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import recipes.persistence.IUserRepository;
import recipes.business.User;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {

    //Setting up repositories
    private final IUserRepository repo;
    private final PasswordEncoder encoder;
    public UserController(IUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    //JSON body in User class format to be provided by client to create database user.
    @PostMapping("/api/register")
    public HttpStatus register (@Valid @RequestBody User user) {
        //IF User does not exist by the email specified (checked against the database
        //set the password with the encoder and save accordingly
        if (!repo.existsByEmailIgnoreCase(user.getEmail())){
            user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    //user searched based on their ID.
    @GetMapping ("/api/user/{id}")
    public ResponseEntity<Optional<User>> getUserById (@PathVariable Long id) {
        if(repo.existsById(id)) {
            return new ResponseEntity<>(repo.findById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
