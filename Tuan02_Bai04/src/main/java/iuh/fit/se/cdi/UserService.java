package iuh.fit.se.cdi;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {
    public User getSampleUser() {
        return new User(1, "Le", "Nguyen Sinh");
    }
}
