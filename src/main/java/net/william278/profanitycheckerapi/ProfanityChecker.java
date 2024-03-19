/*
 * Copyright (c) 2022-2024 William278
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package net.william278.profanitycheckerapi;

import jep.Interpreter;
import jep.MainInterpreter;
import jep.SharedInterpreter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A class to check if a string contains profanity.
 * <p>
 * This uses <a href="https://github.com/ninia/jep">jep</a> to run <a href="https://pypi.org/project/alt-profanity-check/">alt-profanity-checker</a> to use machine learning to determine if a string of text contains profanity
 */
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ProfanityChecker implements AutoCloseable {

    /**
     * The jep interpreter
     */
    @Builder.Default
    private Interpreter interpreter = null;

    /**
     * The path to the jep library file.
     * <p>
     * See <a href="https://github.com/ninia/jep/wiki/FAQ#how-do-i-fix-unsatisfied-link-error-no-jep-in-javalibrarypath">
     * here</a> for help with this setting
     */
    @Nullable
    @Builder.Default
    private String libraryPath = null;

    /**
     * The normalizers to use
     */
    @NotNull
    @Builder.Default
    private List<Normalizer> normalizers = Normalizer.all();

    /**
     * Whether to use a threshold
     */
    @Builder.Default
    private boolean useThreshold = false;

    /**
     * The threshold to use
     */
    @Builder.Default
    private double threshold = 0.9d;

    @SuppressWarnings("unused")
    private ProfanityChecker(@Nullable Interpreter interpreter, @Nullable String libraryPath,
                             @NotNull List<Normalizer> normalizers, boolean useThreshold, double threshold) {
        this.interpreter = interpreter;
        this.libraryPath = libraryPath;
        this.normalizers = normalizers;
        this.useThreshold = useThreshold;
        this.threshold = threshold;
        this.initialize();
    }

    /**
     * Starts the <a href="https://github.com/ninia/jep">jep</a> interpreter and imports libraries by
     * initializing a new {@link SharedInterpreter}
     */
    public void initialize() {
        if (this.libraryPath != null) {
            MainInterpreter.setJepLibraryPath(this.libraryPath);
        }
        if (this.interpreter == null) {
            this.interpreter = new SharedInterpreter();
        }
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
        return useThreshold ? getProfanityProbability(normalized) >= threshold : containsProfanity(normalized);
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
