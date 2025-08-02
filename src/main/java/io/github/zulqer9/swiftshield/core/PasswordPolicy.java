package io.github.zulqer9.swiftshield.core;

/**
 * Configuration for password generation policies.
 * Defines the rules and constraints for password generation.
 * 
 * @author zulqer9
 * @since 2.0.0
 */
public final class PasswordPolicy {
    private final int minLength;
    private final int maxLength;
    private final boolean requireUppercase;
    private final boolean requireLowercase;
    private final boolean requireNumbers;
    private final boolean requireSpecialChars;
    private final String allowedSpecialChars;
    private final double minEntropy;
    private final String[] forbiddenPatterns;
    
    private PasswordPolicy(Builder builder) {
        this.minLength = builder.minLength;
        this.maxLength = builder.maxLength;
        this.requireUppercase = builder.requireUppercase;
        this.requireLowercase = builder.requireLowercase;
        this.requireNumbers = builder.requireNumbers;
        this.requireSpecialChars = builder.requireSpecialChars;
        this.allowedSpecialChars = builder.allowedSpecialChars;
        this.minEntropy = builder.minEntropy;
        this.forbiddenPatterns = builder.forbiddenPatterns.clone();
    }
    
    // Getters
    public int getMinLength() { return minLength; }
    public int getMaxLength() { return maxLength; }
    public boolean requiresUppercase() { return requireUppercase; }
    public boolean requiresLowercase() { return requireLowercase; }
    public boolean requiresNumbers() { return requireNumbers; }
    public boolean requiresSpecialChars() { return requireSpecialChars; }
    public String getAllowedSpecialChars() { return allowedSpecialChars; }
    public double getMinEntropy() { return minEntropy; }
    public String[] getForbiddenPatterns() { return forbiddenPatterns.clone(); }
    
    /**
     * Creates a default password policy.
     * 
     * @return a default password policy
     */
    public static PasswordPolicy getDefault() {
        return new Builder()
                .minLength(12)
                .maxLength(128)
                .requireUppercase(true)
                .requireLowercase(true)
                .requireNumbers(true)
                .requireSpecialChars(true)
                .allowedSpecialChars("!@#$%^&*()_+-=[]{}|;:,.<>?")
                .minEntropy(50.0)
                .forbiddenPatterns("123456", "password", "qwerty")
                .build();
    }
    
    /**
     * Creates an enterprise-grade password policy.
     * 
     * @return an enterprise password policy
     */
    public static PasswordPolicy getEnterprise() {
        return new Builder()
                .minLength(14)
                .maxLength(256)
                .requireUppercase(true)
                .requireLowercase(true)
                .requireNumbers(true)
                .requireSpecialChars(true)
                .allowedSpecialChars("!@#$%^&*()_+-=[]{}|;:,.<>?~`")
                .minEntropy(60.0)
                .forbiddenPatterns("123456", "password", "qwerty", "admin", "root")
                .build();
    }
    
    /**
     * Builder class for creating PasswordPolicy instances.
     */
    public static final class Builder {
        private int minLength = 8;
        private int maxLength = 128;
        private boolean requireUppercase = true;
        private boolean requireLowercase = true;
        private boolean requireNumbers = true;
        private boolean requireSpecialChars = true;
        private String allowedSpecialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        private double minEntropy = 40.0;
        private String[] forbiddenPatterns = new String[0];
        
        public Builder minLength(int minLength) {
            if (minLength < 1) {
                throw new IllegalArgumentException("Minimum length must be positive");
            }
            this.minLength = minLength;
            return this;
        }
        
        public Builder maxLength(int maxLength) {
            if (maxLength < 1) {
                throw new IllegalArgumentException("Maximum length must be positive");
            }
            this.maxLength = maxLength;
            return this;
        }
        
        public Builder requireUppercase(boolean requireUppercase) {
            this.requireUppercase = requireUppercase;
            return this;
        }
        
        public Builder requireLowercase(boolean requireLowercase) {
            this.requireLowercase = requireLowercase;
            return this;
        }
        
        public Builder requireNumbers(boolean requireNumbers) {
            this.requireNumbers = requireNumbers;
            return this;
        }
        
        public Builder requireSpecialChars(boolean requireSpecialChars) {
            this.requireSpecialChars = requireSpecialChars;
            return this;
        }
        
        public Builder allowedSpecialChars(String allowedSpecialChars) {
            this.allowedSpecialChars = allowedSpecialChars != null ? allowedSpecialChars : "";
            return this;
        }
        
        public Builder minEntropy(double minEntropy) {
            if (minEntropy < 0) {
                throw new IllegalArgumentException("Minimum entropy cannot be negative");
            }
            this.minEntropy = minEntropy;
            return this;
        }
        
        public Builder forbiddenPatterns(String... patterns) {
            this.forbiddenPatterns = patterns != null ? patterns.clone() : new String[0];
            return this;
        }
        
        public PasswordPolicy build() {
            if (minLength > maxLength) {
                throw new IllegalArgumentException("Minimum length cannot be greater than maximum length");
            }
            return new PasswordPolicy(this);
        }
    }
    
    @Override
    public String toString() {
        return String.format("PasswordPolicy{length=%d-%d, entropy=%.1f, requirements=%s}",
                           minLength, maxLength, minEntropy, getRequirementsString());
    }
    
    private String getRequirementsString() {
        StringBuilder sb = new StringBuilder();
        if (requireUppercase) sb.append("Upper");
        if (requireLowercase) sb.append("Lower");
        if (requireNumbers) sb.append("Num");
        if (requireSpecialChars) sb.append("Special");
        return sb.toString();
    }
}