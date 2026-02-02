package nyp;

import java.io.Serializable;

class Baggage implements Serializable {
	private static final long serialVersionUID = 1L;
    private double weight;

    public Baggage(double weight) {
        this.weight = weight;
    }
    public double getWeight() { return weight; }
}