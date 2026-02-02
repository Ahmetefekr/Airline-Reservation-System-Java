package nyp;

import java.util.*;
import java.io.*;


public class ReservationManager {
    private List<Ticket> tickets; 
    private final String FILE_NAME = "tickets.dat";

    public ReservationManager() {
        this.tickets = loadTickets();
    }

    public synchronized Ticket makeReservation(Passenger p, Flight f, Seat s, double baggageWeight) {
        if (s.getReserveStatus()) {
            return null;
        }

        s.setReserveStatus(true);
        String resCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Reservation newRes = new Reservation(resCode, f, p, s);

        Baggage newBag = new Baggage(baggageWeight);

        String ticketID = "T-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Ticket newTicket = new Ticket(ticketID, newRes, newBag); 
        
        tickets.add(newTicket);
        saveTickets();

        System.out.println("Bilet: " + ticketID + " Fiyat: " + newTicket.getPrice());
        return newTicket;
    }

    public void cancelTicket(Ticket t) {
        if (t != null) {
            t.getReservation().getSeat().setReserveStatus(false);
            
            tickets.remove(t);
            saveTickets();
            
            System.out.println("Bilet iptal edildi: " + t.getTicketID());
        }
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    private void saveTickets() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tickets);
        } catch (IOException e) {
            System.err.println("Biletler kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Ticket> loadTickets() {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Ticket>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Biletler yüklenirken hata oluştu veya dosya formatı değişti.");
            return new ArrayList<>();
        }
    }
    public void cancelTicketsByFlight(String flightNum) {
        List<Ticket> toRemove = new ArrayList<>();
        
        for (Ticket t : tickets) {
            if (t.getReservation().getFlight().getFlightNum().equals(flightNum)) {
                toRemove.add(t);
            }
        }

        if (!toRemove.isEmpty()) {
            tickets.removeAll(toRemove);
            saveTickets();
            System.out.println(flightNum + " uçuşuna ait " + toRemove.size() + " bilet iptal edildi.");
        }
    }
}