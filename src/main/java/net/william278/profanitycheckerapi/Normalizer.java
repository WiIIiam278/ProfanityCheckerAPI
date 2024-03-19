/*
 * Copyright (c) 2022-2024 William278
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package net.william278.profanitycheckerapi;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

/**
 * Options for normalizing text, providing a normalization function for a string
 */
public enum Normalizer {

    /**
     * {@link java.text.Normalizer#normalize(CharSequence, java.text.Normalizer.Form) Normalize} using the {@link java.text.Normalizer.Form#NFKD NFKD} method
     */
    NFKD_NORMALIZER(
            (text) -> java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFKD)
                    .replaceAll("[^\\p{ASCII}]", "")
                    .toLowerCase()
                    .replaceAll("\\s{2,}", " ").trim()
    ),

    /**
     * Normalize by converting leet speak number patterns to their corresponding letters
     */
    LEET_CONVERTER(
            (text) -> text.replaceAll("1", "i")
                    .replaceAll("3", "e")
                    .replaceAll("4", "a")
                    .replaceAll("5", "s")
                    .replaceAll("7", "t")
                    .replaceAll("0", "o")
                    .replaceAll("9", "g")
                    .replaceAll("8", "b")
                    .replaceAll("6", "g")
    );
    @NotNull
    private final Function<String, String> function;

    Normalizer(@NotNull Function<String, String> function) {
        this.function = function;
    }

    /**
     * Normalize a string using this method
     *
     * @param input The string to normalize
     * @return The normalized string
     */
    @NotNull
    public String normalize(@NotNull String input) {
        return function.apply(input);
    }

    /**
     * Get the normalizers from a list of normalization options
     *
     * @return The normalizers
     */
    @NotNull
    public static List<Normalizer> all() {
        return List.of(values());
    }


}
