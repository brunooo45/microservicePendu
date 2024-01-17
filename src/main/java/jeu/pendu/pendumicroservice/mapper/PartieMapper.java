package jeu.pendu.pendumicroservice.mapper;

import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.dtos.PartieDto;
import jeu.pendu.pendumicroservice.exception.JoueurInexistantException;
import jeu.pendu.pendumicroservice.modele.Joueur;
import jeu.pendu.pendumicroservice.modele.Partie;
import jeu.pendu.pendumicroservice.services.JoueurService;

import java.util.List;
import java.util.stream.Collectors;

public class PartieMapper {

    public static PartieDto toDto(Partie partie) {
        if (partie == null) {
            return null;
        }

        PartieDto dto = new PartieDto();
        dto.setId(partie.getId());
        dto.setEtatMot(partie.getEtatMot());
        dto.setErreurs(partie.getErreurs());
        dto.setLettresDevinees(partie.getLettresDevinees());
        dto.setEtatPartie(partie.getEtatPartie());
        dto.setNomPartie(partie.getNomPartie());
        dto.setJoueurActuelId(partie.getJoueurActuelId());
        dto.setLettresRatees(partie.getLettresRatees());
        dto.setNomsJoueurs(partie.getJoueurs().stream()
                .map(Joueur::getNomJoueur)
                .collect(Collectors.toList()));
        return dto;
    }

    public static Partie toEntity(PartieDto dto, JoueurService joueurService) {
        if (dto == null) {
            return null;
        }

        Partie partie = new Partie();
        partie.setNomPartie(dto.getNomPartie());
        partie.setId(dto.getId());
        partie.setEtatMot(dto.getEtatMot());
        partie.setErreurs(dto.getErreurs());
        partie.setLettresDevinees(dto.getLettresDevinees());
        partie.setEtatPartie(dto.getEtatPartie());
        partie.setLettresRatees(dto.getLettresRatees());
        partie.setJoueurActuelId(dto.getJoueurActuelId());
        List<Joueur> joueurs = dto.getNomsJoueurs().stream()
                .map(nomJoueur -> {
                    try {
                        JoueurDto joueurDto = joueurService.getJoueurParNom(nomJoueur);
                        return JoueurMapper.toEntity(joueurDto,joueurService);
                    } catch (JoueurInexistantException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        partie.setJoueurs(joueurs);
        return partie;
    }
}
