package nyp;

import java.io.Serializable;

class Ticket implements Serializable {
	private static final long serialVersionUID = 1L;
    private String ticketID;
    private Reservation reservation;
    private double price;
    private Baggage baggageAllowance;

    public Ticket(String ticketID, Reservation reservation, Baggage baggage) {
        this.ticketID = ticketID;
        this.reservation = reservation;
        this.baggageAllowance = baggage;
        this.price = CalculatePrice.calculate(reservation.getSeat(), baggage.getWeight());
    }

    @Override
    public String toString() {
        return "TICKET [" + ticketID + "] Price: " + price + "$ (Bag: " + baggageAllowance.getWeight() + "kg)";
    }
    public String getTicketID() { return ticketID; }
    public Reservation getReservation() { return reservation; }
    public double getPrice() { return price; }
    public Baggage getBaggageAllowance() { return baggageAllowance; }
}