package nyp;

import java.io.Serializable;
import java.time.*;

class Reservation implements Serializable {
	private static final long serialVersionUID = 1L;
    private String reservationCode;
    private Flight flight;
    private Passenger passenger;
    private Seat seat;
    private LocalDateTime dateOfReservation;
    private boolean active = true;

    public Reservation(String reservationCode, Flight flight, Passenger passenger, Seat seat) {
        this.reservationCode = reservationCode;
        this.flight = flight;
        this.passenger = passenger;
        this.seat = seat;
        this.dateOfReservation = LocalDateTime.now();
    }
    
    public Passenger getPassenger() { return passenger; }
    public Flight getFlight() { return flight; }
    public Seat getSeat() { return seat; }
    public boolean isActive() { return active; }
    public void cancel() { this.active = false; }
    public String getReservationCode() { return reservationCode; }
    public LocalDateTime getDateOfReservation() { return dateOfReservation; }
}