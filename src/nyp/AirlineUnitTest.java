package nyp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

class AirlineUnitTests {

    private FlightManager flightManager;
    private SeatManager seatManager;
    private Plane plane;
    private Flight flight;

    @BeforeEach
    void setUp() {
        flightManager = new FlightManager();
        plane = new Plane("P1", "Boeing 737", 180);
        
        Route route = new Route("Istanbul", "Ankara", 500);
        flight = new Flight("TK101", route, LocalDate.now().plusDays(1), LocalTime.of(10, 0), 60, plane);
        
        flightManager.addFlight(flight);
        seatManager = new SeatManager(plane);
    }

    // =========================================================================
    // PriceCalculation Test 
    // =========================================================================
    
    @Test
    void testBusinessClassPriceCalculation() {

        Seat businessSeat = new Seat("1A", SeatType.BUSINESS);
        double baggageWeight = 25.0;
        
        double calculatedPrice = CalculatePrice.calculate(businessSeat, baggageWeight);
        
        assertEquals(1050.0, calculatedPrice, "Business fiyatı yanlış hesaplandı!");
    }

    @Test
    void testEconomyClassPriceCalculation() {
        
        Seat economySeat = new Seat("10A", SeatType.ECONOMY);
        double baggageWeight = 10.0;
        
        double calculatedPrice = CalculatePrice.calculate(economySeat, baggageWeight);
        
        assertEquals(500.0, calculatedPrice, "Economy fiyatı yanlış hesaplandı!");
    }

    // =========================================================================
    // FlightSearchEngine Test 
    // =========================================================================

    @Test
    void testSearchFlightsByRoute() {
        
        List<Flight> results = flightManager.getFlights().stream()
            .filter(f -> f.getRoute().getDepartureCity().equalsIgnoreCase("Istanbul") &&
                         f.getRoute().getArrivalCity().equalsIgnoreCase("Ankara"))
            .collect(Collectors.toList());

        assertFalse(results.isEmpty(), "Istanbul-Ankara uçuşu bulunmalıydı!");
        assertEquals("TK101", results.get(0).getFlightNum(), "Yanlış uçuş bulundu!");
    }

    @Test
    void testEliminatePastFlights() {
        
        Flight pastFlight = new Flight("OLD001", new Route("A", "B", 100), 
                                       LocalDate.now().minusDays(5), LocalTime.of(10,0), 120, plane);
        flightManager.addFlight(pastFlight);

        List<Flight> activeFlights = flightManager.getFlights().stream()
            .filter(f -> !f.getDate().isBefore(LocalDate.now()))
            .collect(Collectors.toList());

        boolean containsOld = activeFlights.stream().anyMatch(f -> f.getFlightNum().equals("OLD001"));
        
        assertFalse(containsOld, "Geçmiş tarihli uçuş listede olmamalı!");
    }

    // =========================================================================
    // 3. SeatManager Test
    // =========================================================================

    @Test
    void testEmptySeatsCountDecreases() {
        
        int totalSeats = plane.getCapacity();
        int initialOccupied = seatManager.getOccupiedCount();
        int initialEmpty = totalSeats - initialOccupied;

        Seat seat = plane.getSeatMatrix().get("1A");
        seat.setReserveStatus(true);

        int newOccupied = seatManager.getOccupiedCount();
        int newEmpty = totalSeats - newOccupied;

        assertEquals(initialEmpty - 1, newEmpty, "Boş koltuk sayısı 1 azalmalıydı!");
    }

    @Test
    void testInvalidSeatException() {
    
        assertThrows(NullPointerException.class, () -> {
            Seat invalidSeat = plane.getSeatMatrix().get("99Z");
            invalidSeat.setReserveStatus(true);
        }, "Olmayan koltukta işlem yapılırken Exception fırlatılmalı!");
    }
}