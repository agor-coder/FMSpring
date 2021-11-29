package pl.gorzki.fmspring.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.gorzki.fmspring.users.db.UserJpaRepository;


@AllArgsConstructor
public class FmUserDetailsService implements UserDetailsService {

    private final UserJpaRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return repository.findByEmailUserNameIgnoreCase(userName)
                .map(UserEntityDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
    }
}
