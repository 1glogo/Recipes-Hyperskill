package recipes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IUserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);

    User findByEmailIgnoreCase(String email);


    @Override
    Optional<User> findById(Long aLong);

    @Override
    boolean existsById(Long aLong);


}
