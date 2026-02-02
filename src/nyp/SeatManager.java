package nyp;

import java.util.*;

class SeatManager {
    private Plane plane;

    public SeatManager(Plane plane) {
        this.plane = plane;
        if (plane.getSeatMatrix().isEmpty()) {
            initializeSeats();
        }
    }

    private void initializeSeats() {
        String[] cols = {"A", "B", "C", "D", "E", "F"};

        int totalRows = plane.getCapacity() / 6; 

        for (int i = 1; i <= totalRows; i++) {
            for (String col : cols) {
                String sNum = i + col;
                
                SeatType type = (i <= 5) ? SeatType.BUSINESS : SeatType.ECONOMY;
                
                plane.getSeatMatrix().put(sNum, new Seat(sNum, type));
            }
        }
    }

    public boolean bookRandomSeat(boolean isSync) {
        List<String> keys = new ArrayList<>(plane.getSeatMatrix().keySet());
        java.util.Random r = new java.util.Random();
        
        String randomKey = keys.get(r.nextInt(keys.size()));
        Seat targetSeat = plane.getSeatMatrix().get(randomKey);

        if (isSync) {
            synchronized (plane) { 
                return processBooking(targetSeat);
            }
        } else {
            return processBooking(targetSeat);
        }
    }
    
    private boolean processBooking(Seat seat) {
        if (!seat.getReserveStatus()) {
            try { Thread.sleep(5); } catch (InterruptedException e) {}
            
            seat.setReserveStatus(true);
            return true;
        }
        return false;
    }

    public int getOccupiedCount() {
        return (int) plane.getSeatMatrix().values().stream().filter(Seat::getReserveStatus).count();
    }
    
    public void resetSeats() {
        for (Seat s : plane.getSeatMatrix().values()) {
            s.setReserveStatus(false);
        }
    }
}

