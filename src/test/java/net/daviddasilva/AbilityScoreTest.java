package net.daviddasilva;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenCode;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)

class AbilityScoreTest {

    @ParameterizedTest(name = "Ability score {0} gives a modifier of {1}")
    @CsvSource(textBlock = """
            1,-5
            2, -4
            3, -4
            4, -3
            5, -3
            6, -2
            7, -2
            8, -1
            9, -1
            10, 0
            11, 0
            12, 1
            13, 1
            14, 1
            15, 2
            16, 3
            17, 3
            18, 4
            19, 4
            20, 5
            """
    )
    void abilities_have_modifiers_according_to_table(int score, int expectedModifier) {
        // Given
        AbilityScore abilityScore = new AbilityScore(score);

        // When
        int modifier = abilityScore.getModifier();

        // Then
        then(modifier).isEqualTo(expectedModifier);
    }

    @ParameterizedTest(name = "Ability cannot have a value outside range : {0}")
    @ValueSource(ints = {0, 21})
    void abilities_score_between_1_and_20(int score) {
        // Given
        ThrowableAssert.ThrowingCallable code = () -> new AbilityScore(score);

        // Then
        thenCode(code).isInstanceOf(IllegalArgumentException.class).hasMessage("Ability score should be between 1 and 20");
    }
}