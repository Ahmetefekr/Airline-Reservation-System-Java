package nyp;

import java.io.Serializable;
import java.util.*;

class Plane implements Serializable {
	private static final long serialVersionUID = 1L;
    private String planeID;
    private String planeModel;
    private int capacity;
    private Map<String, Seat> seatMatrix; // "2D array or Map structure"

    public Plane(String planeID, String planeModel, int capacity) {
        this.planeID = planeID;
        this.planeModel = planeModel;
        this.capacity = capacity;
        this.seatMatrix = new HashMap<>();
    }

    public Map<String, Seat> getSeatMatrix() { return seatMatrix; }
    public String getPlaneID() { return planeID; }
    public int getCapacity() { return capacity; }
    public String getPlaneModel() { return planeModel; }
}