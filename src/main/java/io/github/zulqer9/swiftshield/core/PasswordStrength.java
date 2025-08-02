package io.github.zulqer9.swiftshield.core;

/**
 * Enumeration representing different password strength levels.
 * Each level has an associated percentage and descriptive label.
 * 
 * @author zulqer9
 * @since 2.0.0
 */
public enum PasswordStrength {
    /** Very weak password (0-20%) */
    VERY_WEAK(0, 20, "Very Weak", "Critical"),
    
    /** Weak password (21-40%) */
    WEAK(21, 40, "Weak", "Poor"),
    
    /** Fair password (41-60%) */
    FAIR(41, 60, "Fair", "Acceptable"),
    
    /** Good password (61-80%) */
    GOOD(61, 80, "Good", "Strong"),
    
    /** Strong password (81-90%) */
    STRONG(81, 90, "Strong", "Excellent"),
    
    /** Very strong password (91-100%) */
    VERY_STRONG(91, 100, "Very Strong", "Unbreakable");
    
    private final int minPercentage;
    private final int maxPercentage;
    private final String displayName;
    private final String description;
    
    PasswordStrength(int minPercentage, int maxPercentage, String displayName, String description) {
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * Gets the minimum percentage for this strength level.
     * 
     * @return the minimum percentage
     */
    public int getMinPercentage() {
        return minPercentage;
    }
    
    /**
     * Gets the maximum percentage for this strength level.
     * 
     * @return the maximum percentage
     */
    public int getMaxPercentage() {
        return maxPercentage;
    }
    
    /**
     * Gets the display name for this strength level.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the description for this strength level.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the percentage value for this strength level (midpoint).
     * 
     * @return the percentage value
     */
    public int getPercentage() {
        return (minPercentage + maxPercentage) / 2;
    }
    
    /**
     * Determines the password strength based on entropy.
     * 
     * @param entropy the calculated entropy
     * @return the corresponding strength level
     */
    public static PasswordStrength fromEntropy(double entropy) {
        if (entropy < 25) return VERY_WEAK;
        if (entropy < 35) return WEAK;
        if (entropy < 50) return FAIR;
        if (entropy < 65) return GOOD;
        if (entropy < 80) return STRONG;
        return VERY_STRONG;
    }
    
    /**
     * Determines the password strength based on percentage.
     * 
     * @param percentage the strength percentage (0-100)
     * @return the corresponding strength level
     */
    public static PasswordStrength fromPercentage(int percentage) {
        if (percentage <= 20) return VERY_WEAK;
        if (percentage <= 40) return WEAK;
        if (percentage <= 60) return FAIR;
        if (percentage <= 80) return GOOD;
        if (percentage <= 90) return STRONG;
        return VERY_STRONG;
    }
    
    /**
     * Gets the visual representation of the strength as a progress bar.
     * 
     * @param barLength the length of the progress bar
     * @return a string representation of the strength bar
     */
    public String getProgressBar(int barLength) {
        int filledBlocks = (getPercentage() * barLength) / 100;
        int emptyBlocks = barLength - filledBlocks;
        return "█".repeat(filledBlocks) + "░".repeat(emptyBlocks);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}