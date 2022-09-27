package com.Blackjack.com.Blackjack.joc;

import com.Blackjack.com.Blackjack.carti.InsuficienteCartiRamase;
import com.Blackjack.com.Blackjack.carti.PachetCarti;
import com.Blackjack.com.Blackjack.jucatori.Dealer;
import com.Blackjack.com.Blackjack.jucatori.Utilizator;
import com.Blackjack.com.Blackjack.carti.ZeroCartiRamaseException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ZeroCartiRamaseException {
        Scanner in = new Scanner(System.in);
        System.out.println("\nBine ati venit! Introduceti numele: ");
        String numeUtilizator = in.next();

        PachetCarti pachet = PachetCarti.getInstanta();
        pachet.amesteca();

        Utilizator utilizator = new Utilizator(numeUtilizator);
        Dealer dealer = new Dealer();

        while (utilizator.getBuget() > 0) {
            Runda runda = new Runda(utilizator, dealer);

            try {
                runda.startRunda();
                if (runda.incheieJoc)
                    return;
            } catch (InsuficienteCartiRamase ex) {
                System.out.println(ex.getMessage());
                return;
            }


            System.out.println("\n\nAU RAMAS " + pachet.getCarti().size() + " CARTI\n\n");

            if (utilizator.checkBlackJack()) {
                runda.utilizatorulAreBlackJack();
                continue;
            }

            boolean iese = false;
            while (utilizator.getBuget() > 0 && !iese && !runda.incheieJoc) {
                System.out.println("\nAlegeti urmatoarea actiune: ");
                System.out.println("H - HIT  |  S - STAND  | D - DUBLEAZA  | A - ABANDON  | X - INCHIDERE JOC");

                char raspuns = runda.raspunsCorect(in);
                switch (raspuns) {
                    case 'E':
                        return;
                    case 'A':
                        runda.utilizatorAbandon();
                        iese = true;
                        break;
                    case 'H':
                        try {
                            runda.utilizatorHit();
                        } catch (HitImposibilException | BustException ex) {
                            System.out.println(ex.getMessage());
                            iese = true;
                            break;
                        }
                        break;
                    case 'S':
                        runda.utilizatorStand();
                        iese = true;
                        break;
                    case 'D':
                        try {
                            runda.utilizatorDubleaza();
                        } catch (BustException ex) {
                            System.out.println(ex.getMessage());
                            iese = true;
                        } catch (DublareImposibilaException exc) {
                            System.out.println(exc.getMessage());
                        }
                        break;
                    case 'X':
                        runda.utilizatorExit();
                        return;
                }
            }
        }

    }

    private static class Runda {
        private final Utilizator utilizator;
        private final Dealer dealer;

        private boolean incheieJoc = false;
        private double pariuRunda;

        private int hitCount = 0;
        private boolean dublat = false;

        public Runda(Utilizator u, Dealer d) {
            utilizator = u;
            dealer = d;
        }

        private void trageDealerul() {
            while (dealer.sub16()) {
                try {
                    dealer.hit();
                    if (dealer.areAs()) {
                        dealer.setValoareAs();
                    }
                } catch (ZeroCartiRamaseException e) {

                    PachetCarti pachet = PachetCarti.getInstanta();
                    pachet.refacePachet();
                    trageDealerul();
                }
            }
        }

        /**
         * Intai verifica corectitudinea inputului (sa fie numar), apoi bugetul.
         */

        public boolean introducePariuValid(Scanner in) {

            int nrGreseliPariu = 0;
            double pariu = 0;
            boolean pariuValid = false;

            while (nrGreseliPariu < 5 && !pariuValid) {
                try {
                    System.out.println("\nCat doriti sa pariati?");
                    pariu = in.nextDouble();
                    pariuValid = true;
                } catch (InputMismatchException ex) {
                    String trash = in.nextLine();
                    System.out.println("\nVa rog introduceti un numar.");
                    nrGreseliPariu++;
                }
            }

            if (nrGreseliPariu == 5) {
                System.out.println("\nNu s-a inteles! Redeschideti jocul.");
                incheieJoc = true;
                return false;
            }

            int nrDepasesteBuget = 0;
            pariuValid = false;

            while (nrDepasesteBuget < 5 && !pariuValid && nrGreseliPariu < 5) {
                try {
                    utilizator.pariaza(pariu);
                    pariuValid = true;
                } catch (BugetInsuficientException ex) {
                    System.out.println(ex.getMessage());
                    nrDepasesteBuget++;
                    try {
                        pariu = in.nextDouble();
                    } catch (InputMismatchException exc) {
                        String trash = in.nextLine();
                        System.out.println("Va rog introduceti un numar.\nCat doriti sa pariati?");
                        nrGreseliPariu++;
                    }
                }
            }

            if (nrGreseliPariu == 5 || nrDepasesteBuget == 5) {
                System.out.println("Eroare! Redeschideti jocul.");
                incheieJoc = true;
                return false;
            }

            pariuRunda = pariu;
            return true;
        }

        private void initializeazaRunda() throws ZeroCartiRamaseException {
            utilizator.hit();
            utilizator.hit();
            if (utilizator.areAs()) {
                utilizator.setValoareAs();
            }
            dealer.hit();
            dealer.hit();
            if (dealer.areAs()) {
                dealer.setValoareAs();
            }
            afisareMaini();
        }

        public char raspunsCorect(Scanner in) {
            int nrGreseli = 0;
            char raspuns = in.next().charAt(0);
            boolean raspunsValid = false;
            String raspunsuriValide = "HSDAX";

            while (nrGreseli < 5 && !raspunsValid) {
                if (!raspunsuriValide.contains(Character.toString(raspuns))) {
                    System.out.println("Va rog introduceti un raspuns valid.");
                    System.out.println("H - HIT  |  S - STAND  | D - DUBLEAZA  | A - ABANDON  | X - INCHIDERE JOC");
                    raspuns = in.next().charAt(0);
                    nrGreseli++;
                } else raspunsValid = true;
            }

            if (nrGreseli == 5) {
                System.out.println("Eroare! Redeschideti jocul.");
                return 'E';
            } else return raspuns;
        }

        public void obtinePrimulRaspunsCorect(Scanner in) throws ZeroCartiRamaseException {
            char raspuns = in.next().charAt(0);
            int nrGreseli = 0;

            while (nrGreseli < 5) {
                if (raspuns != 'N' && raspuns != 'Y') {
                    System.out.println("Doriti sa continuati? Apasati Y pentru da, si N pentru nu.");
                    raspuns = in.next().charAt(0);
                    nrGreseli++;
                    continue;
                }

                if (raspuns == 'N') {
                    incheieJoc = true;
                    System.out.println("\nBuget final: " + utilizator.getBuget() + "\nLa revedere!\n");
                } else {
                    if (!introducePariuValid(in)) return;
                    initializeazaRunda();
                }
                return;
            }

            System.out.println("\nNu s-a inteles! Redeschideti jocul.");
            incheieJoc = true;
        }

        public void startRunda() throws InsuficienteCartiRamase, ZeroCartiRamaseException {
            PachetCarti pachet = PachetCarti.getInstanta();

            if (pachet.getCarti().size() < 4) {
                pachet.refacePachet();
            }
            Scanner in = new Scanner(System.in);

            System.out.println("\nRunda a inceput! Bugetul tau: " + utilizator.getBuget());
            System.out.println("\nDoriti sa continuati? (Y/N) ");

            obtinePrimulRaspunsCorect(in);
        }


        private void egalitate() {
            utilizator.egalitate();
            System.out.println("\nEgalitate! Runda se incheie. Buget: " + utilizator.getBuget());
        }

        private void castigaDealerul() {
            utilizator.pierde();
            System.out.println("\nGhinion! Runda pierduta. Bugetul ramas: " + utilizator.getBuget());
        }

        private void castigaUtilizatorul() {
            utilizator.castiga();
            System.out.println("\nFelicitari " + utilizator.getNume() + "! Ai castigat runda!");
        }

        public void incheieSiAfiseazaCastigator() {
            afisareFinalRunda();
            afisareMainiFinal();

            if (utilizator.calculeazaSumaCurenta() > dealer.calculeazaSumaCurenta() || dealer.calculeazaSumaCurenta() > 21) {
                castigaUtilizatorul();
            } else if (utilizator.calculeazaSumaCurenta() < dealer.calculeazaSumaCurenta()) {
                castigaDealerul();
            } else {
                egalitate();
            }

            dealer.manaNoua();
            utilizator.manaNoua();

            incheieJoc = true;
        }

        public void utilizatorAbandon() {
            afisareFinalRunda();
            System.out.println("Ati abandonat runda!");
            utilizator.abandoneaza();
            System.out.println("Buget curent: " + utilizator.getBuget());
        }

        public void utilizatorStand() {
            System.out.println("\nAsteptam sa traga dealerul.");
            trageDealerul();

            incheieSiAfiseazaCastigator();

        }

        public void utilizatorHit() throws HitImposibilException, BustException {
            System.out.println("\nTrageti o carte.");

            if (dublat) {
                throw new HitImposibilException("\nDeja ati dublat. Hit nepermis.");
            }

            try {
                utilizator.hit();
                if (utilizator.areAs()) {
                    utilizator.setValoareAs();
                }

                afisareUltimaCarteTrasa();
                hitCount++;
                afisareMaini();

                if (utilizator.calculeazaSumaCurenta() == 21) {
                    trageDealerul();
                    incheieSiAfiseazaCastigator();
                    incheieJoc = true;
                }

                if (utilizator.checkBust()) {
                    afisareFinalRunda();
                    afisareMainiFinal();
                    castigaDealerul();
                    dealer.manaNoua();
                    utilizator.manaNoua();
                    throw new BustException("\nAti depasit 21! Ati pierdut runda.");
                }

            } catch (ZeroCartiRamaseException ex) {
                PachetCarti pachet = PachetCarti.getInstanta();
                pachet.refacePachet();
                utilizatorHit();
            }
        }

        public void utilizatorDubleaza() throws DublareImposibilaException, BustException {
            if (hitCount != 0) {
                throw new DublareImposibilaException("\nDeja ati tras o carte. Dublare nepermisa.");
            }

            if (utilizator.getBuget() < 2 * pariuRunda) {
                throw new DublareImposibilaException("\nBuget insuficient!");
            }

            try {
                utilizator.dubleaza();
                if (utilizator.areAs()) {
                    utilizator.setValoareAs();
                }

                afisareUltimaCarteTrasa();
                dublat = true;
            } catch (ZeroCartiRamaseException ex) {
                PachetCarti pachet = PachetCarti.getInstanta();
                pachet.refacePachet();
                utilizatorDubleaza();
            }

            afisareMaini();

            if (utilizator.checkBust()) {
                afisareFinalRunda();
                afisareMainiFinal();
                castigaDealerul();
                dealer.manaNoua();
                utilizator.manaNoua();
                throw new BustException("\nAti depasit 21! Ati pierdut runda. Buget: " + utilizator.getBuget());
            } else {
                utilizatorStand();
            }

        }

        public void utilizatorExit() {
            System.out.println("Buget final: " + utilizator.getBuget());
            System.out.println("La revedere!");
            incheieJoc = true;
        }

        private void afisareMainiFinal() {
            System.out.println("MANA TA:");
            utilizator.afisareMana();

            System.out.println("MANA DEALERULUI:");
            dealer.afisareMana();
        }

        private void afisareMaini() {
            System.out.println("MANA TA:");
            utilizator.afisareMana();

            System.out.println("MANA DEALERULUI:");
            dealer.afisarePrimaCarte();
        }

        private void afisareUltimaCarteTrasa() {
            System.out.print("Ati tras |  ");
            int nrCartiCurente = utilizator.getMana().getCartiCurente().size();
            utilizator.getMana().getCartiCurente().get(nrCartiCurente - 1).afisareCarte();
            System.out.print("  |\n\n");
        }

        private void afisareFinalRunda() {
            System.out.println("\n――――――――――――――――――――――――――――――――――――\n" +
                    "                    FINAL RUNDA\n" +
                    "――――――――――――――――――――――――――――――――――――\n");
        }

        public void utilizatorulAreBlackJack() {
            afisareFinalRunda();
            afisareMainiFinal();

            if (dealer.checkBlackJack()) {
                egalitate();
            } else {
                castigaUtilizatorul();
                utilizator.blackjack();
            }
            dealer.manaNoua();
            utilizator.manaNoua();
        }


    }
}

