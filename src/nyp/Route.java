package nyp;

import java.io.Serializable;

class Route implements Serializable {
    private static final long serialVersionUID = 1L;
    private String departureCity;
    private String arrivalCity;
    private double distance;

    public Route(String departureCity, String arrivalCity, double distance) {
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.distance = distance;
    }

    public String getDepartureCity() { return departureCity; }
    public String getArrivalCity() { return arrivalCity; }
    public double getDistance() { return distance; }

    @Override
    public String toString() { return departureCity + " -> " + arrivalCity; }
}