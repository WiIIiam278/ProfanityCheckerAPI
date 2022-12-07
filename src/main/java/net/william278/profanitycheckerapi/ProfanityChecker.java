package net.william278.profanitycheckerapi;

import jep.Interpreter;
import jep.MainInterpreter;
import jep.SharedInterpreter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A class to check if a string contains profanity.
 * <p>
 * This uses <a href="https://github.com/ninia/jep">jep</a> to run <a href="https://pypi.org/project/alt-profanity-check/">alt-profanity-checker</a> to use machine learning to determine if a string of text contains profanity
 * <p>
 * Get a builder instance using {@link ProfanityChecker#builder()}
 */
@SuppressWarnings("unused")
public class ProfanityChecker implements AutoCloseable {

    /**
     * The jep {@link Interpreter} used to execute the python script
     */
    private Interpreter interpreter;

    /**
     * The path to the jep library file
     */
    @Nullable
    private final String libraryPath;

    /**
     * The normalizers to use
     */
    @NotNull
    private final List<Normalizer> normalizers;

    /**
     * Whether to use a threshold
     */
    private final boolean useThreshold;

    /**
     * The threshold to use
     */
    private final double threshold;

    /**
     * Create a new ProfanityChecker instance and initialize the interpreter.
     *
     * @param libraryPath  The path to the jep library file
     * @param normalizers  The normalizers to use
     * @param useThreshold Whether to use a threshold
     * @param threshold    The threshold to use
     * @see #builder() {@code #builder()} to get a {@link ProfanityCheckerBuilder} instance
     */
    protected ProfanityChecker(@Nullable String libraryPath, @NotNull List<Normalizer> normalizers,
                               boolean useThreshold, double threshold) {
        this.libraryPath = libraryPath;
        this.normalizers = normalizers;
        this.useThreshold = useThreshold;
        this.threshold = threshold;
        this.initialize();
    }

    /**
     * Get a builder for a ProfanityChecker instance
     *
     * @return A {@link ProfanityCheckerBuilder} instance
     */
    @NotNull
    public static ProfanityCheckerBuilder builder() {
        return new ProfanityCheckerBuilder();
    }

    /**
     * Starts the <a href="https://github.com/ninia/jep">jep</a> interpreter and imports libraries by initializing a new {@link SharedInterpreter}
     */
    private void initialize() {
        if (this.libraryPath != null) {
            MainInterpreter.setJepLibraryPath(this.libraryPath);
        }
        this.interpreter = new SharedInterpreter();
        this.interpreter.exec("from profanity_check import predict_prob, predict");
    }

    /**
     * Check if a string contains profanity using the configured checker.
     * <p>
     * If a threshold was set through the builder, this will return true if the likelihood of the string
     * containing profanity is greater than the {@link #threshold}
     *
     * @param text The text to check
     * @return {@code true} if the text contains profanity, {@code false} otherwise
     */
    public boolean isProfane(@NotNull String text) {
        final String normalized = normalizeText(text);
        return useThreshold ? getProfanityProbability(text) >= threshold : containsProfanity(text);
    }

    /**
     * Determines whether a string of text contains profanity, automatically
     *
     * @param text The string of text to check
     * @return {@code true} if the text likely contains profanity; {@code false} otherwise
     */
    protected boolean containsProfanity(@NotNull String text) {
        this.interpreter.set("text", text);
        return this.interpreter.getValue("predict([text])[0].item()", Integer.class) == 1;
    }

    /**
     * Returns a {@code double} value (0 to 1 inclusive) indicating the probability that the machine learning algorithm thinks the string contains profanity
     *
     * @param text The string of text to check
     * @return A {@code double} ranging between 0 and 1 inclusive that indicates the likelihood the string of text contains profanity
     */
    protected double getProfanityProbability(@NotNull String text) {
        this.interpreter.set("text", text);
        return this.interpreter.getValue("predict_prob([text])[0].item()", Double.class);
    }

    /**
     * Normalize a string of text
     *
     * @param text The string of text to normalize
     * @return The normalized string of text
     */
    @NotNull
    private String normalizeText(@NotNull String text) {
        String normalized = text;
        for (Normalizer normalizer : normalizers) {
            normalized = normalizer.normalize(normalized);
        }
        return normalized;
    }

    /**
     * Close the interpreter
     */
    @Override
    public void close() {
        if (this.interpreter != null) {
            this.interpreter.close();
        }
    }
}
