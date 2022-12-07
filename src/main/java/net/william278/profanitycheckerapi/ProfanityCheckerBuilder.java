package net.william278.profanitycheckerapi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A builder for a {@link ProfanityChecker} instance
 */
@SuppressWarnings("unused")
public class ProfanityCheckerBuilder {

    @Nullable
    private String libraryPath;
    @NotNull
    private List<Normalizer> normalizers = Normalizer.all();
    private boolean useThreshold = false;
    private double threshold = 0.9d;

    /**
     * Get a new profanity checker builder (see {@link ProfanityChecker#builder()})
     */
    protected ProfanityCheckerBuilder() {
    }

    /**
     * Set the path to the jep library file
     *
     * @param libraryPath The path to the library file
     * @return The ProfanityCheckerBuilder instance
     * @see <a href="https://github.com/ninia/jep/wiki/FAQ#how-do-i-fix-unsatisfied-link-error-no-jep-in-javalibrarypath">
     * * How to fix "Unsatisfied Link Error: no jep in java.library.path"</a>
     */
    @NotNull
    public ProfanityCheckerBuilder withLibraryPath(String libraryPath) {
        this.libraryPath = libraryPath;
        return this;
    }

    /**
     * Set the checker to use a threshold for the likelihood of a word being profane
     *
     * @param threshold The threshold
     * @return The ProfanityCheckerBuilder instance
     */
    @NotNull
    public ProfanityCheckerBuilder withThresholdChecking(double threshold) {
        this.useThreshold = true;
        this.threshold = threshold;
        return this;
    }

    /**
     * Set the checker to automatically determine if text is profane
     *
     * @return The ProfanityCheckerBuilder instance
     */
    @NotNull
    public ProfanityCheckerBuilder withAutomaticChecking() {
        this.useThreshold = false;
        return this;
    }

    /**
     * Set the normalizers to use
     *
     * @param normalizers The normalizers to use
     * @return The ProfanityCheckerBuilder instance
     */
    @NotNull
    public ProfanityCheckerBuilder withNormalizers(@NotNull Normalizer... normalizers) {
        this.normalizers = Arrays.asList(normalizers);
        return this;
    }

    /**
     * Build a new {@link ProfanityChecker} instance
     *
     * @return The ProfanityChecker instance
     */
    @NotNull
    public ProfanityChecker build() {
        return new ProfanityChecker(libraryPath, normalizers, useThreshold, threshold);
    }


}
