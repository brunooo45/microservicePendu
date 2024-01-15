package jeu.pendu.pendumicroservice.facade;

import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.dtos.PartieDto;
import jeu.pendu.pendumicroservice.exception.*;
import jeu.pendu.pendumicroservice.services.JoueurService;
import jeu.pendu.pendumicroservice.services.PartieService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FacadePartie implements IFacadePartie{

    private final PartieService partieService;
    private final JoueurService joueurService;

    public FacadePartie(PartieService partieService, JoueurService joueurService) {
        this.partieService = partieService;
        this.joueurService = joueurService;
    }

    @Override
    public PartieDto getPartie(Long id) throws PartieInexistanteException {
        return partieService.obtenirPartieById(id);
    }

    public PartieDto getPartieEnCours(Long joueurId) throws PartieInexistanteException {
        return partieService.obtenirToutesLesParties().stream()
                .filter(partie -> partie.getEtatPartie().equals("En cours"))
                .filter(partie -> {
                    try {
                        return partie.getNomsJoueurs().contains(joueurService.getJoueur(joueurId).getNomJoueur());
                    } catch (JoueurInexistantException e) {
                        throw new RuntimeException(e);
                    }
                })
                .findFirst()
                .orElseThrow(PartieInexistanteException::new);
    }

    @Override
    public List<PartieDto> getPartiesEnAttente() {
        return partieService.obtenirToutesLesParties().stream()
                .filter(partie -> partie.getEtatPartie().equals("En attente"))
                .collect(Collectors.toList());
    }


    @Override
    public List<PartieDto> getAllParties() {
        return partieService.obtenirToutesLesParties();
    }

    @Override
    public PartieDto creerPartie(String nomPartie, String nomJoueur) throws JoueurInexistantException {
        return partieService.creerPartie(nomPartie, nomJoueur);
    }

    @Override
    public PartieDto rejoindrePartie(String nomPartie, String motDePasse, String nomJoueur) throws JoueurInexistantException, PartieInexistanteException, MotDePasseIncorrectException {
        return partieService.rejoindrePartie(nomPartie, motDePasse, nomJoueur);
    }

    @Override
    public PartieDto lancePartie(Long partieId) throws PartieInexistanteException, NombredeJoueursIncorrectException {
        return partieService.lancerPartie(partieId);
    }

    @Override
    public PartieDto jouerTour(Long partieId, Long joueurId, String proposition) throws JoueurInexistantException, PartieInexistanteException, PasTonTourException {
        return partieService.tourJoueur(partieId, joueurId, proposition);
    }

    @Override
    public JoueurDto getVainqueur(Long partieId) throws PartieInexistanteException {
        return partieService.obtenirVainqueur(partieId);
    }

    @Override
    public void deletePartie(Long id) throws PartieInexistanteException {
        partieService.supprimerPartie(id);

    }
}
