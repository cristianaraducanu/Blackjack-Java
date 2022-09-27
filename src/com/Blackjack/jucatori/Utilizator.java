package com.Blackjack.jucatori;

import com.Blackjack.joc.BugetInsuficientException;
import com.Blackjack.joc.Mana;
import com.Blackjack.carti.ZeroCartiRamaseException;

public class Utilizator extends Jucator {
    private final String nume;

    private double buget;
    private double pariuCurent;
    private boolean exit = false;
    public Utilizator(String nume) {
        this.mana = new Mana();
        this.buget = 500;
        this.pariuCurent = 0;
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }

    public double getBuget() {
        return buget;
    }

    public double getPariuCurent() {
        return pariuCurent;
    }

    public void setPariuCurent(double pariuCurent) {
        this.pariuCurent = pariuCurent;
    }

    public void pierde() {
        buget -= pariuCurent;
        pariuCurent = 0;
    }

    public void castiga() {
        buget += pariuCurent;
        pariuCurent = 0;
    }

    public void abandoneaza() {
        buget -= pariuCurent / 2;
        pariuCurent = 0;
        manaNoua();
    }

    public void egalitate() {
        pariuCurent = 0;
    }

    public void blackjack() {
        buget += 1.5 * pariuCurent;
        pariuCurent = 0;
        manaNoua();

        System.out.println("!!!  BLACKJACK   !!!");
        System.out.println("Felicitari! Ai castigat runda! Buget curent: " + buget);
    }

    public void pariaza (double pariu) throws BugetInsuficientException {
        if (pariu > buget) {
            throw new BugetInsuficientException("Ati depasit bugetul! Alegeti o suma mai mica de " + buget + ".");
        }
        else pariuCurent = pariu;
    }

    public void dubleaza() throws ZeroCartiRamaseException {
        hit();
        pariuCurent *= 2;
    }

    public void exit() {
        exit = true;
    }
}
