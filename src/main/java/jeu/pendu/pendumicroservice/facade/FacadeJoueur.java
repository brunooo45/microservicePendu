package jeu.pendu.pendumicroservice.facade;

import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.exception.JoueurDejaExistantException;
import jeu.pendu.pendumicroservice.exception.JoueurInexistantException;
import jeu.pendu.pendumicroservice.modele.Joueur;
import jeu.pendu.pendumicroservice.services.JoueurService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FacadeJoueur implements IFacadeJoueur {

    private final JoueurService joueurService;

    public FacadeJoueur(JoueurService joueurService) {
        this.joueurService = joueurService;
    }

    @Override
    public JoueurDto creerJoueur(Joueur joueur) throws JoueurDejaExistantException, JoueurInexistantException {
        return joueurService.creerJoueur(joueur);
    }

    @Override
    public List<JoueurDto> getTousLesJoueurs() {
        return joueurService.getTousLesJoueurs();
    }

    @Override
    public JoueurDto getJoueur(Long id) throws JoueurInexistantException {
        return joueurService.getJoueur(id);
    }

    @Override
    public void deleteJoueur(Long id) throws JoueurInexistantException {
        joueurService.deleteJoueur(id);
    }
}
