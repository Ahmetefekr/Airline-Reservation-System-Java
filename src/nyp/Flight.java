package nyp;

import java.io.Serializable;
import java.time.*;

class Flight implements Serializable {
	private static final long serialVersionUID = 1L;
    private String flightNum;
    private String departurePlace;
    private String arrivalPlace;
    private LocalDate date;
    private LocalTime hour;
    private int duration;
    private Plane assignedPlane;
    private Route route; 

    public Flight(String flightNum, Route route, LocalDate date, LocalTime hour, int duration, Plane plane) {
        this.flightNum = flightNum;
        this.route = route;
        this.departurePlace = route.getDepartureCity();
        this.arrivalPlace = route.getArrivalCity();
        this.date = date;
        this.hour = hour;
        this.duration = duration;
        this.assignedPlane = plane;
    }

    public String getFlightNum() { return flightNum; }
    public Plane getAssignedPlane() { return assignedPlane; }
    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setHour(LocalTime hour) { this.hour = hour; }
    public void setDuration(int duration) { this.duration = duration; }
    public int getDuration() { return this.duration; }
    public void setDeparturePlace(String dep) { this.departurePlace = dep; }
    public void setArrivalPlace(String arr) { this.arrivalPlace = arr; }
    public LocalDate getDate() { return date; }
    public LocalTime getHour() { return hour; }
    public Plane getPlane() {return assignedPlane; }
    
    @Override
    public String toString() {
        return flightNum + " | " + departurePlace + " -> " + arrivalPlace + " | " + date;
    }
}