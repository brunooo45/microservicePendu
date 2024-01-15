package jeu.pendu.pendumicroservice.mapper;

import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.modele.Joueur;

public class JoueurMapper {

    public static JoueurDto toDto(Joueur joueur) {
        return new JoueurDto(joueur.getId(), joueur.getNomJoueur(),joueur.isTourJoueur(), joueur.getErreurs());
    }

    public static Joueur toEntity(JoueurDto joueurDto) {
        Joueur joueur = new Joueur();
        joueur.setId(joueurDto.getId());
        joueur.setNomJoueur(joueurDto.getNomJoueur());
        joueur.setTourJoueur(joueurDto.isTourJoueur());
        joueur.setErreurs(joueurDto.getErreurs());
        return joueur;
    }
}
