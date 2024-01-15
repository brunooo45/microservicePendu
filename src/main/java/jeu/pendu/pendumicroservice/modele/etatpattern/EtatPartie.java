package jeu.pendu.pendumicroservice.modele.etatpattern;

import jeu.pendu.pendumicroservice.modele.Partie;

public interface EtatPartie {
    void jouerTour(Partie partie, char lettre);
    void devinerMot(Partie partie, String mot);
    void demarrerPartie(Partie partie);
    void terminerPartie(Partie partie);
    void commencerTourJoueur(Partie partie);
}
