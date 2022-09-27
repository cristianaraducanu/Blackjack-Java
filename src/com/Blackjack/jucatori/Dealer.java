package com.Blackjack.jucatori;

import com.Blackjack.joc.Mana;

public class Dealer extends Jucator {
    private boolean blackjack = false;


    public Dealer() {
        mana = new Mana();
    }


    public void setBlackjack(boolean blackjack) {
        this.blackjack = blackjack;
    }


    public boolean sub16() {
        return (mana.calculeazaSumaCurenta() <= 16);
    }

    public void afisarePrimaCarte() {
        System.out.print("|  ");
        mana.getCartiCurente().get(0).afisareCarte();
        System.out.println("  |");
    }
}
