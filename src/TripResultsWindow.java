import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * TripResultsWindow - Displays hotels, tourist spots, and food spots for a destination
 * 
 * TO CHANGE TEXT COLORS:
 * Scroll down to lines 18-20 where the color constants are defined.
 * Modify the RGB values in the Color constructors:
 *   - SECTION_HEADING_COLOR: Color for "HOTELS", "TOURIST SPOTS", "FOOD SPOTS" headings
 *   - CARD_TITLE_COLOR: Color for hotel/spot names in the cards
 *   - MAIN_TITLE_COLOR: Color for the main "Explore [destination]" title
 * 
 * Example: new Color(255, 0, 0) = Red, new Color(0, 255, 0) = Green, new Color(0, 0, 255) = Blue
 */
public class TripResultsWindow extends JFrame {
    private String destination;
    private String startDate;
    private String endDate;
    private String budget;
    private int numberOfDays;
    private Color accentColor = new Color(52, 152, 219);
    private Color cardBg = new Color(255, 255, 255, 240);
    private java.util.List<String> selectedHotels = new java.util.ArrayList<>();
    private java.util.List<String> selectedTouristSpots = new java.util.ArrayList<>();
    private java.util.List<String> selectedFoodSpots = new java.util.ArrayList<>();
    private JFrame parentFrame;
    
    // *** COLOR CONFIGURATION - CHANGE THESE TO UPDATE ALL TEXT COLORS ***
    private static final Color SECTION_HEADING_COLOR = new Color(60, 60, 60);     // Dark gray for section headings
    private static final Color CARD_TITLE_COLOR = new Color(20, 20, 20);          // Dark text for card titles
    private static final Color MAIN_TITLE_COLOR = new Color(255, 255, 255);       // White for main title on dark background
    private static final Color CARD_BG_COLOR = new Color(255, 249, 196);          // Light yellow like search panel
    
    public TripResultsWindow(String destination, String startDate, String endDate, String budget, JFrame parentFrame) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.parentFrame = parentFrame;
        this.numberOfDays = calculateDays(startDate, endDate);
        initializeWindow();
        loadData();
    }
    
    private int calculateDays(String start, String end) {
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.LocalDate startDate = java.time.LocalDate.parse(start, formatter);
            java.time.LocalDate endDate = java.time.LocalDate.parse(end, formatter);
            return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        } catch (Exception e) {
            return 3; // Default to 3 days if parsing fails
        }
    }
    
    private void initializeWindow() {
        setTitle("Trip Details - " + destination);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Create main scrollable panel with all sections
        JPanel mainPanel = createMainPanel();
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Add button panel at bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(13, 27, 62));
        
        // Previous button - matching design with create plan button
        JButton previousButton = createRoundedButton("← Previous");
        previousButton.addActionListener(e -> goBackToPreviousPage());
        
        // Create plan button
        JButton createPlanButton = createRoundedButton("Create My Perfect Plan");
        createPlanButton.addActionListener(e -> showFinalPlan());
        
        buttonPanel.add(previousButton);
        buttonPanel.add(createPlanButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // Dark blue gradient background (same as first page)
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create dark blue background matching button panel
                g2d.setColor(new Color(13, 27, 62));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // Add title with stylish font
        JLabel titleLabel = new JLabel("Explore " + destination);
        titleLabel.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 48));
        titleLabel.setForeground(MAIN_TITLE_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Add trip info
        JLabel tripInfo = new JLabel(numberOfDays + " Days Trip | " + startDate + " to " + endDate + " | Budget: " + budget);
        tripInfo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tripInfo.setForeground(new Color(255, 255, 255));
        tripInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(tripInfo);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Add all sections
        mainPanel.add(createHotelsSection());
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(createTouristSpotsSection());
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(createFoodSpotsSection());
        mainPanel.add(Box.createVerticalStrut(30));
        
        return mainPanel;
    }
    
    private String getImagePathForDestination() {
        switch (destination.toLowerCase()) {
            case "munnar": return "munnar.jpeg";
            case "bangalore": return "bnglr.jpeg";
            case "goa": return "goa.jpeg";
            case "pondicherry": return "pondi.jpeg";
            default: return "munnar.jpeg"; // fallback image
        }
    }
    
    private JPanel createHotelsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        
        // Section heading
        JLabel heading = new JLabel("🏨 HOTELS");
        heading.setFont(new Font("Segoe UI Emoji", Font.BOLD, 48));
        heading.setForeground(CARD_BG_COLOR);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.add(heading);
        section.add(Box.createVerticalStrut(20));
        
        // Hotels grid - smaller and centered
        JPanel hotelsGrid = new JPanel(new GridLayout(0, 2, 15, 15));
        hotelsGrid.setOpaque(false);
        hotelsGrid.setMaximumSize(new Dimension(900, 2000));
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM hotels WHERE destination = ?")) {
            
            stmt.setString(1, destination);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String hotelName = rs.getString("name");
                String price = "₹" + String.format("%.0f", rs.getDouble("price_per_night")) + " per night";
                JPanel hotelCard = createSelectableCard(
                    hotelName,
                    price,
                    new Color(46, 204, 113),
                    selectedHotels
                );
                hotelsGrid.add(hotelCard);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading hotels: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            section.add(errorLabel);
        }
        
        section.add(hotelsGrid);
        section.setAlignmentX(Component.CENTER_ALIGNMENT);
        return section;
    }
    
    private JPanel createTouristSpotsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        
        // Section heading
        JLabel heading = new JLabel("🗺️ TOURIST SPOTS");
        heading.setFont(new Font("Segoe UI Emoji", Font.BOLD, 48));
        heading.setForeground(CARD_BG_COLOR);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.add(heading);
        section.add(Box.createVerticalStrut(20));
        
        // Tourist spots grid - smaller and centered
        JPanel spotsGrid = new JPanel(new GridLayout(0, 3, 12, 12));
        spotsGrid.setOpaque(false);
        spotsGrid.setMaximumSize(new Dimension(900, 2000));
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tourist_spots WHERE destination = ?")) {
            
            stmt.setString(1, destination);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String spotName = rs.getString("name");
                JPanel spotCard = createSelectableCard(
                    spotName,
                    "",
                    new Color(155, 89, 182),
                    selectedTouristSpots
                );
                spotsGrid.add(spotCard);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading tourist spots: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            section.add(errorLabel);
        }
        
        section.add(spotsGrid);
        section.setAlignmentX(Component.CENTER_ALIGNMENT);
        return section;
    }
    
    private JPanel createFoodSpotsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        
        // Section heading
        JLabel heading = new JLabel("🍽️ FOOD SPOTS");
        heading.setFont(new Font("Segoe UI Emoji", Font.BOLD, 48));
        heading.setForeground(CARD_BG_COLOR);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.add(heading);
        section.add(Box.createVerticalStrut(20));
        
        // Food spots grid - smaller and centered
        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 12, 12));
        foodGrid.setOpaque(false);
        foodGrid.setMaximumSize(new Dimension(900, 2000));
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM food_spots WHERE destination = ?")) {
            
            stmt.setString(1, destination);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String foodName = rs.getString("name");
                JPanel foodCard = createSelectableCard(
                    foodName,
                    "",
                    new Color(230, 126, 34),
                    selectedFoodSpots
                );
                foodGrid.add(foodCard);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading food spots: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            section.add(errorLabel);
        }
        
        section.add(foodGrid);
        section.setAlignmentX(Component.CENTER_ALIGNMENT);
        return section;
    }
    
    private JPanel createSelectableCard(String title, String subtitle, Color accentColor, java.util.List<String> selectionList) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(8, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Create rounded button with same design as main buttons
        JButton selectButton = new JButton(title + (subtitle != null && !subtitle.isEmpty() ? " - " + subtitle : "")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                Boolean isSelected = (Boolean) getClientProperty("selected");
                if (isSelected != null && isSelected) {
                    g2d.setColor(new Color(46, 204, 113)); // Green when selected
                } else {
                    g2d.setColor(new Color(255, 245, 157)); // Lighter yellow when not selected
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        selectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectButton.setForeground(new Color(13, 27, 62)); // Dark blue text
        selectButton.setFocusPainted(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setBorderPainted(false);
        selectButton.setOpaque(false);
        selectButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        selectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectButton.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Initialize selection state
        selectButton.putClientProperty("selected", false);
        
        // Toggle selection on click
        selectButton.addActionListener(e -> {
            JButton btn = (JButton) e.getSource();
            boolean isSelected = selectionList.contains(title);
            
            if (isSelected) {
                selectionList.remove(title);
                btn.putClientProperty("selected", false);
            } else {
                selectionList.add(title);
                btn.putClientProperty("selected", true);
            }
            btn.repaint();
        });
        
        card.add(selectButton, BorderLayout.CENTER);
        
        return card;
    }
    
    private void showFinalPlan() {
        // Validate minimum selections
        if (selectedTouristSpots.isEmpty()) {
            showStyledError("No Tourist Spot Selected", "Please select at least one tourist spot!");
            return;
        }
        
        // Validate that there are enough tourist spots for each day
        if (selectedTouristSpots.size() < numberOfDays) {
            showStyledError("Insufficient Tourist Spots", 
                "Please select at least " + numberOfDays + " tourist spots!<br>" +
                "You need at least one tourist spot for each day of your " + numberOfDays + "-day trip.<br>" +
                "Currently selected: " + selectedTouristSpots.size() + " spot(s)");
            return;
        }
        
        if (selectedHotels.isEmpty()) {
            showStyledError("No Hotel Selected", "Please select at least one hotel!");
            return;
        }
        
        // Validate that number of hotels doesn't exceed number of nights
        int numberOfNights = numberOfDays - 1;
        if (selectedHotels.size() > numberOfNights) {
            showStyledError("Too Many Hotels Selected", 
                "You have selected too many hotels!<br><br>" +
                "For a " + numberOfDays + "-day trip, you need only " + numberOfNights + " night(s) of stay.<br>" +
                "Currently selected: " + selectedHotels.size() + " hotel(s)<br><br>" +
                "Please select at most " + numberOfNights + " hotel(s).");
            return;
        }
        
        if (selectedFoodSpots.isEmpty()) {
            showStyledError("No Food Spot Selected", "Please select at least one food spot!");
            return;
        }
        
        // Validate that there are enough food spots for each day
        if (selectedFoodSpots.size() < numberOfDays) {
            showStyledError("Insufficient Food Spots", 
                "Please select at least " + numberOfDays + " food spots!<br>" +
                "You need at least one food spot for each day of your " + numberOfDays + "-day trip.<br>" +
                "Currently selected: " + selectedFoodSpots.size() + " spot(s)");
            return;
        }
        
        // Calculate hotel cost and validate against budget
        double hotelCost = calculateTotalCost();
        double budgetLimit = getBudgetLimit();
        
        if (hotelCost > budgetLimit) {
            showStyledError("Budget Exceeded", 
                "Warning: Selected hotel prices exceed your budget!<br><br>" +
                "Hotel Cost (for " + numberOfNights + " nights): ₹" + String.format("%.2f", hotelCost) + "<br>" +
                "Budget Limit: ₹" + String.format("%.2f", budgetLimit) + "<br><br>" +
                "Please select cheaper hotels or increase your budget.");
            return;
        }
        
        // Create final plan window
        JFrame finalPlanFrame = new JFrame("Your Perfect Trip Plan");
        finalPlanFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        finalPlanFrame.setLocationRelativeTo(this);
        finalPlanFrame.setLayout(new BorderLayout());
        finalPlanFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel with gradient matching first page
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Create dark blue background matching button panel
                g2d.setColor(new Color(13, 27, 62));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Your Perfect " + numberOfDays + "-Day Trip Plan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Destination label
        JLabel destLabel = new JLabel(destination + " | " + startDate + " to " + endDate);
        destLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        destLabel.setForeground(new Color(255, 255, 255));
        destLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(destLabel);
        mainPanel.add(Box.createVerticalStrut(40));
        
        // Create day-by-day itinerary
        for (int day = 1; day <= numberOfDays; day++) {
            mainPanel.add(createDayPlan(day));
            mainPanel.add(Box.createVerticalStrut(15));
        }
        
        // Add extra space at the bottom to ensure last day is fully visible
        mainPanel.add(Box.createVerticalStrut(50));
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        finalPlanFrame.add(scrollPane, BorderLayout.CENTER);
        
        // Add previous button to final plan page
        JPanel finalButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        finalButtonPanel.setBackground(new Color(13, 27, 62));
        
        JButton finalPreviousButton = createRoundedButton("← Previous");
        finalPreviousButton.addActionListener(e -> {
            finalPlanFrame.dispose();
            TripResultsWindow.this.setVisible(true);
        });
        
        finalButtonPanel.add(finalPreviousButton);
        finalPlanFrame.add(finalButtonPanel, BorderLayout.SOUTH);
        
        finalPlanFrame.setVisible(true);
        TripResultsWindow.this.setVisible(false);
    }
    
    private JPanel createDayPlan(int dayNumber) {
        // Create rounded panel with same design as buttons
        JPanel dayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Same yellow background as buttons
                g2d.setColor(new Color(255, 245, 157));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                g2d.dispose();
            }
        };
        dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
        dayPanel.setOpaque(false);
        dayPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        dayPanel.setMaximumSize(new Dimension(700, Integer.MAX_VALUE)); // Width 700, height auto-adjusts
        dayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Day heading - same text color as buttons
        JLabel dayHeading = new JLabel("DAY " + dayNumber);
        dayHeading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        dayHeading.setForeground(new Color(13, 27, 62)); // Same dark blue as button text
        dayHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        dayPanel.add(dayHeading);
        dayPanel.add(Box.createVerticalStrut(8));
        
        // Hotels - show on all days EXCEPT the last day
        if (dayNumber < numberOfDays) {
            int hotelStartIndex = distributeStartIndex(selectedHotels.size(), numberOfDays - 1, dayNumber);
            int hotelEndIndex = distributeEndIndex(selectedHotels.size(), numberOfDays - 1, dayNumber);
            
            // Ensure at least one hotel per day (except last day) if available
            if (hotelStartIndex >= hotelEndIndex && selectedHotels.size() > 0) {
                // If no hotel assigned but hotels exist, assign at least one
                int index = (dayNumber - 1) % selectedHotels.size();
                String hotel = selectedHotels.get(index);
                JLabel hotelLabel = new JLabel("🏨 Stay: " + hotel);
                hotelLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 17));
                hotelLabel.setForeground(new Color(13, 27, 62)); // Same dark blue as button text
                hotelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dayPanel.add(hotelLabel);
                dayPanel.add(Box.createVerticalStrut(3));
            } else {
                for (int i = hotelStartIndex; i < hotelEndIndex; i++) {
                    String hotel = selectedHotels.get(i);
                    JLabel hotelLabel = new JLabel("🏨 Stay: " + hotel);
                    hotelLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 17));
                    hotelLabel.setForeground(new Color(13, 27, 62)); // Same dark blue as button text
                    hotelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    dayPanel.add(hotelLabel);
                    dayPanel.add(Box.createVerticalStrut(3));
                }
            }
            dayPanel.add(Box.createVerticalStrut(2));
        }
        
        // Tourist spots - distribute evenly across ALL days (every day has places to visit)
        int spotStartIndex = distributeStartIndex(selectedTouristSpots.size(), numberOfDays, dayNumber);
        int spotEndIndex = distributeEndIndex(selectedTouristSpots.size(), numberOfDays, dayNumber);
        
        // Ensure at least one tourist spot per day if available
        if (spotStartIndex >= spotEndIndex && selectedTouristSpots.size() > 0) {
            // If no spot assigned but spots exist, assign at least one
            int index = (dayNumber - 1) % selectedTouristSpots.size();
            String spot = selectedTouristSpots.get(index);
            JLabel spotLabel = new JLabel("🗺️ Visit: " + spot);
            spotLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 17));
            spotLabel.setForeground(new Color(13, 27, 62)); // Same dark blue as button text
            spotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            dayPanel.add(spotLabel);
            dayPanel.add(Box.createVerticalStrut(3));
        } else {
            for (int i = spotStartIndex; i < spotEndIndex; i++) {
                String spot = selectedTouristSpots.get(i);
                JLabel spotLabel = new JLabel("🗺️ Visit: " + spot);
                spotLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 17));
                spotLabel.setForeground(new Color(13, 27, 62)); // Same dark blue as button text
                spotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dayPanel.add(spotLabel);
                dayPanel.add(Box.createVerticalStrut(3));
            }
        }
        
        dayPanel.add(Box.createVerticalStrut(2));
        
        // Food spots - distribute evenly across ALL days (every day has places to eat)
        int foodStartIndex = distributeStartIndex(selectedFoodSpots.size(), numberOfDays, dayNumber);
        int foodEndIndex = distributeEndIndex(selectedFoodSpots.size(), numberOfDays, dayNumber);
        
        // Ensure at least one food spot per day if available
        if (foodStartIndex >= foodEndIndex && selectedFoodSpots.size() > 0) {
            // If no food assigned but food spots exist, assign at least one
            int index = (dayNumber - 1) % selectedFoodSpots.size();
            String food = selectedFoodSpots.get(index);
            JLabel foodLabel = new JLabel("🍽️ Eat at: " + food);
            foodLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 17));
            foodLabel.setForeground(new Color(13, 27, 62)); // Same dark blue as button text
            foodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            dayPanel.add(foodLabel);
            dayPanel.add(Box.createVerticalStrut(3));
        } else {
            for (int i = foodStartIndex; i < foodEndIndex; i++) {
                String food = selectedFoodSpots.get(i);
                JLabel foodLabel = new JLabel("🍽️ Eat at: " + food);
                foodLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 17));
                foodLabel.setForeground(new Color(13, 27, 62)); // Same dark blue as button text
                foodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dayPanel.add(foodLabel);
                dayPanel.add(Box.createVerticalStrut(3));
            }
        }
        
        return dayPanel;
    }
    
    private JPanel createFinalSection(String title, java.util.List<String> items) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        heading.setForeground(new Color(255, 223, 0));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(heading);
        section.add(Box.createVerticalStrut(15));
        
        for (String item : items) {
            JLabel itemLabel = new JLabel("  • " + item);
            itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            itemLabel.setForeground(new Color(240, 240, 240));
            itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            section.add(itemLabel);
            section.add(Box.createVerticalStrut(8));
        }
        
        return section;
    }
    
    private void loadData() {
        // This method can be used to load any additional data needed
    }
    
    /**
     * Creates a rounded button with consistent styling
     */
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setBackground(new Color(255, 245, 157)); // Lighter yellow
        button.setForeground(new Color(13, 27, 62));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Go back to the previous page (main trip planner)
     */
    private void goBackToPreviousPage() {
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
        dispose(); // Close current window
    }
    
    /**
     * Show a custom styled error dialog
     */
    private void showStyledError(String title, String message) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        // Larger size for longer messages
        dialog.setSize(450, 280);
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
    
    private double getBudgetLimit() {
        // Parse budget string to get upper limit
        if (budget.contains("₹10,000+")) {
            return 100000; // No upper limit, use large number
        }
        
        String[] parts = budget.split(" - ");
        if (parts.length == 2) {
            String upperLimit = parts[1].replace("₹", "").replace(",", "");
            try {
                return Double.parseDouble(upperLimit);
            } catch (NumberFormatException e) {
                return 10000; // Default
            }
        }
        return 10000; // Default
    }
    
    private double calculateTotalCost() {
        double hotelCost = 0;
        
        // Calculate hotel costs based on actual nights stayed at each hotel
        // No stay on the last day
        int numberOfNights = numberOfDays - 1;
        
        // Count how many nights each hotel is used in the itinerary
        java.util.Map<String, Integer> hotelNightsMap = new java.util.HashMap<>();
        
        // Iterate through each day (except last day) to see which hotel is assigned
        for (int day = 1; day < numberOfDays; day++) {
            int hotelStartIndex = distributeStartIndex(selectedHotels.size(), numberOfNights, day);
            int hotelEndIndex = distributeEndIndex(selectedHotels.size(), numberOfNights, day);
            
            // Handle case where at least one hotel per day is assigned
            if (hotelStartIndex >= hotelEndIndex && selectedHotels.size() > 0) {
                int index = (day - 1) % selectedHotels.size();
                String hotel = selectedHotels.get(index);
                hotelNightsMap.put(hotel, hotelNightsMap.getOrDefault(hotel, 0) + 1);
            } else {
                // Add all hotels assigned to this day
                for (int i = hotelStartIndex; i < hotelEndIndex; i++) {
                    String hotel = selectedHotels.get(i);
                    hotelNightsMap.put(hotel, hotelNightsMap.getOrDefault(hotel, 0) + 1);
                }
            }
        }
        
        // Calculate cost based on actual nights at each hotel
        try (Connection conn = DatabaseConnection.getConnection()) {
            for (java.util.Map.Entry<String, Integer> entry : hotelNightsMap.entrySet()) {
                String hotelName = entry.getKey();
                int nightsAtHotel = entry.getValue();
                
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT price_per_night FROM hotels WHERE name = ? AND destination = ?");
                stmt.setString(1, hotelName);
                stmt.setString(2, destination);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    double pricePerNight = rs.getDouble("price_per_night");
                    hotelCost += pricePerNight * nightsAtHotel;
                }
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return hotelCost;
    }
    
    /**
     * Helper method to calculate the start index for even distribution
     * @param totalItems Total number of items to distribute
     * @param totalDays Total number of days
     * @param currentDay Current day number (1-indexed)
     * @return Start index for this day
     */
    private int distributeStartIndex(int totalItems, int totalDays, int currentDay) {
        int baseItemsPerDay = totalItems / totalDays;
        int remainder = totalItems % totalDays;
        
        // Days 1 to remainder get one extra item
        if (currentDay <= remainder) {
            return (currentDay - 1) * (baseItemsPerDay + 1);
        } else {
            return remainder * (baseItemsPerDay + 1) + (currentDay - remainder - 1) * baseItemsPerDay;
        }
    }
    
    /**
     * Helper method to calculate the end index for even distribution
     * @param totalItems Total number of items to distribute
     * @param totalDays Total number of days
     * @param currentDay Current day number (1-indexed)
     * @return End index for this day (exclusive)
     */
    private int distributeEndIndex(int totalItems, int totalDays, int currentDay) {
        int baseItemsPerDay = totalItems / totalDays;
        int remainder = totalItems % totalDays;
        
        // Days 1 to remainder get one extra item
        if (currentDay <= remainder) {
            return currentDay * (baseItemsPerDay + 1);
        } else {
            return remainder * (baseItemsPerDay + 1) + (currentDay - remainder) * baseItemsPerDay;
        }
    }
}

// Rounded card panel for selectable items
class RoundedCardPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw rounded rectangle background
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        
        // Draw border
        g2d.setColor(new Color(200, 200, 200));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        
        g2d.dispose();
    }
}
