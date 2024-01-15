package jeu.pendu.pendumicroservice.facade;

import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.dtos.PartieDto;
import jeu.pendu.pendumicroservice.exception.*;

import java.util.List;

public interface IFacadePartie {
    PartieDto getPartie(Long id) throws PartieInexistanteException;
    PartieDto getPartieEnCours(Long joueurId) throws JoueurInexistantException, PartieInexistanteException;
    List<PartieDto> getPartiesEnAttente();
    List<PartieDto> getAllParties();
    PartieDto creerPartie(String nomPartie, String nomJoueur) throws JoueurInexistantException;
    PartieDto rejoindrePartie(String nomPartie, String motDePasse, String nomJoueur) throws JoueurInexistantException, PartieInexistanteException, MotDePasseIncorrectException;
    PartieDto lancePartie(Long partieId) throws JoueurInexistantException, PartieInexistanteException, NombredeJoueursIncorrectException;
    PartieDto jouerTour(Long partieId, Long joueurId, String proposition) throws JoueurInexistantException, PartieInexistanteException, PasTonTourException;
    JoueurDto getVainqueur(Long partieId) throws PartieInexistanteException;
    void deletePartie(Long id) throws PartieInexistanteException;
}
