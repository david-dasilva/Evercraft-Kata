package net.daviddasilva;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

@Getter
public final class Character {
    public static final int NAT_20 = 20;
    public static final int BASE_DAMAGE = 1;
    public static final int XP_FOR_ATTACK = 10;
    public static final int LEVEL_THRESHOLD_XP = 1000;

    private final String name;
    private final Alignment alignment;
    private final int armorClass;
    private final EnumMap<Ability, AbilityScore> abilities;
    private int hitPoints;
    private long xp;
    private int level;


    public Character(String name, Alignment alignment, int armorClass, int hitPoints, Map<Ability, AbilityScore> abilities, int level) {
        this.name = name;
        this.alignment = alignment;
        this.armorClass = armorClass + abilities.get(Ability.DEXTERITY).getModifier();
        this.hitPoints = hitPoints + abilities.get(Ability.CONSTITUTION).getModifier();
        this.abilities = new EnumMap<>(abilities);
        this.level = level;
    }

    public static CharacterBuilder builder() {
        return new CharacterBuilder();
    }


    public boolean attemptAttack(Character opponent, int roll) {
        boolean attackSuccessful = this.attack(opponent, roll);
        if (attackSuccessful) {
            gainXP(XP_FOR_ATTACK);
        }
        return attackSuccessful;
    }

    private boolean attack(Character opponent, int roll) {
        if (roll == NAT_20) {
            opponent.takeCriticalHit(getStrengthModifier() * 2);
            return true;
        }
        boolean attackSuccessful = roll + getStrengthModifier() >= opponent.getArmorClass();
        if (attackSuccessful) {
            opponent.takeHit(getStrengthModifier());
        }
        return attackSuccessful;
    }

    public void takeHit(int opponentModifier) {
        int damageDealt = BASE_DAMAGE + opponentModifier;
        if (damageDealt <= 0) {
            damageDealt = BASE_DAMAGE;
        }
        this.hitPoints -= damageDealt;
    }

    public void takeCriticalHit(int opponentModifier) {
        int damageDealt = BASE_DAMAGE * 2 + opponentModifier;
        if (damageDealt <= 0) {
            damageDealt = BASE_DAMAGE;
        }
        this.hitPoints -= damageDealt;
    }

    public boolean isDead() {
        return this.hitPoints <= 0;
    }

    public int getStrengthModifier() {
        return this.abilities.get(Ability.STRENGTH).getModifier();
    }
    public int getDexterityModifier() {
        return this.abilities.get(Ability.DEXTERITY).getModifier();
    }
    public int getConstitutionModifier() {
        return this.abilities.get(Ability.CONSTITUTION).getModifier();
    }
    public int getWisdomModifier() {
        return this.abilities.get(Ability.WISDOM).getModifier();
    }
    public int getIntelligenceModifier() {
        return this.abilities.get(Ability.INTELLIGENCE).getModifier();
    }
    public int getCharismaModifier() {
        return this.abilities.get(Ability.CHARISMA).getModifier();
    }

    public long getCurrentXP() {
        return this.xp;
    }

    public void gainXP(long xp) {
        long previousXp = this.xp;
        this.xp += xp;
        int nextLevelThreshold = this.level * LEVEL_THRESHOLD_XP;
        if ((previousXp / nextLevelThreshold < 1) && (this.xp / nextLevelThreshold >= 1)) {
            this.levelUp();
        }
    }

    private void levelUp() {
        this.level += 1;
    }
    public int getLevel() {
        return level;
    }

}
