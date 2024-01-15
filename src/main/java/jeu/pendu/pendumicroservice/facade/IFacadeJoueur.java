package jeu.pendu.pendumicroservice.facade;

import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.exception.JoueurDejaExistantException;
import jeu.pendu.pendumicroservice.exception.JoueurInexistantException;
import jeu.pendu.pendumicroservice.modele.Joueur;

import java.util.List;

public interface IFacadeJoueur {
    JoueurDto creerJoueur(Joueur joueur) throws JoueurDejaExistantException, JoueurInexistantException;
    List<JoueurDto> getTousLesJoueurs();
    JoueurDto getJoueur(Long id) throws JoueurInexistantException;
    void deleteJoueur(Long id) throws JoueurInexistantException;
}
