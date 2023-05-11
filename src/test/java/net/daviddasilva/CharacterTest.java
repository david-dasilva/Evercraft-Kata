package net.daviddasilva;

import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.EnumMap;

import static java.util.Map.entry;
import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SoftAssertionsExtension.class)
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
        boolean succeeded = player.attemptAttack(opponent, roll);

        // Then
        then(succeeded).isTrue();
    }

    @Test
    void attack_should_fail_if_roll_lower_than_ac() {
        // Given
        var opponent = Character.builder().armorClass(10).build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 5);

        // Then
        then(succeeded).isFalse();
    }


    @Test
    void attack_always_succeed_on_roll_20() {
        // Given
        var opponent = Character.builder().armorClass(100).build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 20);

        // Then
        then(succeeded).isTrue();
    }

    @Test
    void successful_attack_deals_damage(BDDSoftAssertions softly) {
        // Given
        var opponent = Character.builder()
                                .armorClass(5)
                                .hitPoints(5)
                                .build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 15);

        // Then
        softly.then(succeeded).isTrue();
        softly.then(opponent.getHitPoints()).isEqualTo(4);
    }

    @Test
    void critical_hit_deals_double_damage(BDDSoftAssertions softly) {
        // Given
        var opponent = Character.builder()
                                .armorClass(5)
                                .hitPoints(5)
                                .build();
        var player = Character.builder().build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 20);

        // Then
        softly.then(succeeded).isTrue();
        softly.then(opponent.getHitPoints()).isEqualTo(3);
    }

    @Test
    void character_is_dead_if_hit_points_reach_zero() {
        // Given
        var player = Character.builder()
                              .hitPoints(1)
                              .build();
        // When
        player.takeHit(0);

        // Then
        then(player.isDead()).isTrue();
    }

    @Test
    void character_is_dead_if_hit_points_below_zero() {
        // Given
        var player = Character.builder()
                              .hitPoints(1)
                              .build();
        // When
        player.takeHit(2);

        // Then
        then(player.isDead()).isTrue();
    }

    @Test
    void character_has_default_abilities() {
        // Given
        var player = Character.builder().build();

        // When
        EnumMap<Ability, AbilityScore> abilities = player.getAbilities();

        // Then
        then(abilities).containsOnly(
                entry(Ability.STRENGTH, new AbilityScore(10)),
                entry(Ability.DEXTERITY, new AbilityScore(10)),
                entry(Ability.CONSTITUTION, new AbilityScore(10)),
                entry(Ability.WISDOM, new AbilityScore(10)),
                entry(Ability.INTELLIGENCE, new AbilityScore(10)),
                entry(Ability.CHARISMA, new AbilityScore(10))
                );
    }

    @Test
    void strength_modifier_is_applied_to_attack_and_damage_dealt(BDDSoftAssertions softly) {
        // Given
        var player = Character.builder()
                              .strength(15)
                              .build();

        var opponent = Character.builder()
                                .hitPoints(5)
                                .armorClass(10)
                                .build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 8);

        // Then
        softly.then(succeeded).isTrue();
        softly.then(opponent.getHitPoints()).isEqualTo(2);
    }

    @Test
    void strength_modifier_is_applied_to_attack_and_damage_dealt_case_negative(BDDSoftAssertions softly) {
        // Given
        var player = Character.builder().strength(5)
                              .build();

        var opponent = Character.builder()
                                .hitPoints(5)
                                .armorClass(10)
                                .build();

        // When
        boolean firstAttackHit = player.attemptAttack(opponent, 10);
        boolean secondAttackHit = player.attemptAttack(opponent, 15);

        // Then
        softly.then(firstAttackHit).isFalse();
        softly.then(secondAttackHit).isTrue();
        softly.then(opponent.getHitPoints()).isEqualTo(4);
    }

    @Test
    void minimum_damage_is_always_one(BDDSoftAssertions softly) {
        // Given
        var player = Character.builder()
                              .strength(1)
                              .build();

        var opponent = Character.builder()
                                .hitPoints(5)
                                .armorClass(10)
                                .build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 19);

        // Then
        softly.then(succeeded).isTrue();
        softly.then(opponent.getHitPoints()).isEqualTo(4);
    }

    @Test
    void minimum_damage_is_always_one_even_critical(BDDSoftAssertions softly) {
        // Given
        var player = Character.builder()
                              .strength(1)
                              .build();

        var opponent = Character.builder()
                                .hitPoints(5)
                                .armorClass(10)
                                .build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 20);

        // Then
        softly.then(succeeded).isTrue();
        softly.then(opponent.getHitPoints()).isEqualTo(4);
    }

    @Test
    void strength_modifier_is_doubled_on_critical_hit(BDDSoftAssertions softly) {
        // Given
        var player = Character.builder()
                              .strength(15)
                              .build();

        var opponent = Character.builder()
                                .hitPoints(10)
                                .armorClass(10)
                                .build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 20);

        // Then
        softly.then(succeeded).isTrue();
        softly.then(opponent.getHitPoints()).isEqualTo(4);
    }

    @Test
    void dexterity_modifier_is_added_to_armor_class() {
        // Given
        var player = Character.builder()
                              .armorClass(10)
                              .dexterity(15)
                              .build();
        // When
        int armorClass = player.getArmorClass();

        // Then
        then(armorClass).isEqualTo(12);
    }

    @Test
    void constitution_modifier_is_added_to_hit_points() {
        // Given
        var player = Character.builder()
                .hitPoints(10)
                .constitution(15)
                .build();
        // When
        int hitPoints = player.getHitPoints();

        // Then
        then(hitPoints).isEqualTo(12);
    }

    @Test
    void should_gain_experience_when_attack_landed(BDDSoftAssertions softly) {
        // Given
        var player = Character.builder().build();
        var opponent = Character.builder().build();

        // When
        boolean attackSuccessful = player.attemptAttack(opponent, 15);

        // Then
        softly.then(attackSuccessful).isTrue();
        softly.then(player.getCurrentXP()).isEqualTo(10);
    }

    @Test
    void should_not_gain_experience_when_attack_fails(BDDSoftAssertions softly) {
        // Given
        var player = Character.builder().build();
        var opponent = Character.builder().build();

        // When
        boolean attackSuccessful = player.attemptAttack(opponent, 1);

        // Then
        softly.then(attackSuccessful).isFalse();
        softly.then(player.getCurrentXP()).isZero();
    }


}
