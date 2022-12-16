package recipes.business;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.persistence.IUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    //setting up repositories
    private final IUserRepository repo;
    public UserDetailsServiceImpl(IUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByEmailIgnoreCase(username);

        if(user==null){
            throw new UsernameNotFoundException("username/email: " + username);
        }
        return new UserDetailsImpl(user);
    }
}
