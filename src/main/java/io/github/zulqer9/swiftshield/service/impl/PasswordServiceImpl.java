package io.github.zulqer9.swiftshield.service.impl;

import io.github.zulqer9.swiftshield.core.Password;
import io.github.zulqer9.swiftshield.core.PasswordPolicy;
import io.github.zulqer9.swiftshield.core.PasswordStrength;
import io.github.zulqer9.swiftshield.service.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of PasswordService providing secure password generation and analysis.
 * Uses cryptographically secure random number generation and entropy calculations.
 * 
 * @author zulqer9
 * @since 2.0.0
 */
public class PasswordServiceImpl implements PasswordService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordServiceImpl.class);
    private static final Logger securityLogger = LoggerFactory.getLogger("io.github.zulqer9.swiftshield.security");
    
    // Character sets for password generation
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String DEFAULT_SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    
    private final SecureRandom secureRandom;
    private final PasswordPolicy defaultPolicy;
    
    public PasswordServiceImpl() {
        this.secureRandom = new SecureRandom();
        this.defaultPolicy = PasswordPolicy.getDefault();
        logger.info("PasswordService initialized with secure random generator");
        securityLogger.info("Password service initialized with default policy: {}", defaultPolicy);
    }
    
    @Override
    public Password generatePassword() {
        return generatePassword(defaultPolicy);
    }
    
    @Override
    public Password generatePassword(PasswordPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Password policy cannot be null");
        }
        
        logger.debug("Generating password with policy: {}", policy);
        
        // Determine password length within policy bounds
        int minLen = policy.getMinLength();
        int maxLen = policy.getMaxLength();
        int length = minLen + secureRandom.nextInt(maxLen - minLen + 1);
        
        // Build character pool based on policy
        StringBuilder charPool = new StringBuilder();
        StringBuilder password = new StringBuilder();
        
        // Ensure at least one character from each required category
        if (policy.requiresUppercase()) {
            charPool.append(UPPERCASE);
            password.append(UPPERCASE.charAt(secureRandom.nextInt(UPPERCASE.length())));
        }
        
        if (policy.requiresLowercase()) {
            charPool.append(LOWERCASE);
            password.append(LOWERCASE.charAt(secureRandom.nextInt(LOWERCASE.length())));
        }
        
        if (policy.requiresNumbers()) {
            charPool.append(NUMBERS);
            password.append(NUMBERS.charAt(secureRandom.nextInt(NUMBERS.length())));
        }
        
        if (policy.requiresSpecialChars()) {
            String specialChars = policy.getAllowedSpecialChars();
            if (specialChars.isEmpty()) {
                specialChars = DEFAULT_SPECIAL;
            }
            charPool.append(specialChars);
            password.append(specialChars.charAt(secureRandom.nextInt(specialChars.length())));
        }
        
        // Fill remaining positions
        String pool = charPool.toString();
        int remaining = length - password.length();
        for (int i = 0; i < remaining; i++) {
            password.append(pool.charAt(secureRandom.nextInt(pool.length())));
        }
        
        // Shuffle the password to avoid predictable patterns
        char[] passArray = password.toString().toCharArray();
        for (int i = passArray.length - 1; i > 0; i--) {
            int j = secureRandom.nextInt(i + 1);
            char temp = passArray[i];
            passArray[i] = passArray[j];
            passArray[j] = temp;
        }
        
        String finalPassword = new String(passArray);
        
        // Validate against forbidden patterns
        if (containsForbiddenPattern(finalPassword, policy.getForbiddenPatterns())) {
            logger.debug("Generated password contains forbidden pattern, regenerating");
            return generatePassword(policy); // Recursive retry
        }
        
        double entropy = calculateEntropy(finalPassword);
        PasswordStrength strength = PasswordStrength.fromEntropy(entropy);
        
        Password result = new Password(finalPassword, entropy, strength);
        
        logger.debug("Password generated successfully: {}", result);
        securityLogger.info("Password generated: length={}, entropy={:.2f}, strength={}", 
                          result.getLength(), entropy, strength);
        
        return result;
    }
    
    @Override
    public Password generatePassword(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Password length must be positive");
        }
        
        PasswordPolicy policy = new PasswordPolicy.Builder()
                .minLength(length)
                .maxLength(length)
                .requireUppercase(true)
                .requireLowercase(true)
                .requireNumbers(true)
                .requireSpecialChars(true)
                .build();
        
        return generatePassword(policy);
    }
    
    @Override
    public Password optimizePassword(String existingPassword) {
        return optimizePassword(existingPassword, defaultPolicy);
    }
    
    @Override
    public Password optimizePassword(String existingPassword, PasswordPolicy policy) {
        if (existingPassword == null || existingPassword.trim().isEmpty()) {
            logger.debug("Empty password provided for optimization, generating new password");
            return generatePassword(policy);
        }
        
        logger.debug("Optimizing password of length: {}", existingPassword.length());
        
        String trimmed = existingPassword.trim();
        
        // Keep a portion of the original password for user recognition
        int keepLength = Math.min(3, trimmed.length());
        String keptPart = trimmed.substring(0, keepLength);
        
        // Generate additional secure content
        StringBuilder charPool = new StringBuilder();
        StringBuilder optimization = new StringBuilder(keptPart);
        
        // Build character pool
        if (policy.requiresUppercase()) charPool.append(UPPERCASE);
        if (policy.requiresLowercase()) charPool.append(LOWERCASE);
        if (policy.requiresNumbers()) charPool.append(NUMBERS);
        if (policy.requiresSpecialChars()) {
            String specialChars = policy.getAllowedSpecialChars();
            charPool.append(specialChars.isEmpty() ? DEFAULT_SPECIAL : specialChars);
        }
        
        // Ensure required character types are present
        if (policy.requiresUppercase() && !containsCharType(optimization.toString(), UPPERCASE)) {
            optimization.append(UPPERCASE.charAt(secureRandom.nextInt(UPPERCASE.length())));
        }
        if (policy.requiresLowercase() && !containsCharType(optimization.toString(), LOWERCASE)) {
            optimization.append(LOWERCASE.charAt(secureRandom.nextInt(LOWERCASE.length())));
        }
        if (policy.requiresNumbers() && !containsCharType(optimization.toString(), NUMBERS)) {
            optimization.append(NUMBERS.charAt(secureRandom.nextInt(NUMBERS.length())));
        }
        if (policy.requiresSpecialChars() && !containsCharType(optimization.toString(), policy.getAllowedSpecialChars())) {
            String specialChars = policy.getAllowedSpecialChars().isEmpty() ? DEFAULT_SPECIAL : policy.getAllowedSpecialChars();
            optimization.append(specialChars.charAt(secureRandom.nextInt(specialChars.length())));
        }
        
        // Add random characters to reach desired length
        String pool = charPool.toString();
        int targetLength = Math.max(policy.getMinLength(), optimization.length() + 8);
        targetLength = Math.min(targetLength, policy.getMaxLength());
        
        while (optimization.length() < targetLength) {
            optimization.append(pool.charAt(secureRandom.nextInt(pool.length())));
        }
        
        // Shuffle everything except the kept part
        char[] passArray = optimization.toString().toCharArray();
        for (int i = passArray.length - 1; i > keepLength; i--) {
            int j = keepLength + secureRandom.nextInt(i + 1 - keepLength);
            char temp = passArray[i];
            passArray[i] = passArray[j];
            passArray[j] = temp;
        }
        
        String optimizedPassword = new String(passArray);
        double entropy = calculateEntropy(optimizedPassword);
        PasswordStrength strength = PasswordStrength.fromEntropy(entropy);
        
        Password result = new Password(optimizedPassword, entropy, strength);
        
        logger.debug("Password optimized: original_length={}, new_length={}, entropy={:.2f}", 
                   existingPassword.length(), result.getLength(), entropy);
        securityLogger.info("Password optimized: entropy_improvement={:.2f}, new_strength={}", 
                          entropy - calculateEntropy(existingPassword), strength);
        
        return result;
    }
    
    @Override
    public boolean validatePassword(String password) {
        return validatePassword(password, defaultPolicy);
    }
    
    @Override
    public boolean validatePassword(String password, PasswordPolicy policy) {
        if (password == null || policy == null) {
            return false;
        }
        
        // Check length requirements
        if (password.length() < policy.getMinLength() || password.length() > policy.getMaxLength()) {
            return false;
        }
        
        // Check character type requirements
        if (policy.requiresUppercase() && !containsCharType(password, UPPERCASE)) {
            return false;
        }
        if (policy.requiresLowercase() && !containsCharType(password, LOWERCASE)) {
            return false;
        }
        if (policy.requiresNumbers() && !containsCharType(password, NUMBERS)) {
            return false;
        }
        if (policy.requiresSpecialChars() && !containsCharType(password, policy.getAllowedSpecialChars())) {
            return false;
        }
        
        // Check entropy requirement
        double entropy = calculateEntropy(password);
        if (entropy < policy.getMinEntropy()) {
            return false;
        }
        
        // Check forbidden patterns
        return !containsForbiddenPattern(password, policy.getForbiddenPatterns());
    }
    
    @Override
    public double calculateEntropy(String password) {
        if (password == null || password.isEmpty()) {
            return 0.0;
        }
        
        // Determine character set size
        int charsetSize = 0;
        if (containsCharType(password, LOWERCASE)) charsetSize += 26;
        if (containsCharType(password, UPPERCASE)) charsetSize += 26;
        if (containsCharType(password, NUMBERS)) charsetSize += 10;
        if (containsSpecialChars(password)) charsetSize += countSpecialChars(password);
        
        // Calculate entropy: log2(charset_size^length)
        return password.length() * Math.log(charsetSize) / Math.log(2);
    }
    
    private boolean containsCharType(String password, String charSet) {
        return password.chars().anyMatch(c -> charSet.indexOf(c) >= 0);
    }
    
    private boolean containsSpecialChars(String password) {
        return password.chars().anyMatch(c -> 
            !Character.isLetterOrDigit(c) && !Character.isWhitespace(c));
    }
    
    private int countSpecialChars(String password) {
        Set<Character> specialChars = new HashSet<>();
        password.chars().forEach(c -> {
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                specialChars.add((char) c);
            }
        });
        return Math.max(specialChars.size(), 10); // Minimum assumption for entropy calculation
    }
    
    private boolean containsForbiddenPattern(String password, String[] forbiddenPatterns) {
        String lowerPassword = password.toLowerCase();
        return Arrays.stream(forbiddenPatterns)
                .anyMatch(pattern -> lowerPassword.contains(pattern.toLowerCase()));
    }
}