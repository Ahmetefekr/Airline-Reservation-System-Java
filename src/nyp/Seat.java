package nyp;

import java.io.Serializable;

public class Seat implements Serializable {
	private static final long serialVersionUID = 1L;
    private String seatNum;
    private SeatType seatClass;
    private double price;
    private boolean reserveStatus;

    public Seat(String seatNum, SeatType seatClass) {
        this.seatNum = seatNum;
        this.seatClass = seatClass;
        this.reserveStatus = false;

        if (seatClass == SeatType.BUSINESS) {
            this.price = 1000.0;
        } else {
            this.price = 500.0;
        }
    }
    
    public void reserve() {
        this.reserveStatus = true;
    }
    
    public void cancel() {
        this.reserveStatus = false;
    }

    public void setReserveStatus(boolean status) { this.reserveStatus = status; }
    public boolean getReserveStatus() { return reserveStatus; }
    public SeatType getSeatClass() { return seatClass; }
    public double getPrice() { return price; }
    public String getSeatNum() { return seatNum; }
    
    @Override
    public String toString() {
        return seatNum + " (" + seatClass + ")";
    }
}