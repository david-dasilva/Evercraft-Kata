package net.daviddasilva;

import java.util.Map;

import static java.util.Map.entry;

public record AbilityScore(int score) {

    private static final int DEFAULT_SCORE = 10;
    private static final Map<Integer, Integer> MODIFIER_TABLE = Map.ofEntries(
            entry(1,-5),
            entry(2, -4),
            entry(3, -4),
            entry(4, -3),
            entry(5, -3),
            entry(6, -2),
            entry(7, -2),
            entry(8, -1),
            entry(9, -1),
            entry(10, 0),
            entry(11, 0),
            entry(12, 1),
            entry(13, 1),
            entry(14, 1),
            entry(15, 2),
            entry(16, 3),
            entry(17, 3),
            entry(18, 4),
            entry(19, 4),
            entry(20, 5)

    );

    public AbilityScore {
        if (score < 1 || score > 20) {
            throw new IllegalArgumentException("Ability score should be between 1 and 20");
        }
    }

    public AbilityScore() {
        this(DEFAULT_SCORE);
    }

    public int getModifier() {
        return MODIFIER_TABLE.get(this.score);
    }
}
