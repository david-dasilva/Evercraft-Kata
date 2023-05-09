package net.daviddasilva;

import lombok.Builder;
import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

@Builder
@Data
public final class Character {

    private static final int DEFAULT_ABILITY_SCORE = 10;
    private static final int DEFAULT_ARMOR_CLASS = 10;
    private static final int DEFAULT_HIT_POINTS = 5;
    private static final Map<Ability, AbilityScore> DEFAULT_ABILITIES = Map.of(
            Ability.STRENGTH, new AbilityScore(),
            Ability.DEXTERITY, new AbilityScore(),
            Ability.CONSTITUTION, new AbilityScore(),
            Ability.WISDOM, new AbilityScore(),
            Ability.INTELLIGENCE, new AbilityScore(),
            Ability.CHARISMA, new AbilityScore()

    );

    private String name;
    private Alignment alignment;
    @Builder.Default
    private int armorClass = DEFAULT_ARMOR_CLASS;
    @Builder.Default
    private int hitPoints = DEFAULT_HIT_POINTS;
    @Builder.Default
    private EnumMap<Ability, AbilityScore> abilities = new EnumMap<>(DEFAULT_ABILITIES);



    public boolean attack(Character opponent, int roll) {
        boolean attackSuccessful;
        if (roll == 20) {
            attackSuccessful = true;
            opponent.takeHit();
        } else {
            attackSuccessful = roll >= opponent.getArmorClass();
        }
        if (attackSuccessful) {
            opponent.takeHit();
        }
        return attackSuccessful;
    }

    public void takeHit() {
        this.hitPoints -= 1;
    }

    public boolean isDead() {
        return this.hitPoints <= 0;
    }


}
