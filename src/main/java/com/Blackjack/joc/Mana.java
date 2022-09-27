package com.Blackjack.com.Blackjack.joc;

import com.Blackjack.com.Blackjack.carti.Card;

import java.util.ArrayList;
import java.util.List;

public class Mana {
    private List<Card> cartiCurente;

    public Mana() {
        this.cartiCurente = new ArrayList<>();
    }

    public List<Card> getCartiCurente() {
        return cartiCurente;
    }

    public Card adaugaCarte(Card carte) {
        cartiCurente.add(carte);
        return carte;
    }

    public double calculeazaSumaCurenta() {
        double suma = 0;
        for (Card card : cartiCurente) {
            suma += card.getValoare();
        }
        return suma;
    }

}
