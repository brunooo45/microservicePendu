package jeu.pendu.pendumicroservice.services;

import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.exception.JoueurDejaExistantException;
import jeu.pendu.pendumicroservice.exception.JoueurInexistantException;
import jeu.pendu.pendumicroservice.mapper.JoueurMapper;
import jeu.pendu.pendumicroservice.modele.Joueur;
import jeu.pendu.pendumicroservice.repository.DaoJoueur;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JoueurService {

    private final DaoJoueur daoJoueur;

    public JoueurService(DaoJoueur daoJoueur) {
        this.daoJoueur = daoJoueur;
    }

    public List<JoueurDto> getTousLesJoueurs() {
        return daoJoueur.getTousLesJoueurs().stream()
                .map(JoueurMapper::toDto)
                .collect(Collectors.toList());
    }

    public JoueurDto creerJoueur(Joueur joueur) throws JoueurDejaExistantException, JoueurInexistantException {
        if (joueurExisteParNom(joueur.getNomJoueur()) || joueurExisteParId(joueur.getId())) {
            throw new JoueurDejaExistantException();
        }
        Joueur joueurCree = daoJoueur.creerJoueur(joueur);
        return JoueurMapper.toDto(joueurCree);
    }

    private boolean joueurExisteParNom(String nomJoueur) {
        return getTousLesJoueurs().stream()
                .anyMatch(j -> j.getNomJoueur().equalsIgnoreCase(nomJoueur));
    }

    private boolean joueurExisteParId(Long id) throws JoueurInexistantException {
        return id != null && getTousLesJoueurs().stream()
                .anyMatch(j -> j.getId().equals(id));
    }

    public JoueurDto getJoueur(Long id) throws JoueurInexistantException {
        Joueur joueur = daoJoueur.getJoueur(id);
        if (joueur == null) {
            throw new JoueurInexistantException();
        }
        return JoueurMapper.toDto(joueur);
    }

    public void deleteJoueur(Long id) throws JoueurInexistantException {
        try {
            daoJoueur.deleteJoueur(id);
        } catch (EmptyResultDataAccessException e) {
            throw new JoueurInexistantException();
        }
    }
    public JoueurDto getJoueurParNom(String nomJoueur) throws JoueurInexistantException {
        Joueur joueur = daoJoueur.findByNomJoueur(nomJoueur);
        if (joueur == null) {
            throw new JoueurInexistantException();
        }
        return JoueurMapper.toDto(joueur);
    }
}
