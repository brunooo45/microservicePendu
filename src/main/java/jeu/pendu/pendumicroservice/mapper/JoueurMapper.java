package jeu.pendu.pendumicroservice.mapper;

import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.modele.Joueur;
import jeu.pendu.pendumicroservice.services.JoueurService;

public class JoueurMapper {

    public static JoueurDto toDto(Joueur joueur) {
        return new JoueurDto(joueur.getId(), joueur.getNomJoueur(),joueur.isTourJoueur(), joueur.getErreurs(), PartieMapper.toDto(joueur.getPartie()));
    }

    public static Joueur toEntity(JoueurDto joueurDto, JoueurService joueurService) {
        Joueur joueur = new Joueur();
        joueur.setId(joueurDto.getId());
        joueur.setNomJoueur(joueurDto.getNomJoueur());
        joueur.setTourJoueur(joueurDto.isTourJoueur());
        joueur.setErreurs(joueurDto.getErreurs());
        joueur.setPartie(PartieMapper.toEntity(joueurDto.getPartieDto(), joueurService));
        return joueur;
    }
}
