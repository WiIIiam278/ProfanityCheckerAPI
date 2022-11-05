package net.william278.profanitycheckerapi;

import jep.Interpreter;
import jep.MainInterpreter;
import jep.SharedInterpreter;

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

}
