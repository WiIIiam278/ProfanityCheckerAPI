package net.william278.profanitycheckerapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProfanityCheckerTests {

    private static final ProfanityChecker checker = new ProfanityChecker();

    @Test
    public void givenSentenceContainingProfanity_testIsProfane() {
        Assertions.assertTrue(checker.isTextProfane("This is a fucking test sentence"));
        Assertions.assertTrue(checker.isTextProfane("_54848498niggakedsamda"));
        Assertions.assertTrue(checker.isTextProfane("Sh1tface"));
        Assertions.assertTrue(checker.isTextProfane("Twat"));
        Assertions.assertTrue(checker.isTextProfane("Cunt"));
        Assertions.assertTrue(checker.isTextProfane("n1gga"));
        Assertions.assertTrue(checker.isTextProfane("nnnnnnnnnnnnnnnnnnnnnnn1gggggggggggggggggaaaaaaaaaaaaaa56446465495"));
    }

    @Test
    public void givenProfaneWord_testIsProfane() {
        Assertions.assertTrue(checker.isTextProfane("fuck"));
    }

    @Test
    public void givenProfaneAcronym_testIsProfane() {
        Assertions.assertTrue(checker.isTextProfane("wtf"));
    }

    @Test
    public void givenProfaneWord_testProfanityLikelihoodThreshold() {
        Assertions.assertTrue(checker.getTextProfanityLikelihood("fuck") > 0.9d);
    }

}
