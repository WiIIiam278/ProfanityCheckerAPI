package net.william278.profanitycheckerapi;

import jep.Interpreter;
import jep.MainInterpreter;
import jep.SharedInterpreter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class to check if a string contains profanity.
 * <p>
 * This uses <a href="https://github.com/ninia/jep">jep</a> to run <a href="https://pypi.org/project/alt-profanity-check/">alt-profanity-checker</a> to use machine learning to determine if a string of text contains profanity
 */
@SuppressWarnings("unused")
public class ProfanityChecker implements AutoCloseable {

    /**
     * The jep {@link Interpreter} used to execute the python script
     */
    private Interpreter interpreter;

    /**
     * Create a new ProfanityChecker instance and initialize the interpreter, with a specified jep library path
     *
     * @param libraryPath File path of the jep library
     * @see <a href="https://github.com/ninia/jep/wiki/FAQ#how-do-i-fix-unsatisfied-link-error-no-jep-in-javalibrarypath">
     * How to fix "Unsatisfied Link Error: no jep in java.library.path"</a>
     */
    public ProfanityChecker(String libraryPath) {
        MainInterpreter.setJepLibraryPath(libraryPath);
        initialize();
    }

    /**
     * Create a new ProfanityChecker instance and initialize the interpreter
     */
    public ProfanityChecker() {
        initialize();
    }

    /**
     * Starts the <a href="https://github.com/ninia/jep">jep</a> interpreter and imports libraries by initializing a new {@link SharedInterpreter}
     */
    private void initialize() {
        interpreter = new SharedInterpreter();
        interpreter.exec("from profanity_check import predict_prob, predict");
    }

    /**
     * Determines whether a string of text contains profanity
     *
     * @param text The string of text to check
     * @return {@code true} if the text likely contains profanity; {@code false} otherwise
     */
    public boolean isTextProfane(String text) {
        interpreter.set("text", text);
        return interpreter.getValue("predict([text])[0].item()", Integer.class) == 1;
    }

    /**
     * Returns a {@code double} value (0 to 1 inclusive) indicating the probability that the machine learning algorithm thinks the string contains profanity
     *
     * @param text The string of text to check
     * @return A {@code double} ranging between 0 and 1 inclusive that indicates the likelihood the string of text contains profanity
     */
    public double getTextProfanityLikelihood(String text) {
        interpreter.set("text", text);
        return interpreter.getValue("predict_prob([text])[0].item()", Double.class);
    }

    /**
     * Determines whether a string of text contains profanity
     *
     * @param text The string of text to check
     * @return {@code true} if the text likely contains profanity; {@code false} otherwise
     */
    public boolean isTextProfaneBypass(String text) {
        return isTextProfaneBypassTimed(text, Long.MAX_VALUE);
    }

    /**
     * Returns a {@code double} value (0 to 1 inclusive) indicating the probability that the machine learning algorithm thinks the string contains profanity
     *
     * @param text The string of text to check
     * @return A {@code double} ranging between 0 and 1 inclusive that indicates the likelihood the string of text contains profanity
     */
    public double getTextProfanityLikelihoodBypass(String text) {
        return getTextProfanityLikelihoodBypassTimed(text, Long.MAX_VALUE);
    }

    /**
     * Determines whether a string of text contains profanity
     *
     * @param text The string of text to check timeLimitMilli
     * @param timeLimitMilli The time limit in milliseconds
     * @return {@code true} if the text likely contains profanity; {@code false} otherwise
     */
    public boolean isTextProfaneBypassTimed(String text, long timeLimitMilli) {
        Set<String> toCheck = getTextToCheck(text);
        long startTime = System.currentTimeMillis();
        for (String s : toCheck) {
            boolean result = isTextProfane(s);
            if (result) {
                return true;
            }
            if (System.currentTimeMillis() - startTime > timeLimitMilli) {
                return false;
            }
        }
        return false;
    }

    /**
     * Returns a {@code double} value (0 to 1 inclusive) indicating the probability that the machine learning algorithm thinks the string contains profanity
     *
     * @param text The string of text to check
     * @param timeLimitMilli The time limit in milliseconds
     * @return A {@code double} ranging between 0 and 1 inclusive that indicates the likelihood the string of text contains profanity
     */
    public double getTextProfanityLikelihoodBypassTimed(String text, long timeLimitMilli) {
        Set<String> toCheck = getTextToCheck(text);
        double highest = 0;
        long startTime = System.currentTimeMillis();
        for (String s : toCheck) {
            double result = getTextProfanityLikelihood(s);
            if (result > highest) {
                highest = result;
            }
            if (System.currentTimeMillis() - startTime > timeLimitMilli) {
                return highest;
            }
        }
        return highest;
    }

    /**
     * Safely dispose of the ProfanityChecker by closing the jep interpreter
     *
     * @throws IllegalStateException if the interpreter was not properly initialized
     */
    @Override
    public void close() throws IllegalStateException {
        if (interpreter != null) {
            interpreter.close();
        } else {
            throw new IllegalStateException("The jep interpreter was not initialized");
        }
    }

    /**
     * Safely dispose of the ProfanityChecker by closing the jep interpreter
     *
     * @throws IllegalStateException if the interpreter was not properly initialized
     * @deprecated Use {@link #close()}
     */
    @Deprecated(since = "1.1", forRemoval = true)
    public void dispose() throws IllegalStateException {
        close();
    }

    private Set<String> getTextToCheck(String text) {
        Set<String> toCheck = new HashSet<>();
        toCheck.add(text);
        toCheck.addAll(addSubStringsBasedOnSpace(toCheck));
        toCheck.addAll(removeNumbers(toCheck));
        toCheck.addAll(removeLetters(toCheck));
        toCheck.addAll(removeLettersAndNumbers(toCheck));
        toCheck.addAll(addSpaces(toCheck));
        toCheck.addAll(addSubStringsBasedOnSpace(toCheck));
        toCheck.addAll(convertNumbersToLetters(toCheck));
        toCheck.addAll(removeRepeatingCharacters(toCheck));
        toCheck.removeIf(String::isEmpty);
        return toCheck;
    }

    /**
     * Adds all substrings of the given strings that are separated by a space
     *
     * @param input The strings to add substrings to
     * @return A set of strings with substrings added
     */
    public Set<String> addSubStringsBasedOnSpace(Set<String> input) {
        Set<String> output = new HashSet<>();
        for (String s : input) {
            output.addAll(List.of(s.split(" ")));
        }
        return output;
    }

    /**
     * Removes all numbers from the given strings
     *
     * @param input The strings to remove number from
     * @return A set of strings with numbers removed
     */
    public Set<String> removeNumbers(Set<String> input) {
        Set<String> output = new HashSet<>();
        for (String s : input) {
            output.add(s.replaceAll("[^a-zA-Z]", ""));
        }
        return output;
    }

    /**
     * Removes all letters from the given strings
     *
     * @param input The strings to remove letters from
     * @return A set of strings with letters removed
     */
    public Set<String> removeLetters(Set<String> input) {
        Set<String> output = new HashSet<>();
        for (String s : input) {
            output.add(s.replaceAll("[^0-9]", ""));
        }
        return output;
    }

    /**
     * Removes all letters and numbers from the given strings
     *
     * @param input The strings to remove letters and numbers from
     * @return A set of strings with letters and numbers removed
     */
    public Set<String> removeLettersAndNumbers(Set<String> input) {
        Set<String> output = new HashSet<>();
        for (String s : input) {
            output.add(s.replaceAll("[^a-zA-Z0-9]", ""));
        }
        return output;
    }

    /**
     * Adds spaces between all characters in the given strings
     *
     * @param input The strings to add spaces to
     * @return A set of strings with spaces added
     */
    public Set<String> addSpaces(Set<String> input) {
        Set<String> output = new HashSet<>();
        for (String s : input) {
            if (s.length() > 1) {
                for (int i = 0; i < s.length() - 1; i++) {
                    output.add(s.substring(0, i) + " " + s.substring(i));
                }
            }
        }
        return output;
    }

    /**
     * Converts all numbers in the given strings to letters
     *
     * @param input The strings to convert numbers to letters
     * @return A set of strings with numbers converted to letters
     */
    public Set<String> convertNumbersToLetters(Set<String> input) {
        Set<String> output = new HashSet<>();
        for (String s : input) {
            output.add(s.replaceAll("0", "o")
                    .replaceAll("1", "i")
                    .replaceAll("3", "e")
                    .replaceAll("4", "a")
                    .replaceAll("5", "s")
                    .replaceAll("7", "t")
                    .replaceAll("8", "b")
                    .replaceAll("9", "g"));
        }
        return output;
    }

    /**
     * Removes all repeating characters in the given strings
     *
     * @param input The strings to remove repeating characters from
     * @return A set of strings with repeating characters removed
     */
    public Set<String> removeRepeatingCharacters(Set<String> input) {
        Set<String> output = new HashSet<>();
        for (String s : input) {
            StringBuilder sb = new StringBuilder();
            char lastChar = ' ';
            for (char c : s.toCharArray()) {
                if (c != lastChar) {
                    sb.append(c);
                    lastChar = c;
                }
            }
            output.add(sb.toString());
        }
        return output;
    }
}
