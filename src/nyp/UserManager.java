package nyp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserManager {
    private List<User> users;
    private final String FILE_NAME = "users.dat";

    public UserManager() {
        this.users = loadUsers();
        if (users.isEmpty()) {
            users.add(new User("admin", "123", UserRole.ADMIN));
            saveUsers();
        }
    }
    
    public void addUser(User u) {
        users.add(u);
        saveUsers();
    }

    public void deleteUser(String username) {
        users.removeIf(u -> u.getUsername().equals(username));
        saveUsers();
    }

    public void updateUser(String oldUsername, String newUsername, String newPassword, UserRole role) {
        for (User u : users) {
            if (u.getUsername().equals(oldUsername)) {
                users.remove(u);
                users.add(new User(newUsername, newPassword, role));
                saveUsers();
                return;
            }
        }
    }

    public List<User> getStaffList() {
        return users.stream()
                .filter(u -> u.getRole() == UserRole.ADMIN)
                .collect(Collectors.toList());
    }
    
    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<User> loadUsers() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<User>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String generateNextPassengerID() {
        int maxId = 0;
        for (User u : users) {
            if (u instanceof Passenger) {
                try {
                    int currentId = Integer.parseInt(((Passenger) u).getPassengerID());
                    if (currentId > maxId) {
                        maxId = currentId;
                    }
                } catch (NumberFormatException e) {
                	
                }
            }
        }
        return String.valueOf(maxId + 1);
    }

    public boolean registerPassenger(String name, String surname, String contact, String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        String newID = generateNextPassengerID();
        
        Passenger newPassenger = new Passenger(newID, name, surname, contact, username, password);

        newPassenger.setFullName(name, surname); 

        users.add(newPassenger);
        saveUsers(); 
        return true;
    }
}