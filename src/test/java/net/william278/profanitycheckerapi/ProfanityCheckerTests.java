package net.william278.profanitycheckerapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProfanityCheckerTests {

    private static final ProfanityChecker checker = new ProfanityChecker();

    @Test
    public void givenSentenceContainingProfanity_testIsProfane() {
        Assertions.assertTrue(checker.isTextProfane("This is a fucking test sentence"));
        Assertions.assertTrue(checker.isTextProfaneBypass("_54848498niggakedsamda"));
        Assertions.assertTrue(checker.isTextProfaneBypass("Sh1tface"));
        Assertions.assertTrue(checker.isTextProfaneBypass("Twat"));
        Assertions.assertTrue(checker.isTextProfaneBypass("Cunt"));
        Assertions.assertTrue(checker.isTextProfaneBypass("n1gga"));
        Assertions.assertTrue(checker.isTextProfaneBypass("nnnnnnnnnnnnnnnnnnnnnnn1gggggggggggggggggaaaaaaaaaaaaaa56446465495"));
        System.out.println(checker.getTextProfanityLikelihoodBypass("nnnnnnnnnnnnnnnnnnnnnnn1gggggggggggggggggaaaaaaaaaaaaaa56446465495"));
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
