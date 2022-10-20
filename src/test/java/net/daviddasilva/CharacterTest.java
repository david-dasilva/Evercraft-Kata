package net.daviddasilva;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;

public class CharacterTest {

    @Test
    void canSetAName() {
        // Given
        var expectedName = "Krom le conquérant";
        // When
        var character = Character.builder().name(expectedName).build();
        // Then
        then(character.getName()).isEqualTo(expectedName);
    }


    @Test
    void canSetAlignment() {
        var character = Character.builder().alignment(Alignment.GOOD).build();
        then(character.getAlignment()).isInstanceOf(Alignment.class);
    }


    @Test
    void canHaveArmorClass() {
        // Given
        var character = Character.builder().armorClass(15).build();
        // When

        // Then
        then(character.getArmorClass()).isEqualTo(15);
    }

    @Test
    void canHaveADefaultArmorClass() {
        // Given
        var character = Character.builder().build();
        // When
        // Then
        then(character.getArmorClass()).isEqualTo(10);
    }


    @Test
    void canHaveHitPoints() {
        // Given
        var character = Character.builder().hitPoints(10).build();
        // When
        // Then
        then(character.getHitPoints()).isEqualTo(10);
    }

    @Test
    void canHaveDefaultHitPoints() {
        // Given
        var character = Character.builder().build();
        // When
        // Then
        then(character.getHitPoints()).isEqualTo(5);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 15})
    void attackShouldSucceedIfRollHigherThanAC(int roll) {
        // Given
        var opponent = Character.builder().armorClass(10).build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attack(opponent, roll);

        // Then
        then(succeeded).isTrue();
    }

    @Test
    void attackShouldFailIfRollLowerThanAC() {
        // Given
        var opponent = Character.builder().armorClass(10).build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attack(opponent, 5);

        // Then
        then(succeeded).isFalse();
    }


    @Test
    void attackAlwaysSucceedOnRoll20() {
        // Given
        var opponent = Character.builder().armorClass(100).build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attack(opponent, 20);

        // Then
        then(succeeded).isTrue();
    }




}
