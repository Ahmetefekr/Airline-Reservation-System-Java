package nyp;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ReportGenerator implements Runnable {
    
    private FlightManager flightManager;
    private JLabel statusLabel;

    public ReportGenerator(FlightManager fm, JLabel label) {
        this.flightManager = fm;
        this.statusLabel = label;
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> statusLabel.setText("Status: Preparing report... (Please wait)"));

        try {
            for (int i = 0; i <= 100; i += 20) {
                Thread.sleep(500); 
            }
            
            int count = (flightManager != null) ? flightManager.getFlights().size() : 0;
            
            String res = "Report Finished. Total Active Flights: " + count;
            SwingUtilities.invokeLater(() -> statusLabel.setText(res));

        } catch (InterruptedException e) {
            SwingUtilities.invokeLater(() -> statusLabel.setText("Status: Error!"));
        }
    }
}