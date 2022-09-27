package com.Blackjack.com.Blackjack.carti;

public class Card {
    private Simbol simbol;
    private NumeCarte nume;
    private int valoare;
    private String valoareString;
    private boolean as = false;


    public boolean isAs() {
        return as;
    }

    public Card(Simbol simbol, NumeCarte nume) {
        this.simbol = simbol;
        this.nume = nume;
        setValoareDupaNume();
    }

    public int getValoare() {
        return valoare;
    }

    public void setValoare(int valoare) {
        this.valoare = valoare;
    }

    public String getValoareString() {
        return valoareString;
    }

    public void setValoareDupaNume() {
        switch (this.nume) {
            case DOI:
                this.valoare = 2;
                this.valoareString = "2";
                break;
            case TREI:
                this.valoare = 3;
                this.valoareString = "3";
                break;
            case PATRU:
                this.valoare = 4;
                this.valoareString = "4";
                break;
            case CINCI:
                this.valoare = 5;
                this.valoareString = "5";
                break;
            case SASE:
                this.valoare = 6;
                this.valoareString = "6";
                break;
            case SAPTE:
                this.valoare = 7;
                this.valoareString = "7";
                break;
            case OPT:
                this.valoare = 8;
                this.valoareString = "8";
                break;
            case NOUA:
                this.valoare = 9;
                this.valoareString = "9";
                break;
            case ZECE:
                this.valoare = 10;
                this.valoareString = "10";
                break;
            case VALET:
                this.valoare = 10;
                this.valoareString = "J";
                break;
            case DAMA:
                this.valoare = 10;
                this.valoareString = "Q";
                break;
            case POPA:
                this.valoare = 10;
                this.valoareString = "K";
                break;
            case AS:
                this.as = true;
                this.valoare = 11;
                this.valoareString = "A";
                break;
            default:
                break;
        }
    }

    public String getSimbolString() {
        String simbol = "";
        switch (this.simbol) {
            case ROMB:
                simbol = "♦";
                break;
            case TREFLA:
                simbol = "♣";
                break;
            case INIMA_NEAGRA:
                simbol = "♠";
                break;
            case INIMA_ROSIE:
                simbol = "♥";
                break;
            default:
                break;
        }
        return simbol;
    }


    public void afisareCarte() {
        System.out.print(getValoareString() + getSimbolString());
    }
}
