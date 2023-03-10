package recipes.persistence;
import org.springframework.data.jpa.repository.JpaRepository;
import recipes.business.User;

import java.util.Optional;

//relevant queries have been created or overridden using JPA for USERS
public interface IUserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);
    User findByEmailIgnoreCase(String email);

    @Override
    Optional<User> findById(Long aLong);

    @Override
    boolean existsById(Long aLong);
}
