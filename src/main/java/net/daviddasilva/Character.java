package net.daviddasilva;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public final class Character {
    private String name;
    private Alignment alignment;
    @Builder.Default
    private int armorClass = 10;
    @Builder.Default
    private int hitPoints = 5;


    public boolean attack(Character opponent, int roll) {
        if (roll == 20) {
            return true;
        } else {
            return roll >= opponent.getArmorClass();
        }

    }



}
