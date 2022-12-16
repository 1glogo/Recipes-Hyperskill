package recipes.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import recipes.persistence.IUserRepository;
import recipes.persistence.User;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    IUserRepository repo;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/api/register")
    public ResponseEntity register (@Valid @RequestBody User user) {

        if (!repo.existsByEmailIgnoreCase(user.getEmail())){
            user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("/api/user/{id}")
    public ResponseEntity<Optional<User>> getUser (@PathVariable Long id) {
        if(repo.existsById(id)) {
            return new ResponseEntity<>(repo.findById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
