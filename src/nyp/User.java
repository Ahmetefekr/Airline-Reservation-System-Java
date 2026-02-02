package nyp;
import java.io.*;

class User implements Serializable {
	private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private UserRole role;

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
}
