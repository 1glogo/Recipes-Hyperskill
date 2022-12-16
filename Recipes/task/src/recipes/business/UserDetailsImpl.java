package recipes.business;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import recipes.persistence.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> rolesAndAuthorities;

    public UserDetailsImpl(String username, String password, List<GrantedAuthority> rolesAndAuthorities) {
        this.username = username;
        this.password = password;
        this.rolesAndAuthorities = rolesAndAuthorities;
    }

    public UserDetailsImpl(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
       // int length = user.getRecipesAuthored().size();
        List<String> recipesAuthoredList= new ArrayList<>();
/*        for (int i = 0; i < length;i++){
            recipesAuthoredList.add(user.getRecipesAuthored().get(i).getId().toString());
        }*/
        this.rolesAndAuthorities = List.of(new SimpleGrantedAuthority(recipesAuthoredList.toString()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //UNUSED METHODS BELOW
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
