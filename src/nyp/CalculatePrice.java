package nyp;

class CalculatePrice {
    // PDF Gereksinimi: Business logic for calculating ticket prices
    
    public static double calculate(Seat seat, double baggageWeight) {
        double basePrice = seat.getPrice(); 


        double freeAllowance = (seat.getSeatClass() == SeatType.BUSINESS) ? 20.0 : 15.0;
        
        double extraBaggageCost = 0.0;
        
        if (baggageWeight > freeAllowance) {
            extraBaggageCost = (baggageWeight - freeAllowance) * 10.0;
        }

        return basePrice + extraBaggageCost;
    }
}