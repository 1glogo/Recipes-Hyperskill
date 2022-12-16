package recipes.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import recipes.persistence.User;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);

    User findByEmailIgnoreCase(String email);


    @Override
    Optional<User> findById(Long aLong);

    @Override
    boolean existsById(Long aLong);


}
