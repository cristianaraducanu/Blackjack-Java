package com.Blackjack.jucatori;

import com.Blackjack.joc.Mana;
import com.Blackjack.carti.Card;
import com.Blackjack.carti.PachetCarti;
import com.Blackjack.carti.ZeroCartiRamaseException;

public class Jucator {
    protected Mana mana;
    private boolean as;

    public boolean areAs() {
        return as;
    }
    public Mana getMana() {
        return mana;
    }


    public Card hit() throws ZeroCartiRamaseException {
        PachetCarti pachet = PachetCarti.getInstanta();

        try {
            Card carteTrasa = mana.adaugaCarte(pachet.trageCarte());
            if(carteTrasa.isAs())
                as = true;
            return carteTrasa;
        }
        catch (ZeroCartiRamaseException ex) {
            throw new ZeroCartiRamaseException("Nu mai sunt carti! Incheiem runda, apoi jocul.");
        }

    }

    public double calculeazaSumaCurenta() {
        return mana.calculeazaSumaCurenta();
    }

    public boolean checkBlackJack() {
        return (mana.getCartiCurente().size() == 2 && calculeazaSumaCurenta() == 21);
    }

    public boolean checkBust() {
        return (calculeazaSumaCurenta() > 21);
    }

    public void manaNoua() {
        mana = new Mana();
    }

    public void afisareMana() {
        int total = 0;
        System.out.print("|  ");
        for (Card carte : mana.getCartiCurente()) {
            carte.afisareCarte();
            System.out.print("  |  ");
            total += carte.getValoare();
        }
        System.out.println("SUMA CURENTA: " + total + "\n");
    }

    public void setValoareAs() {
        if (calculeazaSumaCurenta() > 21) {
            for (Card carte : mana.getCartiCurente()) {
                if (carte.isAs()) {
                    carte.setValoare(1);
                }
                if (calculeazaSumaCurenta() <= 21) break;
            }
        }
    }
}
