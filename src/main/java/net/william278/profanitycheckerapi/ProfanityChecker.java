package net.william278.profanitycheckerapi;

import jep.Interpreter;
import jep.SharedInterpreter;

/**
 * Uses <a href="https://github.com/ninia/jep">jep</a> to run <a href="https://pypi.org/project/alt-profanity-check/">alt-profanity-checker</a> to use machine learning to determine if a string of text contains profanity
 */
public class ProfanityChecker {

    /**
     * The jep {@link Interpreter} used to execute the python script
     */
    private final Interpreter interpreter;

    /**
     * Fetches a new instance of the profanity checker class and initialises the interpreter
     */
    public ProfanityChecker() {
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
        return interpreter.getValue("predict([text])[0]", Integer.class) == 1;
    }

    /**
     * Returns a {@code double} value (0 to 1 inclusive) indicating the probability that the machine learning algorithm thinks the string contains profanity
     *
     * @param text The string of text to check
     * @return A {@code double} ranging between 0 and 1 inclusive that indicates the likelihood the string of text contains profanity
     */
    public double getTextProfanityLikelihood(String text) {
        interpreter.set("text", text);
        return interpreter.getValue("predict_prob([text])[0]", Double.class);
    }
}
