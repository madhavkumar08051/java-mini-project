package ui;

import java.awt.Color;

public class ThemeManager {
    // Modern Color Palette
    public static final Color PRIMARY = new Color(108, 99, 255);    // Soft Purple
    public static final Color SUCCESS = new Color(67, 217, 173);    // Mint Green
    public static final Color DANGER = new Color(255, 101, 132);     // Coral Pink
    
    // Light Mode
    public static final Color LIGHT_BG = new Color(248, 249, 252);
    public static final Color LIGHT_CARD = Color.WHITE;
    public static final Color LIGHT_TEXT = new Color(45, 45, 60);
    
    // Dark Mode
    public static final Color DARK_BG = new Color(30, 30, 46);
    public static final Color DARK_CARD = new Color(45, 45, 65);
    public static final Color DARK_TEXT = new Color(230, 230, 240);

    private static boolean isDarkMode = true;

    public static Color getBackground() { return isDarkMode ? DARK_BG : LIGHT_BG; }
    public static Color getCardBackground() { return isDarkMode ? DARK_CARD : LIGHT_CARD; }
    public static Color getTextColor() { return isDarkMode ? DARK_TEXT : LIGHT_TEXT; }
    
    public static void toggleTheme() {
        isDarkMode = !isDarkMode;
    }

    public static boolean isDarkMode() {
        return isDarkMode;
    }
}
