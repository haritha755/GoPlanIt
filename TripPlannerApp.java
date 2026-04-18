import javax.swing.*;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class TripPlannerApp extends JFrame {
    
    public TripPlannerApp() {
        initializeComponents();
        setupLayout();
        applyStyling();
    }
    
    private void initializeComponents() {
        setTitle("Trip Planner App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create gradient background panel
        GradientBackgroundPanel backgroundPanel = new GradientBackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        
        // Main container with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setOpaque(false); // Make transparent to show gradient
        
        // Add main heading - GoPlanIt
        JLabel mainHeading = new JLabel("GoPlanIt", SwingConstants.CENTER);
        mainHeading.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 56));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(mainHeading);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Add subheading - Plan, Pack, Go
        JLabel subheading = new JLabel("PLAN, PACK, GO...", SwingConstants.CENTER);
        subheading.setFont(new Font("Georgia", Font.PLAIN, 18));
        subheading.setForeground(Color.WHITE);
        subheading.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subheading);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Add search box
        JPanel searchPanel = createSearchBox();
        mainPanel.add(searchPanel);
        mainPanel.add(Box.createVerticalStrut(50));
        
        // Add app highlights
        JPanel highlightsPanel = createHighlightsSection();
        mainPanel.add(highlightsPanel);
        mainPanel.add(Box.createVerticalStrut(50));
        
        // Add popular destinations
        JPanel destinationsPanel = createDestinationsSection();
        mainPanel.add(destinationsPanel);
        
        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        backgroundPanel.add(scroll, BorderLayout.CENTER);
        
        add(backgroundPanel, BorderLayout.CENTER);
    }
    
    private JLabel createHeading() {
        JLabel heading = new JLabel("Plan Your Perfect Adventure");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 40));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add drop shadow effect
        heading.setBorder(new EmptyBorder(2, 2, 0, 0));
        heading.setForeground(new Color(0, 0, 0, 50)); // Shadow color
        
        JLabel shadowLabel = new JLabel("Plan Your Perfect Adventure");
        shadowLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        shadowLabel.setForeground(Color.WHITE);
        shadowLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        shadowLabel.setBorder(new EmptyBorder(-2, -2, 2, 2));
        
        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new OverlayLayout(headingPanel));
        headingPanel.setOpaque(false);
        headingPanel.add(shadowLabel);
        headingPanel.add(heading);
        
        return new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
                FontMetrics fm = g2d.getFontMetrics();
                String text = "Plan Your Perfect Adventure";
                int x = (getWidth() - fm.stringWidth(text)) / 2 + 2;
                int y = (getHeight() + fm.getAscent()) / 2 + 2;
                g2d.drawString(text, x, y);
                
                // Draw main text
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, x - 2, y - 2);
                g2d.dispose();
            }
        };
    }
    
    private JPanel createSearchBox() {
        JPanel searchPanel = new RoundedPanel(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Light yellow gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 249, 196), 0, getHeight(), new Color(255, 236, 179));
                // subtle shadow
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(5, 5, getWidth(), getHeight(), 20, 20);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                // Do not call super to avoid repainting with white
            }
        };
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setOpaque(false);
        searchPanel.setPreferredSize(new Dimension(900, 300));
        searchPanel.setMaximumSize(new Dimension(1000, 360));
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Add shadow effect
        searchPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Search form components
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Destination field
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel destLabel = new JLabel("Destination");
        destLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        destLabel.setForeground(new Color(60, 60, 60));
        formPanel.add(destLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField destinationField = new JTextField(15);
        destinationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        destinationField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        destinationField.setText("Enter destination");
        destinationField.setForeground(Color.GRAY);
        destinationField.setBackground(Color.WHITE);
        
        // Add focus listeners for placeholder behavior
        destinationField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (destinationField.getText().equals("Enter destination")) {
                    destinationField.setText("");
                    destinationField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (destinationField.getText().isEmpty()) {
                    destinationField.setText("Enter destination");
                    destinationField.setForeground(Color.GRAY);
                }
            }
        });
        
        formPanel.add(destinationField, gbc);
        
        // Start Date field
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel startLabel = new JLabel("Start Date");
        startLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        startLabel.setForeground(new Color(60, 60, 60));
        formPanel.add(startLabel, gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField startDateField = new JTextField(10);
        startDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        startDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        startDateField.setText("DD/MM/YYYY");
        startDateField.setForeground(Color.GRAY);
        startDateField.setBackground(Color.WHITE);
        
        startDateField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (startDateField.getText().equals("DD/MM/YYYY")) {
                    startDateField.setText("");
                    startDateField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (startDateField.getText().isEmpty()) {
                    startDateField.setText("DD/MM/YYYY");
                    startDateField.setForeground(Color.GRAY);
                }
            }
        });
        
        formPanel.add(startDateField, gbc);
        
        // End Date field
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel endLabel = new JLabel("End Date");
        endLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        endLabel.setForeground(new Color(60, 60, 60));
        formPanel.add(endLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField endDateField = new JTextField(10);
        endDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        endDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        endDateField.setText("DD/MM/YYYY");
        endDateField.setForeground(Color.GRAY);
        endDateField.setBackground(Color.WHITE);
        
        endDateField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (endDateField.getText().equals("DD/MM/YYYY")) {
                    endDateField.setText("");
                    endDateField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (endDateField.getText().isEmpty()) {
                    endDateField.setText("DD/MM/YYYY");
                    endDateField.setForeground(Color.GRAY);
                }
            }
        });
        
        formPanel.add(endDateField, gbc);
        
        // Budget dropdown
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel budgetLabel = new JLabel("Budget");
        budgetLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        budgetLabel.setForeground(new Color(60, 60, 60));
        formPanel.add(budgetLabel, gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        String[] budgetOptions = {"₹2,500 - ₹3,500", "₹3,500 - ₹5,000", "₹5,000 - ₹7,500", "₹7,500 - ₹10,000", "₹10,000+"};
        JComboBox<String> budgetCombo = new JComboBox<>(budgetOptions);
        budgetCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        budgetCombo.setSelectedIndex(0); // Default to first option
        budgetCombo.setBackground(Color.WHITE);
        budgetCombo.setForeground(Color.BLACK);
        budgetCombo.setPreferredSize(new Dimension(150, 35));
        
        formPanel.add(budgetCombo, gbc);
        
        // Search button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 0));

        JButton searchButton = new GradientButton("Search Trips");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(180, 50));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String destination = destinationField.getText().trim();
                String startDate = startDateField.getText().trim();
                String endDate = endDateField.getText().trim();
                String budget = (String) budgetCombo.getSelectedItem();
                
                // Check if destination is empty or still showing placeholder
                if (destination.isEmpty() || destination.equals("Enter destination")) {
                    showStyledWarning("Input Required", "Please enter a destination!");
                    destinationField.requestFocus();
                    return;
                }
                
                // Validate dates
                if (startDate.isEmpty() || startDate.equals("DD/MM/YYYY") || 
                    endDate.isEmpty() || endDate.equals("DD/MM/YYYY")) {
                    showStyledWarning("Input Required", "Please enter both start and end dates!");
                    return;
                }
                
                // Validate date format and logic
                String dateError = validateDates(startDate, endDate);
                if (dateError != null) {
                    showStyledError("Invalid Date", dateError);
                    return;
                }
                
                // Open the TripResultsWindow with the destination and dates
                try {
                    new TripResultsWindow(destination, startDate, endDate, budget, TripPlannerApp.this);
                    dispose(); // Close the main window
                } catch (Exception ex) {
                    showStyledError("Error", "Error opening results: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        buttons.add(searchButton);

        formPanel.add(buttons, gbc);
        
        searchPanel.add(formPanel);
        
        return searchPanel;
    }
    
    private JPanel createHighlightsSection() {
        JPanel highlightsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        highlightsPanel.setOpaque(false);
        
        String[] highlights = {"1M+ Happy Travelers", "50K+ Destinations", "24/7 Support"};
        String[] icons = {"👥", "🌍", "🕐"};
        Color[] colors = {
            new Color(52, 152, 219), // Blue
            new Color(46, 204, 113), // Green
            new Color(155, 89, 182)  // Purple
        };
        
        for (int i = 0; i < highlights.length; i++) {
            JPanel highlightCard = new HighlightCard(highlights[i], icons[i], colors[i]);
            highlightsPanel.add(highlightCard);
        }
        
        return highlightsPanel;
    }
    
    private JPanel createDestinationsSection() {
        JPanel destinationsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        destinationsPanel.setOpaque(false);
        
        // Use hardcoded destinations
        String[] destinations = {"Munnar", "Bangalore", "Pondicherry"};
        String[] prices = {"From ₹2,000", "From ₹3,000", "From ₹3,500"};
        String[] imagePaths = {"munnar.jpeg", "bnglr.jpeg", "pondi.jpeg"};
        
        for (int i = 0; i < destinations.length; i++) {
            JPanel card = createDestinationCard(destinations[i], prices[i], imagePaths[i]);
            destinationsPanel.add(card);
        }
        
        return destinationsPanel;
    }
    
    private JPanel createDestinationCard(String destination, String price, String imagePath) {
        JPanel card = new DestinationCard(destination, price, imagePath);
        return card;
    }
    
    private void applyStyling() {
        // Apply modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Validates date format and logic
     * @param startDate Start date in DD/MM/YYYY format
     * @param endDate End date in DD/MM/YYYY format
     * @return Error message if validation fails, null if valid
     */
    private String validateDates(String startDate, String endDate) {
        // Check date format (DD/MM/YYYY)
        if (!startDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return "Start date must be in DD/MM/YYYY format!";
        }
        if (!endDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return "End date must be in DD/MM/YYYY format!";
        }
        
        // Parse date components
        String[] startParts = startDate.split("/");
        String[] endParts = endDate.split("/");
        
        int startDay = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]);
        int startYear = Integer.parseInt(startParts[2]);
        
        int endDay = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]);
        int endYear = Integer.parseInt(endParts[2]);
        
        // Validate month (1-12)
        if (startMonth < 1 || startMonth > 12) {
            return "Start date has invalid month! Month must be between 1 and 12.";
        }
        if (endMonth < 1 || endMonth > 12) {
            return "End date has invalid month! Month must be between 1 and 12.";
        }
        
        // Validate day based on month
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
        // Check for leap year
        if (isLeapYear(startYear)) {
            daysInMonth[1] = 29;
        }
        if (startDay < 1 || startDay > daysInMonth[startMonth - 1]) {
            return "Start date has invalid day! Day must be between 1 and " + daysInMonth[startMonth - 1] + " for month " + startMonth + ".";
        }
        
        // Reset for end date leap year check
        daysInMonth[1] = 28;
        if (isLeapYear(endYear)) {
            daysInMonth[1] = 29;
        }
        if (endDay < 1 || endDay > daysInMonth[endMonth - 1]) {
            return "End date has invalid day! Day must be between 1 and " + daysInMonth[endMonth - 1] + " for month " + endMonth + ".";
        }
        
        // Validate year (reasonable range)
        if (startYear < 2000 || startYear > 2100) {
            return "Start year must be between 2000 and 2100!";
        }
        if (endYear < 2000 || endYear > 2100) {
            return "End year must be between 2000 and 2100!";
        }
        
        // Parse dates and compare
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            LocalDate today = LocalDate.now();
            
            // Check if start date has already passed
            if (start.isBefore(today)) {
                return "Start date cannot be in the past! Please select a future date.";
            }
            
            // Check if end date has already passed
            if (end.isBefore(today)) {
                return "End date cannot be in the past! Please select a future date.";
            }
            
            // Check if end date is before start date
            if (end.isBefore(start)) {
                return "End date cannot be before start date!";
            }
            
            // Check if dates are the same
            if (end.isEqual(start)) {
                return "End date must be after start date! Please select at least a 1-day trip.";
            }
            
            // Calculate number of days
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
            
            // Check if trip duration exceeds 7 days
            if (daysBetween > 7) {
                return "Trip duration cannot exceed 7 days! Please select dates within a 7-day period.";
            }
            
        } catch (Exception e) {
            return "Invalid date format! Please use DD/MM/YYYY format.";
        }
        
        return null; // All validations passed
    }
    
    /**
     * Check if a year is a leap year
     */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    
    /**
     * Show a custom styled error dialog
     */
    private void showStyledError(String title, String message) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 220);
        dialog.setLocationRelativeTo(this);
        
        // Main panel with rounded dark blue background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(13, 27, 62));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setOpaque(false);
        
        // Error icon
        JLabel iconLabel = new JLabel("⚠️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(iconLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Message
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // OK button with same design as other buttons
        JButton okButton = new JButton("OK") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 245, 157));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        okButton.setForeground(new Color(13, 27, 62));
        okButton.setFocusPainted(false);
        okButton.setContentAreaFilled(false);
        okButton.setBorderPainted(false);
        okButton.setOpaque(false);
        okButton.setPreferredSize(new Dimension(120, 45));
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.addActionListener(e -> dialog.dispose());
        
        mainPanel.add(okButton);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Show a custom styled warning dialog
     */
    private void showStyledWarning(String title, String message) {
        showStyledError(title, message);
    }
    
    // Entry point kept inside the TripPlannerApp class
    public static void main(String[] args) {
        // Initialize database before starting the application
        DatabaseInitializer.initializeDatabase();
        
        SwingUtilities.invokeLater(() -> new TripPlannerApp().setVisible(true));
    }
}

// Custom component classes
class GradientBackgroundPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create dark blue background matching button panel
        g2d.setColor(new Color(13, 27, 62));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }
}

class RoundedPanel extends JPanel {
    private int radius;
    
    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw rounded rectangle
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRoundRect(5, 5, getWidth(), getHeight(), radius, radius);
        
        g2d.dispose();
    }
}

class RoundedTextField extends JTextField {
    private int radius;
    
    public RoundedTextField(int columns) {
        super(columns);
        this.radius = 15;
        setOpaque(false);
        setBorder(new EmptyBorder(12, 15, 12, 15));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setCaretColor(Color.BLACK);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        // Draw border
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        g2d.dispose();
        
        // Call super to paint text and cursor
        super.paintComponent(g);
    }
}

class RoundedComboBox<T> extends JComboBox<T> {
    
    public RoundedComboBox(T[] items) {
        super(items);
        // Use standard styling that works properly
        setOpaque(true);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setFocusable(true);
        setEnabled(true);
        
        // Set a standard border instead of custom painting
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Ensure proper font
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
}

class GradientButton extends JButton {
    private boolean hovered = false;
    
    public GradientButton(String text) {
        super(text);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Create gradient
        GradientPaint gradient;
        if (hovered) {
            gradient = new GradientPaint(
                0, 0, new Color(25, 118, 210),
                0, getHeight(), new Color(13, 71, 161)
            );
        } else {
            gradient = new GradientPaint(
                0, 0, new Color(33, 150, 243),
                0, getHeight(), new Color(25, 118, 210)
            );
        }
        
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        
        g2d.dispose();
        // Now paint the button text and focus outline on top
        super.paintComponent(g);
    }
}

class HighlightCard extends JPanel {
    private String text;
    private String icon;
    private Color color;
    private boolean hovered = false;
    
    public HighlightCard(String text, String icon, Color color) {
        this.text = text;
        this.icon = icon;
        this.color = color;
        setPreferredSize(new Dimension(200, 120));
        setOpaque(false);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Scale effect on hover
        if (hovered) {
            g2d.scale(1.05, 1.05);
        }
        
        // Draw gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(color.getRed(), color.getGreen(), color.getBlue(), 20),
            0, getHeight(), new Color(color.getRed(), color.getGreen(), color.getBlue(), 40)
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        
        // Draw border
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        
        // Draw icon
        g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int iconX = (getWidth() - fm.stringWidth(icon)) / 2;
        int iconY = (getHeight() / 2) - 10;
        g2d.drawString(icon, iconX, iconY);
        
        // Draw text
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.setColor(Color.WHITE);
        fm = g2d.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(text)) / 2;
        int textY = iconY + 30;
        g2d.drawString(text, textX, textY);
        
        g2d.dispose();
    }
}

class DestinationCard extends JPanel {
    private String destination;
    private String price;
    private String imagePath;
    private BufferedImage image;
    private boolean hovered = false;
    
    public DestinationCard(String destination, String price, String imagePath) {
        this.destination = destination;
        this.price = price;
        this.imagePath = imagePath;
        setPreferredSize(new Dimension(220, 260));
        setMaximumSize(new Dimension(220, 260));
        setOpaque(false);
        
        // Load image
        try {
            image = ImageIO.read(resolveImage(imagePath));
        } catch (IOException e) {
            System.err.println("Could not load image: " + imagePath);
            image = null;
        }
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Scale effect on hover
        if (hovered) {
            g2d.scale(1.05, 1.05);
        }
        
        // Draw card background
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        
        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRoundRect(5, 5, getWidth(), getHeight(), 20, 20);
        
        // Draw image
        if (image != null) {
            g2d.setColor(Color.WHITE);
            // Clip to rounded top corners before drawing image so image has rounded corners
            Shape clip = g2d.getClip();
            int imgHeight = 120;
            RoundRectangle2D rr = new RoundRectangle2D.Float(0, 0, getWidth(), imgHeight, 20, 20);
            g2d.setClip(rr);

            // Scale and center image
            int imgWidth = getWidth();
            // int imgHeight defined above
            int x = 0;
            int y = 0;

            // Calculate scaling to maintain aspect ratio
            double scaleX = (double) imgWidth / image.getWidth();
            double scaleY = (double) imgHeight / image.getHeight();
            double scale = Math.max(scaleX, scaleY);

            int scaledWidth = (int) (image.getWidth() * scale);
            int scaledHeight = (int) (image.getHeight() * scale);

            int imgX = (imgWidth - scaledWidth) / 2;
            int imgY = (imgHeight - scaledHeight) / 2;

            g2d.drawImage(image, imgX, imgY, scaledWidth, scaledHeight, null);
            g2d.setClip(clip);
        } else {
            // Fallback colored background
            g2d.setColor(new Color(200, 200, 200));
            g2d.fillRoundRect(0, 0, getWidth(), 140, 20, 20);
        }
        
        // Draw destination name
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.setColor(new Color(33, 37, 41));
        FontMetrics fm = g2d.getFontMetrics();
        int nameX = (getWidth() - fm.stringWidth(destination)) / 2;
        int nameY = 160;
        g2d.drawString(destination, nameX, nameY);
        
        // Draw price
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2d.setColor(new Color(108, 117, 125));
        fm = g2d.getFontMetrics();
        int priceX = (getWidth() - fm.stringWidth(price)) / 2;
        int priceY = nameY + 20;
        g2d.drawString(price, priceX, priceY);
        
        g2d.dispose();
    }

    // Moved here: resolve image paths robustly
    private static File resolveImage(String path) {
        File candidate = new File(path);
        if (candidate.exists()) return candidate;
        File withDsa = new File("dsa" + File.separator + path);
        if (withDsa.exists()) return withDsa;
        File fromUserDir = new File(System.getProperty("user.dir"), path);
        if (fromUserDir.exists()) return fromUserDir;
        File fromUserDirDsa = new File(System.getProperty("user.dir"), "dsa" + File.separator + path);
        return fromUserDirDsa;
    }
}