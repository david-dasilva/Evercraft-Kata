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
