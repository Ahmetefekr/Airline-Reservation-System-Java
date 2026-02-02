package nyp;

import java.io.*;
import java.util.*;
import java.time.*;

public class FlightManager {
    private List<Flight> flights;
    private final String FILE_NAME = "flights.dat";

    public FlightManager() {
        this.flights = loadFlights();
    }

    public boolean addFlight(Flight newFlight) {
        for (Flight f : flights) {
            if (f.getFlightNum().equalsIgnoreCase(newFlight.getFlightNum())) {
                System.out.println("Duplicate Flight Detected: " + newFlight.getFlightNum());
                return false;
            }
        }
        flights.add(newFlight);
        saveFlights();
        return true;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void deleteFlight(String flightNum) {
        boolean removed = flights.removeIf(f -> f.getFlightNum().equals(flightNum));
        
        if (removed) {
            saveFlights();
            System.out.println(flightNum + " numaralı uçuş silindi.");
        } else {
            System.out.println("Silinecek uçuş bulunamadı: " + flightNum);
        }
    }

    public void updateFlightTime(String flightNum, LocalDate newDate, LocalTime newHour) {
        boolean found = false;
        for (Flight f : flights) {
            if (f.getFlightNum().equals(flightNum)) {
                f.setDate(newDate);
                f.setHour(newHour);
                found = true;
                break;
            }
        }
        
        if (found) {
            saveFlights();
            System.out.println(flightNum + " uçuşunun saati güncellendi.");
        } else {
            System.out.println("Güncellenecek uçuş bulunamadı.");
        }
    }
    
    private void saveFlights() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(flights);
        } catch (IOException e) {
            System.err.println("Dosya kaydetme hatası: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Flight> loadFlights() {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Flight>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Dosya okuma hatası veya sınıf uyumsuzluğu.");
            return new ArrayList<>();
        }
    }
    public void updateFlightFull(String flightNum, Route newRoute, LocalDate newDate, LocalTime newTime, int newDuration) {
        boolean found = false;
        for (Flight f : flights) {
            if (f.getFlightNum().equals(flightNum)) {
                f.setRoute(newRoute);
                f.setDeparturePlace(newRoute.getDepartureCity());
                f.setArrivalPlace(newRoute.getArrivalCity());
                f.setDate(newDate);
                f.setHour(newTime);
                f.setDuration(newDuration); // YENİ EKLENDİ
                
                found = true;
                break;
            }
        }
        
        if (found) {
            saveFlights();
            System.out.println(flightNum + " güncellendi.");
        } else {
            System.out.println("Güncellenecek uçuş bulunamadı.");
        }
    }
    public void saveAllFlights() {
        saveFlights();
    }
}