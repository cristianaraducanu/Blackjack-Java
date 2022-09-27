package com.Blackjack.com.Blackjack.carti;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PachetCarti {
    private static final PachetCarti instanta = new PachetCarti();

    private List<Card> carti;

    private boolean gol = false;


    /**
     * Initializeaza un pachet clasic de carti
     */

    public PachetCarti() {
        this.carti = new ArrayList<>();

        for (Simbol simbol : Simbol.values()) {
            for (NumeCarte carte : NumeCarte.values()) {
                this.carti.add(new Card(simbol, carte));
            }
        }
    }

    public List<Card> getCarti() {
        return carti;
    }

    public static PachetCarti getInstanta() {
        return instanta;
    }

    public boolean eGol() {
        return gol;
    }

    public void amesteca() {
        Collections.shuffle(carti);
    }

    public Card trageCarte() throws ZeroCartiRamaseException {
        try {
            return carti.remove(0);
        }
        catch (IndexOutOfBoundsException ex) {
            throw new ZeroCartiRamaseException("Nu mai sunt carti! Incheiem runda, apoi jocul.");
        }
    }

    public void cartiRamase() {
        System.out.println("Au ramas " + carti.size() + " carti.");
    }

    public void pachetGol() { gol = true; }

    public void refacePachet() {
        carti = new ArrayList<>();

        for (Simbol simbol : Simbol.values()) {
            for (NumeCarte carte : NumeCarte.values()) {
                this.carti.add(new Card(simbol, carte));
            }
        }

        amesteca();
    }
}
