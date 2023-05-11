package net.daviddasilva;

import java.util.EnumMap;
import java.util.Map;

public class CharacterBuilder {

    private static final int DEFAULT_ARMOR_CLASS = 10;
    private static final int DEFAULT_HIT_POINTS = 5;
    private static final int DEFAULT_LEVEL = 1;
    private static final int MINIMAL_HIT_POINTS = 1;
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
    private int armorClass = DEFAULT_ARMOR_CLASS;
    private int hitPoints = DEFAULT_HIT_POINTS;
    private final EnumMap<Ability, AbilityScore> abilities;
    private int level = DEFAULT_LEVEL;

    CharacterBuilder() {
        this.abilities = new EnumMap<>(DEFAULT_ABILITIES);
    }

    public CharacterBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CharacterBuilder alignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public CharacterBuilder armorClass(int armorClass) {
        this.armorClass = armorClass;
        return this;
    }

    public CharacterBuilder hitPoints(int hitPoints) {
        this.hitPoints = hitPoints > 0 ? hitPoints : MINIMAL_HIT_POINTS;
        return this;
    }

    public CharacterBuilder strength(int score) {
        this.abilities.put(Ability.STRENGTH, new AbilityScore(score));
        return this;
    }

    public CharacterBuilder dexterity(int score) {
        this.abilities.put(Ability.DEXTERITY, new AbilityScore(score));
        return this;
    }

    public CharacterBuilder constitution(int score) {
        this.abilities.put(Ability.CONSTITUTION, new AbilityScore(score));
        return this;
    }

    public CharacterBuilder wisdom(int score) {
        this.abilities.put(Ability.WISDOM, new AbilityScore(score));
        return this;
    }

    public CharacterBuilder intelligence(int score) {
        this.abilities.put(Ability.INTELLIGENCE, new AbilityScore(score));
        return this;
    }

    public CharacterBuilder charisma(int score) {
        this.abilities.put(Ability.CHARISMA, new AbilityScore(score));
        return this;
    }

    public CharacterBuilder level(int level) {
        this.level = level;
        return this;
    }

    public Hero build() {
        return new Hero(this.name, this.alignment, armorClass, hitPoints, abilities, level);
    }
}
