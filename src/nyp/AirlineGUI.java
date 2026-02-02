package nyp;

/* AHMET EFE KARAHAN
   YİĞİT AYTÜRK   */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class AirlineGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

    // --- YÖNETİCİLER (MANAGERS) ---
    private FlightManager flightManager;
    private ReservationManager reservationManager;
    private UserManager userManager;
    
    // --- O ANKİ OTURUM ---
    private Passenger currentPassenger;
    
    // --- GUI BİLEŞENLERİ ---
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public AirlineGUI() {
    	
        flightManager = new FlightManager();
        reservationManager = new ReservationManager();
        userManager = new UserManager();

        setTitle("Airline Management System - Group Project");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createAdminPanel(), "ADMIN");
        mainPanel.add(createPassengerPanel(), "PASSENGER");

        add(mainPanel);
    }
    
    // =========================================================================
    // LOGIN EKRANI
    // =========================================================================

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLbl = new JLabel("AIRLINE RESERVATION SYSTEM");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 24));
        
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register New Passenger"); // YENİ BUTON

        loginBtn.addActionListener(e -> {
            String uName = userField.getText();
            String pass = new String(passField.getPassword());
            User user = userManager.authenticate(uName, pass);

            if (user != null) {
                if (user.getRole() == UserRole.ADMIN) {
                    cardLayout.show(mainPanel, "ADMIN");
                } else {
                    if (user instanceof Passenger) {
                        currentPassenger = (Passenger) user;
                    } else {
                    	currentPassenger = new Passenger("TEMP", uName, "User", "N/A", uName, "123");
                    }
                    cardLayout.show(mainPanel, "PASSENGER");
                }
                userField.setText(""); passField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }
        });

        // REGISTER BUTONU
        registerBtn.addActionListener(e -> openRegisterDialog());

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLbl, gbc);
        
        gbc.gridwidth = 1; gbc.gridy = 1; 
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; 
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passField, gbc);

        gbc.gridx = 1; gbc.gridy = 3; 
        panel.add(loginBtn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        return panel;
    }

    // =========================================================================
    // ADMIN EKRANI
    // =========================================================================
    private JComponent createAdminPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // --- TAB 1: UÇUŞ YÖNETİMİ ---
        JPanel flightPanel = new JPanel(new BorderLayout());
        
        String[] columns = {"Flight Num", "Departure", "Arrival", "Date", "Time", "Duration (Min)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        
        refreshFlightTable(model);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        JTextField fNumTxt = new JTextField();
        JTextField depTxt = new JTextField();
        JTextField arrTxt = new JTextField();
        JTextField dateTxt = new JTextField("2026-01-01"); 
        JTextField timeTxt = new JTextField("14:30");
        JTextField durTxt = new JTextField("120");
        String[] planeModels = {
                "Boeing 737 (180 Seat)", 
                "Airbus A320 (150 Seat)", 
                "Boeing 787 Dreamliner (300 Seat)"
            };
            JComboBox<String> planeSelector = new JComboBox<>(planeModels);
        
        JButton addBtn = new JButton("Add Flight");
        JButton delBtn = new JButton("Delete Selected");
        JButton updateBtn = new JButton("Update Selected");
        JButton logoutBtn = new JButton("Logout");

        // TABLO SEÇİMİ
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    Object idObj = model.getValueAt(row, 0);
                    fNumTxt.setText(idObj != null ? idObj.toString() : "");

                    Object depObj = model.getValueAt(row, 1);
                    depTxt.setText(depObj != null ? depObj.toString() : "");
                    
                    Object arrObj = model.getValueAt(row, 2);
                    arrTxt.setText(arrObj != null ? arrObj.toString() : "");

                    Object dateObj = model.getValueAt(row, 3);
                    dateTxt.setText(dateObj != null ? dateObj.toString() : "");

                    Object timeObj = model.getValueAt(row, 4);
                    timeTxt.setText(timeObj != null ? timeObj.toString() : "");

                    Object durObj = model.getValueAt(row, 5);
                    durTxt.setText(durObj != null ? durObj.toString() : "120");
                    
                    fNumTxt.setEditable(false); 
                } else {
                    fNumTxt.setEditable(true);
                }
            }
        });

        // EKLEME (ADD)
        addBtn.addActionListener(e -> {
            try {
                java.time.LocalDate date = java.time.LocalDate.parse(dateTxt.getText());
                java.time.LocalTime time = java.time.LocalTime.parse(timeTxt.getText());
                
                if (java.time.LocalDateTime.of(date, time).isBefore(java.time.LocalDateTime.now())) {
                     JOptionPane.showMessageDialog(this, "Cannot add flight in the past!", "Date Error", JOptionPane.WARNING_MESSAGE);
                     return;
                }

                Route route = new Route(depTxt.getText(), arrTxt.getText(), 1000.0);
                
                String selectedPlane = (String) planeSelector.getSelectedItem();
                String modelName = "Boeing 737"; 
                int capacity = 180;
                
                if (selectedPlane.contains("A320")) { modelName = "Airbus A320"; capacity = 150; }
                else if (selectedPlane.contains("787")) { modelName = "Boeing 787"; capacity = 300; }

                Plane plane = new Plane("P" + System.currentTimeMillis(), modelName, capacity);
                int duration = Integer.parseInt(durTxt.getText().trim());
                
                Flight f = new Flight(
                    fNumTxt.getText().trim().toUpperCase(),
                    route, 
                    date, 
                    time, 
                    duration, 
                    plane
                );
                
                boolean isAdded = flightManager.addFlight(f);
                
                if (isAdded) {
                    refreshFlightTable(model);
                    JOptionPane.showMessageDialog(this, "Flight Added Successfully!");
                    fNumTxt.setText(""); depTxt.setText(""); arrTxt.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Error: Flight Number '" + f.getFlightNum() + "' already exists!\n(Bu uçuş numarası zaten kullanılıyor)", 
                        "Duplicate Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Check inputs! " + ex.getMessage());
            }
        });

        // GÜNCELLEME (UPDATE)
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                try {
                    String fNum = fNumTxt.getText();
                    Route newRoute = new Route(depTxt.getText(), arrTxt.getText(), 1000.0);
                    LocalDate newDate = LocalDate.parse(dateTxt.getText());
                    LocalTime newTime = LocalTime.parse(timeTxt.getText());
                    int newDuration = Integer.parseInt(durTxt.getText().trim()); // YENİ
                    
                    flightManager.updateFlightFull(fNum, newRoute, newDate, newTime, newDuration);
                    
                    refreshFlightTable(model);
                    JOptionPane.showMessageDialog(this, "Flight Updated Successfully!");
                    
                    table.clearSelection();
                    fNumTxt.setEditable(true);
                    fNumTxt.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: Check inputs!");
                }
            }
        });

        // SİLME (DELETE)
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            
            if (row != -1) {
                String flightNum = (String) model.getValueAt(row, 0);

                String message = "DİKKAT!\n" +
                                 "Bu uçuşu silerseniz, bu uçuşa ait satılmış TÜM BİLETLER İPTAL EDİLECEKTİR!\n" +
                                 "Yolcular mağdur olabilir. Devam etmek istiyor musunuz?";
                
                int confirm = JOptionPane.showConfirmDialog(
                        this, 
                        message, 
                        "Kritik İşlem Uyarısı", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    
                    reservationManager.cancelTicketsByFlight(flightNum);

                    Flight targetFlight = null;
                    for (Flight f : flightManager.getFlights()) {
                        if (f.getFlightNum().equals(flightNum)) {
                            targetFlight = f;
                            break;
                        }
                    }
                    
                    if (targetFlight != null) {
                        flightManager.deleteFlight(targetFlight.getFlightNum());
                        
                        refreshFlightTable(model);
                        JOptionPane.showMessageDialog(this, "Uçuş ve ilgili tüm biletler başarıyla silindi.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lütfen silinecek bir uçuş seçin.");
            }
        });

        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        formPanel.add(new JLabel("Flight Num (ID):")); formPanel.add(fNumTxt);
        formPanel.add(new JLabel("Departure:")); formPanel.add(depTxt);
        formPanel.add(new JLabel("Arrival:")); formPanel.add(arrTxt);
        formPanel.add(new JLabel("Date (YYYY-MM-DD):")); formPanel.add(dateTxt);
        formPanel.add(new JLabel("Time (HH:MM):")); formPanel.add(timeTxt);
        formPanel.add(new JLabel("Duration (min):")); formPanel.add(durTxt);
        formPanel.add(new JLabel("Aircraft Model:")); formPanel.add(planeSelector);
        formPanel.add(addBtn); formPanel.add(updateBtn);
        
        JPanel bottomControls = new JPanel(new BorderLayout());
        bottomControls.add(formPanel, BorderLayout.CENTER);
        bottomControls.add(delBtn, BorderLayout.EAST);

        flightPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        flightPanel.add(bottomControls, BorderLayout.SOUTH);
        flightPanel.add(logoutBtn, BorderLayout.NORTH);
        
        // Diğer Tablar
        JPanel reportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reportPanel.setBorder(BorderFactory.createTitledBorder("Scenario 2: Async Report"));
        
        JButton reportBtn = new JButton("Generate Report");
        JLabel reportLbl = new JLabel("Status: Ready");
        
        reportBtn.addActionListener(e -> {
            ReportGenerator task = new ReportGenerator(flightManager, reportLbl);
            new Thread(task).start();
        });
        
        reportPanel.add(reportBtn);
        reportPanel.add(reportLbl);
        
        JPanel staffPanel = createStaffPanel();

        tabbedPane.add("Flight Management", flightPanel);
        tabbedPane.add("Staff Management", staffPanel);
        tabbedPane.add("Reports (Scenario 2)", reportPanel);
        tabbedPane.add("Scenario 1: Simulation", createSimulationPanel());

        return tabbedPane;
    }    
    // =========================================================================
    // STAFF PANEL
    // =========================================================================
    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tablo Modeli
        String[] cols = {"Username", "Password (Hidden)", "Role"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        
        Runnable refreshTable = () -> {
            model.setRowCount(0);
            for (User u : userManager.getStaffList()) {
                model.addRow(new Object[]{u.getUsername(), "***", u.getRole()});
            }
        };
        refreshTable.run();

        // Form Alanları
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField userTxt = new JTextField();
        JTextField passTxt = new JTextField();
        JComboBox<UserRole> roleCombo = new JComboBox<>(UserRole.values());
        
        JButton addBtn = new JButton("Add Staff");
        JButton delBtn = new JButton("Delete Selected");
        JButton updateBtn = new JButton("Update Selected");

        // EKLEME İŞLEMİ
        addBtn.addActionListener(e -> {
            String uName = userTxt.getText();
            String pass = passTxt.getText();
            UserRole role = (UserRole) roleCombo.getSelectedItem();
            
            if(!uName.isEmpty() && !pass.isEmpty()) {
                userManager.addUser(new User(uName, pass, role));
                refreshTable.run();
                JOptionPane.showMessageDialog(this, "Staff Added!");
            }
        });

        // SİLME İŞLEMİ
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String uName = (String) model.getValueAt(row, 0);
                if (uName.equals("admin")) {
                    JOptionPane.showMessageDialog(this, "Cannot delete Main Admin!");
                } else {
                    userManager.deleteUser(uName);
                    refreshTable.run();
                }
            }
        });

        // GÜNCELLEME İŞLEMİ
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String oldName = (String) model.getValueAt(row, 0);
                String newName = userTxt.getText();
                String newPass = passTxt.getText();
                UserRole newRole = (UserRole) roleCombo.getSelectedItem();
                
                if(!newName.isEmpty() && !newPass.isEmpty()) {
                    userManager.updateUser(oldName, newName, newPass, newRole);
                    refreshTable.run();
                    JOptionPane.showMessageDialog(this, "Staff Updated!");
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill fields to update.");
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                userTxt.setText((String) model.getValueAt(row, 0));
            }
        });

        formPanel.add(new JLabel("Username:")); formPanel.add(userTxt);
        formPanel.add(new JLabel("New Password:")); formPanel.add(passTxt);
        formPanel.add(new JLabel("Role:")); formPanel.add(roleCombo);
        formPanel.add(addBtn); formPanel.add(updateBtn);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(delBtn, BorderLayout.EAST);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }
    
    // ===========================================================================
    // PASSENGER PANEL
    // ===========================================================================

    private JComponent createPassengerPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel searchPanel = new JPanel(new BorderLayout());
        
        JPanel topContainer = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        
        JTextField searchDep = new JTextField(10);
        JTextField searchArr = new JTextField(10);
        JButton searchBtn = new JButton("Search Flights");
        JButton showAllBtn = new JButton("Refresh All");
        
        JButton logoutBtn = new JButton("Logout");
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutBtn);
        
        // Tablo Ayarları
        String[] col = {"Num", "Departure", "Arrival", "Date", "Time", "Duration"};
        DefaultTableModel searchModel = new DefaultTableModel(col, 0);
        JTable searchTable = new JTable(searchModel);

        Runnable loadFlights = () -> {
            searchModel.setRowCount(0);
            
            String depFilter = searchDep.getText().trim();
            String arrFilter = searchArr.getText().trim();
            
            java.time.LocalDateTime now = java.time.LocalDateTime.now(); 

            for (Flight f : flightManager.getFlights()) {

                java.time.LocalDateTime flightTime = java.time.LocalDateTime.of(f.getDate(), f.getHour());
                
                if (flightTime.isBefore(now)) {
                    continue; 
                }

                boolean matchDep = depFilter.isEmpty() || f.getRoute().getDepartureCity().equalsIgnoreCase(depFilter);
                boolean matchArr = arrFilter.isEmpty() || f.getRoute().getArrivalCity().equalsIgnoreCase(arrFilter);

                if (matchDep && matchArr) {
                    searchModel.addRow(new Object[]{
                        f.getFlightNum(), 
                        f.getRoute().getDepartureCity(),
                        f.getRoute().getArrivalCity(),
                        f.getDate(),
                        f.getHour(),
                        f.getDuration(),
                    });
                }
            }
        };

        loadFlights.run();

        searchBtn.addActionListener(e -> loadFlights.run());

        showAllBtn.addActionListener(e -> {
            searchDep.setText("");
            searchArr.setText("");
            loadFlights.run();
        });

        // Logout
        logoutBtn.addActionListener(e -> {
             cardLayout.show(mainPanel, "LOGIN");
             searchDep.setText("");
             searchArr.setText("");
        });

        JButton bookBtn = new JButton("Book Selected Flight");
        bookBtn.addActionListener(e -> {
            int row = searchTable.getSelectedRow();
            if(row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a flight first!");
                return;
            }
            String fNum = (String) searchModel.getValueAt(row, 0);
            Flight selectedFlight = flightManager.getFlights().stream()
                .filter(f -> f.getFlightNum().equals(fNum)).findFirst().orElse(null);
            
            if(selectedFlight != null) {
                openSeatMapDialog(selectedFlight);
            }
        });

        // Panelleri Yerleştir
        filterPanel.add(new JLabel("From:")); filterPanel.add(searchDep);
        filterPanel.add(new JLabel("To:")); filterPanel.add(searchArr);
        filterPanel.add(searchBtn); filterPanel.add(showAllBtn);
        
        topContainer.add(logoutPanel, BorderLayout.NORTH);
        topContainer.add(filterPanel, BorderLayout.CENTER);

        searchPanel.add(topContainer, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchTable), BorderLayout.CENTER);
        searchPanel.add(bookBtn, BorderLayout.SOUTH);

        // ===========================================================================
        // --- TAB 2: MY RESERVATIONS ---
        // ===========================================================================
        JPanel myResPanel = new JPanel(new BorderLayout());
        String[] resCol = {"Res Code", "Flight", "Seat", "Status"};
        DefaultTableModel resModel = new DefaultTableModel(resCol, 0);
        JTable resTable = new JTable(resModel);
        
        JButton refreshResBtn = new JButton("Refresh My Reservations");
        JButton cancelResBtn = new JButton("Cancel Selected");
        
        refreshResBtn.addActionListener(e -> refreshReservationTable(resModel));

        cancelResBtn.addActionListener(e -> {
            int row = resTable.getSelectedRow();
            if (row != -1) {
                String ticketID = (String) resModel.getValueAt(row, 0);
                Ticket targetTicket = null;
                for(Ticket t : reservationManager.getTickets()) { 
                    if(t.getTicketID().equals(ticketID)) {
                        targetTicket = t;
                        break;
                    }
                }
                if (targetTicket != null) {
                    String flightID = targetTicket.getReservation().getFlight().getFlightNum();
                    String seatNum = targetTicket.getReservation().getSeat().getSeatNum();
                    
                    for (Flight realFlight : flightManager.getFlights()) {
                        if (realFlight.getFlightNum().equals(flightID)) {
                            Seat realSeat = realFlight.getPlane().getSeatMatrix().get(seatNum);
                            if (realSeat != null) realSeat.setReserveStatus(false);
                            break;
                        }
                    }
                    reservationManager.cancelTicket(targetTicket);
                    flightManager.saveAllFlights(); 
                    refreshReservationTable(resModel); 
                    JOptionPane.showMessageDialog(this, "Ticket Cancelled.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a ticket.");
            }
        });

        JPanel resControlPanel = new JPanel();
        resControlPanel.add(refreshResBtn);
        resControlPanel.add(cancelResBtn);

        myResPanel.add(new JScrollPane(resTable), BorderLayout.CENTER);
        myResPanel.add(resControlPanel, BorderLayout.SOUTH);

        tabbedPane.add("Search & Book", searchPanel);
        tabbedPane.add("My Reservations", myResPanel);
        
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                refreshReservationTable(resModel);
            }
        });

        return tabbedPane;
    }
    // =========================================================================
    // KOLTUK SEÇİM EKRANI
    // =========================================================================
    private void openSeatMapDialog(Flight flight) {
        JDialog dialog = new JDialog(this, "Select Seat for " + flight.getFlightNum(), true);
        dialog.setSize(600, 800);
        dialog.setLayout(new BorderLayout());

        JPanel seatGrid = new JPanel(new GridLayout(flight.getAssignedPlane().getCapacity()/6, 6, 5, 5));
        Map<String, Seat> seats = flight.getAssignedPlane().getSeatMatrix();
        
        if(seats.isEmpty()) {
            new SeatManager(flight.getAssignedPlane()); 
        }

        String[] cols = {"A", "B", "C", "D", "E", "F"};
        for(int i=1; i<= flight.getAssignedPlane().getCapacity()/6; i++) {
            for(String c : cols) {
                String seatNum = i + c;
                Seat seat = seats.get(seatNum);
                JButton btn = new JButton(seatNum);
                
                if(seat != null) {
                    if(seat.getReserveStatus()) {
                        btn.setBackground(Color.RED);
                        btn.setEnabled(false);
                    } else {
                        btn.setBackground(seat.getSeatClass() == SeatType.BUSINESS ? Color.ORANGE : Color.GREEN);
                        
                        btn.addActionListener(e -> {
                            String input = JOptionPane.showInputDialog(dialog, 
                                "Seat: " + seatNum + " (" + seat.getSeatClass() + ")\n" +
                                "Enter Baggage Weight (kg):", "0");
                            
                            if (input != null && !input.isEmpty()) {
                                try {
                                    double baggageWeight = Double.parseDouble(input);
                                    
                                    double finalPrice = CalculatePrice.calculate(seat, baggageWeight);
                                    
                                    int confirm = JOptionPane.showConfirmDialog(dialog, 
                                        "Seat Price: " + (seat.getSeatClass() == SeatType.BUSINESS ? 1000 : 500) + "TL\n" +
                                        "Baggage (" + baggageWeight + "kg): " + (finalPrice - (seat.getSeatClass() == SeatType.BUSINESS ? 1000 : 500)) + "TL\n" +
                                        "--------------------------------\n" +
                                        "TOTAL PRICE: " + finalPrice + "TL\n\n" +
                                        "Confirm Booking?", 
                                        "Confirm Payment", JOptionPane.YES_NO_OPTION);
                                    
                                    if(confirm == JOptionPane.YES_OPTION) {
                                    	reservationManager.makeReservation(currentPassenger, flight, seat, baggageWeight);
                                        flightManager.saveAllFlights();
                                        JOptionPane.showMessageDialog(dialog, "Reservation Successful!\nPrice: " + finalPrice + "TL");
                                        dialog.dispose();
                                    }
                                    
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(dialog, "Please enter a valid number for weight!");
                                }
                            }
                        });
                    }
                }
                seatGrid.add(btn);
            }
        }

        dialog.add(new JScrollPane(seatGrid), BorderLayout.CENTER);
        
        // Bilgilendirme Paneli
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(new JLabel("GREEN: Economy (500TL) | ORANGE: Business (1000TL) | RED: Occupied", SwingConstants.CENTER));
        infoPanel.add(new JLabel("Extra Baggage (>15kg): +10TL per kg", SwingConstants.CENTER));
        
        dialog.add(infoPanel, BorderLayout.NORTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    // =========================================================================
    // YARDIMCI METOTLAR (EKRAN GÜNCELLEME)
    // =========================================================================

    private void refreshReservationTable(DefaultTableModel model) {
        model.setColumnIdentifiers(new String[]{"Ticket ID", "Flight Num", "Route", "Seat", "Price", "Baggage", "Filght Date / Hour"});
        model.setRowCount(0);

        if (currentPassenger == null) return;

        for (Ticket t : reservationManager.getTickets()) {
            
            if (t.getReservation().getPassenger().getUsername().equals(currentPassenger.getUsername())) {
            	Flight flight = t.getReservation().getFlight();
                model.addRow(new Object[]{
                    t.getTicketID(),
                    t.getReservation().getFlight().getFlightNum(),
                    t.getReservation().getFlight().getRoute(),
                    t.getReservation().getSeat().getSeatNum(),
                    t.getPrice() + " TL",
                    t.getBaggageAllowance().getWeight(),
                    flight.getDate() + " / " + flight.getHour(),
                    
                });
            }
        }
    }
    
    private void refreshFlightTable(DefaultTableModel model) {
        
        model.setRowCount(0);
        for (Flight f : flightManager.getFlights()) {
            model.addRow(new Object[]{
                f.getFlightNum(),
                f.getRoute().getDepartureCity(),
                f.getRoute().getArrivalCity(),
                f.getDate(),
                f.getHour(),
                f.getDuration(),
                f.getPlane().getPlaneModel()
            });
        }
    }
    
    // =========================================================================
    // KAYIT EKRANI
    // =========================================================================
    private void openRegisterDialog() {
        JDialog dialog = new JDialog(this, "Passenger Registration", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        
        JTextField nameTxt = new JTextField();
        JTextField surnameTxt = new JTextField();
        JTextField contactTxt = new JTextField();
        JTextField userTxt = new JTextField();
        JPasswordField passTxt = new JPasswordField();
        JButton saveBtn = new JButton("Complete Registration");

        saveBtn.addActionListener(e -> {
            String name = nameTxt.getText().trim();
            String surname = surnameTxt.getText().trim();
            String contact = contactTxt.getText().trim();
            String username = userTxt.getText().trim();
            String password = new String(passTxt.getPassword()).trim();

            if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields!");
                return;
            }

            boolean success = userManager.registerPassenger(name, surname, contact, username, password);
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Registration Successful! Please Login.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Username already exists! Try another.");
            }
        });

        dialog.add(new JLabel("Name:")); dialog.add(nameTxt);
        dialog.add(new JLabel("Surname:")); dialog.add(surnameTxt);
        dialog.add(new JLabel("Contact Info (Phone/Email):")); dialog.add(contactTxt);
        dialog.add(new JLabel("Username:")); dialog.add(userTxt);
        dialog.add(new JLabel("Password:")); dialog.add(passTxt);
        dialog.add(new JLabel("")); dialog.add(saveBtn);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    // =========================================================================
    // SENARYO 1: SİMÜLASYON PANELİ
    // =========================================================================
    private JPanel createSimulationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        Plane simPlane = new Plane("SIM-01", "Simulation Plane", 180); 
        SeatManager simSeatManager = new SeatManager(simPlane); 
        
        // ÜST KISIM: KONTROLLER
        JPanel controlPanel = new JPanel();
        JCheckBox syncCheck = new JCheckBox("Use Synchronization (Safe Mode)");
        JButton startBtn = new JButton("Start Simulation (90 Users)");
        JButton resetBtn = new JButton("Reset Seats");
        JLabel statusLbl = new JLabel("Occupied: 0 / 180");
        
        controlPanel.add(syncCheck);
        controlPanel.add(startBtn);
        controlPanel.add(resetBtn);
        controlPanel.add(statusLbl);
        
        // ORTA KISIM: KOLTUK GÖRSELİ
        JPanel seatGrid = new JPanel(new GridLayout(30, 6, 2, 2));
        JButton[][] seatButtons = new JButton[30][6];
        
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 6; j++) {
                JButton btn = new JButton();
                btn.setBackground(Color.GREEN); 
                btn.setEnabled(false); 
                seatButtons[i][j] = btn;
                seatGrid.add(btn);
            }
        }

        // BAŞLAT BUTONU
        startBtn.addActionListener(e -> {
            boolean isSync = syncCheck.isSelected();
            Thread[] threads = new Thread[90];
            
            for (int i = 0; i < 90; i++) {
                threads[i] = new Thread(() -> {
                    boolean booked = false;
                    while (!booked) {
                        booked = simSeatManager.bookRandomSeat(isSync);
                    }
                });
            }

            for (Thread t : threads) t.start();
            
            new Thread(() -> {
                try { Thread.sleep(1000); } catch (Exception ex) {} 
                SwingUtilities.invokeLater(() -> updateSimulationVisuals(seatButtons, statusLbl, simPlane));
            }).start();
        });

        // SIFIRLA BUTONU
        resetBtn.addActionListener(e -> {
            simSeatManager.resetSeats();
            updateSimulationVisuals(seatButtons, statusLbl, simPlane);
        });

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(seatGrid), BorderLayout.CENTER);
        return panel;
    }

    private void updateSimulationVisuals(JButton[][] btns, JLabel lbl, Plane simPlane) {
        int occupied = 0;
        
        String[] cols = {"A", "B", "C", "D", "E", "F"};
        
        for(int i=1; i<=30; i++) {
            for(int j=0; j<6; j++) {
                 String seatNum = i + cols[j];
                 Seat s = simPlane.getSeatMatrix().get(seatNum);
                 
                 if (s != null) {
                     if(s.getReserveStatus()) {
                         btns[i-1][j].setBackground(Color.RED);
                         occupied++;
                     } else {
                         btns[i-1][j].setBackground(Color.GREEN);
                     }
                 }
            }
        }
        lbl.setText("Occupied: " + occupied + " / 180 (Expected: 90)");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AirlineGUI().setVisible(true));
    }
    
}
