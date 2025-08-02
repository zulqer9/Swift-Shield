package io.github.zulqer9.swiftshield.core;

/**
 * Represents a generated password with its associated metadata.
 * This class encapsulates the password string along with quality metrics.
 * 
 * @author zulqer9
 * @since 2.0.0
 */
public final class Password {
    private final String value;
    private final int length;
    private final double entropy;
    private final PasswordStrength strength;
    private final long generationTime;
    
    /**
     * Creates an immutable Password instance.
     * 
     * @param value the password string
     * @param entropy the calculated entropy
     * @param strength the password strength category
     */
    public Password(String value, double entropy, PasswordStrength strength) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Password value cannot be null or empty");
        }
        if (entropy < 0) {
            throw new IllegalArgumentException("Entropy cannot be negative");
        }
        if (strength == null) {
            throw new IllegalArgumentException("Strength cannot be null");
        }
        
        this.value = value;
        this.length = value.length();
        this.entropy = entropy;
        this.strength = strength;
        this.generationTime = System.currentTimeMillis();
    }
    
    /**
     * Gets the password string value.
     * 
     * @return the password value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Gets the password length.
     * 
     * @return the password length
     */
    public int getLength() {
        return length;
    }
    
    /**
     * Gets the calculated entropy.
     * 
     * @return the entropy value
     */
    public double getEntropy() {
        return entropy;
    }
    
    /**
     * Gets the password strength category.
     * 
     * @return the strength category
     */
    public PasswordStrength getStrength() {
        return strength;
    }
    
    /**
     * Gets the generation timestamp.
     * 
     * @return the generation time in milliseconds
     */
    public long getGenerationTime() {
        return generationTime;
    }
    
    /**
     * Gets the strength percentage (0-100).
     * 
     * @return the strength as a percentage
     */
    public int getStrengthPercentage() {
        return strength.getPercentage();
    }
    
    /**
     * Gets a masked version of the password for logging.
     * 
     * @return masked password string
     */
    public String getMaskedValue() {
        if (value.length() <= 2) {
            return "*".repeat(value.length());
        }
        return value.charAt(0) + "*".repeat(value.length() - 2) + value.charAt(value.length() - 1);
    }
    
    @Override
    public String toString() {
        return String.format("Password{length=%d, entropy=%.2f, strength=%s}", 
                           length, entropy, strength);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Password password = (Password) obj;
        return value.equals(password.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}