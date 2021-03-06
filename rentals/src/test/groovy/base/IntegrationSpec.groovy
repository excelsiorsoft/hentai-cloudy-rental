package base
import eu.solidcraft.hentai.RentalApplication
import eu.solidcraft.hentai.infrastructure.Profiles
import eu.solidcraft.hentai.rentals.dtos.Film
import eu.solidcraft.hentai.users.User
import eu.solidcraft.hentai.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.RememberMeAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

@Rollback
@Transactional
@SpringApplicationConfiguration(classes = RentalApplication)
@ActiveProfiles(Profiles.TEST)
@Ignore
class IntegrationSpec extends Specification {
    protected static final String username = "test"
    protected static final String password = "test"

    @Autowired
    private AuthenticationManager authenticationManager

    @Autowired
    private UserRepository userRepository

    @Shared protected ArrayList<Film> persistedFilms = []

    void setup() {
        moviesArePresent()
        loginUser()
    }

    protected moviesArePresent() {
        persistedFilms = TestData.films
    }

    private void loginUser() {
        User user = new User(username, password);
        userRepository.save(user)
        UserDetails userDetails = userRepository.loadUserByUsername(username)
        RememberMeAuthenticationToken rememberMeAuthenticationToken = new RememberMeAuthenticationToken("key", userDetails, null);
        rememberMeAuthenticationToken.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(rememberMeAuthenticationToken);
    }
}
