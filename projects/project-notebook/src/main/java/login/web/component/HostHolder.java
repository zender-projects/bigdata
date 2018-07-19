package login.web.component;

import login.model.User;
import org.springframework.stereotype.Component;

/**
 * Share user information in the thread of request.
 *
 * @author mac
 * */
@Component
public class HostHolder {

    // container for sharing user information
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    /**
     * Get current user information.
     * @return User
     * */
    public User getCurrentUser() {
        return userHolder.get();
    }

    /**
     * Set current user information.
     * @param user
     * */
    public void setCurrentUser(User user) {
        userHolder.set(user);
    }

    /**
     * Clear user information in current thread
     * */
    public void clear() {
        userHolder.remove();
    }

}
