package net.daviddasilva;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CharacterTest {

    @Test
    void can_set_a_name() {
        // Given
        var expectedName = "Krom le conquérant";
        // When
        var character = Character.builder().name(expectedName).build();
        // Then
        then(character.getName()).isEqualTo(expectedName);
    }


    @Test
    void can_set_alignment() {
        var character = Character.builder().alignment(Alignment.GOOD).build();
        then(character.getAlignment()).isInstanceOf(Alignment.class);
    }


    @Test
    void can_have_armor_class() {
        // Given
        var character = Character.builder().armorClass(15).build();
        // When

        // Then
        then(character.getArmorClass()).isEqualTo(15);
    }

    @Test
    void can_have_a_default_armor_class() {
        // Given
        var character = Character.builder().build();
        // When
        // Then
        then(character.getArmorClass()).isEqualTo(10);
    }


    @Test
    void can_have_hit_points() {
        // Given
        var character = Character.builder().hitPoints(10).build();
        // When
        // Then
        then(character.getHitPoints()).isEqualTo(10);
    }

    @Test
    void can_have_default_hit_points() {
        // Given
        var character = Character.builder().build();
        // When
        // Then
        then(character.getHitPoints()).isEqualTo(5);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 15})
    void attack_should_succeed_if_roll_higher_than_ac(int roll) {
        // Given
        var opponent = Character.builder().armorClass(10).build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attack(opponent, roll);

        // Then
        then(succeeded).isTrue();
    }

    @Test
    void attack_should_fail_if_roll_lower_than_ac() {
        // Given
        var opponent = Character.builder().armorClass(10).build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attack(opponent, 5);

        // Then
        then(succeeded).isFalse();
    }


    @Test
    void attack_always_succeed_on_roll_20() {
        // Given
        var opponent = Character.builder().armorClass(100).build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attack(opponent, 20);

        // Then
        then(succeeded).isTrue();
    }

    @Test
    void successful_attack_deals_damage() {
        // Given
        var opponent = Character.builder()
                                .armorClass(5)
                                .hitPoints(5)
                                .build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attack(opponent, 15);

        // Then
        then(succeeded).isTrue();
        then(opponent.getHitPoints()).isEqualTo(4);
    }

    @Test
    void critical_hit_deals_double_damage() {
        // Given
        var opponent = Character.builder()
                                .armorClass(5)
                                .hitPoints(5)
                                .build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attack(opponent, 20);

        // Then
        then(succeeded).isTrue();
        then(opponent.getHitPoints()).isEqualTo(3);
    }

    @Test
    void character_is_dead_if_hit_points_reach_zero() {
        // Given
        var player = Character.builder()
                              .hitPoints(1)
                              .build();
        // When
        player.takeHit();

        // Then
        then(player.isDead()).isTrue();
    }

    @Test
    void character_is_dead_if_hit_points_below_zero() {
        // Given
        var player = Character.builder()
                              .hitPoints(-1)
                              .build();
        // When

        // Then
        then(player.isDead()).isTrue();
    }




}
