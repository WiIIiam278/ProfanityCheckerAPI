/*
 * Copyright (c) 2022-2024 William278
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package net.william278.profanitycheckerapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProfanityCheckerTests {

    private static final ProfanityChecker checker = ProfanityChecker.builder().build();

    @Test
    public void givenSentencesWithProfanity_testIsProfane() {
        Assertions.assertTrue(checker.isProfane("This is a fucking test sentence"));
        Assertions.assertTrue(checker.isProfane("You stupid 5h1t"));
    }

    @Test
    public void givenSentencesWithoutProfanity_testIsNotProfane() {
        Assertions.assertFalse(checker.isProfane("This is a test sentence"));
        Assertions.assertFalse(checker.isProfane("You are a good person"));
    }

    @Test
    public void givenLeetPhrasesWithoutProfanity_testIsNotProfane() {
        Assertions.assertFalse(checker.isProfane("AHOJ"));
        Assertions.assertFalse(checker.isProfane("Hello"));
    }

    @Test
    public void givenScunthorpe_testIsNotProfane() {
        Assertions.assertFalse(checker.isProfane("Scunthorpe"));
    }

    @Test
    public void givenProfaneWord_testIsProfane() {
        Assertions.assertTrue(checker.isProfane("fuck"));
    }

    @Test
    public void givenProfaneAcronym_testIsProfane() {
        Assertions.assertTrue(checker.isProfane("wtf"));
    }

    @Test
    public void givenProfaneWord_testProfanityProbabilityChecker() {
        Assertions.assertTrue(checker.getProfanityProbability("fuck") > 0.9d);
    }

}
