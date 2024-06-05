package com.servicedesk.usersservice.service;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|<>?";

    public static String generateRandomPassword() {
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String uppercaseLetters = lowercaseLetters.toUpperCase();
        String numbers = "0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder passwordBuilder = new StringBuilder();

        // Generate at least one of each character type
        passwordBuilder.append(getRandomCharacter(lowercaseLetters, random));
        passwordBuilder.append(getRandomCharacter(uppercaseLetters, random));
        passwordBuilder.append(getRandomCharacter(numbers, random));
        passwordBuilder.append(getRandomCharacter(SPECIAL_CHARACTERS, random));

        // Generate remaining characters
        int remainingLength = 8 - passwordBuilder.length();
        for (int i = 0; i < remainingLength; i++) {
            String allCharacters = lowercaseLetters + uppercaseLetters + numbers + SPECIAL_CHARACTERS;
            passwordBuilder.append(getRandomCharacter(allCharacters, random));
        }

        // Shuffle the password characters
        String password = passwordBuilder.toString();
        char[] passwordChars = password.toCharArray();
        for (int i = passwordChars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordChars[i];
            passwordChars[i] = passwordChars[j];
            passwordChars[j] = temp;
        }
        password = new String(passwordChars);

        return password;
    }

    private static char getRandomCharacter(String characters, SecureRandom random) {
        int index = random.nextInt(characters.length());
        return characters.charAt(index);
    }
}
