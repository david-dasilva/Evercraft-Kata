package net.daviddasilva;

import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.EnumMap;

import static java.util.Map.entry;
import static org.assertj.core.api.BDDAssertions.then;

@DisplayName("A Hero")
@ExtendWith(SoftAssertionsExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HeroTest {

    @Test
    void can_set_a_name() {
        // Given
        var expectedName = "Krom le conquérant";
        // When
        var hero = Hero.builder().name(expectedName).build();
        // Then
        then(hero.getName()).isEqualTo(expectedName);
    }


    @Test
    void can_set_alignment() {
        var hero = Hero.builder().alignment(Alignment.GOOD).build();
        then(hero.getAlignment()).isInstanceOf(Alignment.class);
    }


    @Test
    void can_have_armor_class() {
        // Given
        var hero = Hero.builder().armorClass(15).build();
        // When

        // Then
        then(hero.getArmorClass()).isEqualTo(15);
    }

    @Test
    void can_have_a_default_armor_class() {
        // Given
        var hero = Hero.builder().build();
        // When
        // Then
        then(hero.getArmorClass()).isEqualTo(10);
    }


    @Test
    void can_have_hit_points() {
        // Given
        var hero = Hero.builder().hitPoints(10).build();
        // When
        // Then
        then(hero.getHitPoints()).isEqualTo(10);
    }

    @Test
    void has_5_hit_points_by_default() {
        // Given
        var hero = Hero.builder().build();
        // When
        // Then
        then(hero.getHitPoints()).isEqualTo(5);
    }

    @ParameterizedTest
    @ValueSource(ints = { 10, 15})
    void attack_should_succeed_if_roll_higher_than_ac(int roll) {
        // Given
        var opponent = Hero.builder().armorClass(10).build();
        var player = Hero.builder().build();

        // When
        boolean succeeded = player.attemptAttack(opponent, roll);

        // Then
        then(succeeded).isTrue();
    }

    @Test
    void attack_should_fail_if_roll_lower_than_ac() {
        // Given
        var opponent = Hero.builder().armorClass(10).build();
        var player = Hero.builder().build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 5);

        // Then
        then(succeeded).isFalse();
    }


    @Test
    void attack_always_succeed_on_roll_20() {
        // Given
        var opponent = Hero.builder().armorClass(100).build();
        var player = Hero.builder().build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 20);

        // Then
        then(succeeded).isTrue();
    }

    @Test
    void successful_attack_deals_damage(BDDSoftAssertions softly) {
        // Given
        var opponent = Hero.builder()
                           .armorClass(5)
                           .hitPoints(5)
                           .build();
        var player = Hero.builder().build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 15);

        // Then
        softly.then(succeeded).isTrue();
        softly.then(opponent.getHitPoints()).isEqualTo(4);
    }

    @Test
    void critical_hit_deals_double_damage(BDDSoftAssertions softly) {
        // Given
        var opponent = Hero.builder()
                           .armorClass(5)
                           .hitPoints(5)
                           .build();
        var player = Hero.builder().build();

        // When
        boolean succeeded = player.attemptAttack(opponent, 20);

        // Then
        softly.then(succeeded).isTrue();
        softly.then(opponent.getHitPoints()).isEqualTo(3);
    }

    @Test
    void hero_is_dead_if_hit_points_reach_zero() {
        // Given
        var player = Hero.builder()
                         .hitPoints(1)
                         .build();
        // When
        player.takeHit(0);

        // Then
        then(player.isDead()).isTrue();
    }

    @Test
    void is_dead_if_hit_points_below_zero() {
        // Given
        var player = Hero.builder()
                         .hitPoints(1)
                         .build();
        // When
        player.takeHit(2);

        // Then
        then(player.isDead()).isTrue();
    }

    @Test
    void hero_has_default_abilities() {
        // Given
        var player = Hero.builder().build();

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
        var player = Hero.builder()
                         .strength(15)
                         .build();

        var opponent = Hero.builder()
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
        var player = Hero.builder().strength(5)
                         .build();

        var opponent = Hero.builder()
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
        var player = Hero.builder()
                         .strength(1)
                         .build();

        var opponent = Hero.builder()
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
        var player = Hero.builder()
                         .strength(1)
                         .build();

        var opponent = Hero.builder()
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
        var player = Hero.builder()
                         .strength(15)
                         .build();

        var opponent = Hero.builder()
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
        var player = Hero.builder()
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
        var player = Hero.builder()
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
        var player = Hero.builder().build();
        var opponent = Hero.builder().build();

        // When
        boolean attackSuccessful = player.attemptAttack(opponent, 15);

        // Then
        softly.then(attackSuccessful).isTrue();
        softly.then(player.getCurrentXP()).isEqualTo(10);
    }

    @Test
    void should_not_gain_experience_when_attack_fails(BDDSoftAssertions softly) {
        // Given
        var player = Hero.builder().build();
        var opponent = Hero.builder().build();

        // When
        boolean attackSuccessful = player.attemptAttack(opponent, 1);

        // Then
        softly.then(attackSuccessful).isFalse();
        softly.then(player.getCurrentXP()).isZero();
    }

    @Test
    void should_be_level_1_by_default() {
        // Given
        var player = Hero.builder().build();

        // When
        int level = player.getLevel();

        // Then
        then(level).isOne();
    }

    @ParameterizedTest(name = "so gaining {0} xp should get to level {1}")
    @CsvSource(textBlock = """
            0,1
            1000,2
            2500,3
            5000, 6
            """)
    void should_gain_one_level_every_1000xp(int xp, int expectedLevel) {
        // Given
        var player = Hero.builder().build();
        var dummy = Hero.builder()
                        .armorClass(1)
                        .hitPoints(9999)
                        .build();

        // When
        while(player.getXp() < xp) {
            player.attemptAttack(dummy, 15);
        }

        // Then
        then(player.getLevel()).isEqualTo(expectedLevel);
    }

    @ParameterizedTest(name = "at level {0} with 15 CON and 10 starting HP has {1} total HP")
    @CsvSource(textBlock = """
            1, 12
            2, 19
            3, 26
            """)
    void hero_gains_hitPoints_when_leveling_up(int level, int expectedHitPoints) {
        // Given
        var player = Hero.builder()
                         .level(level)
                         .hitPoints(10)
                         .constitution(15) // +2
                         .build();

        // Then
        then(player.getHitPoints()).isEqualTo(expectedHitPoints);
    }

    @ParameterizedTest(name = "at level {0} the bonus is +{1}")
    @CsvSource(textBlock = """
            1, 0
            2, 1
            3, 1
            4, 2
            5, 2
            6, 3
            7, 3
            """)
    void hero_gains_a_bonus_on_attack_rolls_at_even_levels(int level, int expectedBonus){
        //Given
        var player = Hero.builder()
                         .level(level)
                         .build();

        // The AC increases with the player level so it becomes more difficult to hit
        var dummy = Hero.builder()
                        .armorClass(10 + expectedBonus)
                        .build();

        // When
        boolean attackSucceeded = player.attemptAttack(dummy, 10);

        // Then
        then(attackSucceeded).isTrue();

    }


}
