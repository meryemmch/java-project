package src.main.java.userManager;
import src.main.java.model.User;

    import java.util.List;

public interface UserOperations {
    void signup();
    boolean login();
    List<User> getAllUsers();
}


