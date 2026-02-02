package nyp;

import java.io.*;

class Passenger extends User implements Serializable {
	private static final long serialVersionUID = 1L;
    private String passengerID;
    private String name;
    private String surname;
    private String contactinfo;

    public Passenger(String passengerID, String name, String surname, String contactInfo, String username, String password) {
        super(username, password, UserRole.PASSENGERS);
        this.passengerID = passengerID;
        this.name = name;
        this.surname = surname;
        this.contactinfo = contactInfo;
    }
    public void setFullName(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getPassengerID() { return passengerID; }
    public String getContactInfo() { return contactinfo; }
    
    public String getFullName() { return name + " " + surname; }
}
