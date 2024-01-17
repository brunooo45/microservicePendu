package jeu.pendu.pendumicroservice.repository;

import jakarta.transaction.Transactional;
import jeu.pendu.pendumicroservice.modele.Joueur;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DaoJoueur {

    private final JoueurRepository joueurRepository;

    public DaoJoueur(JoueurRepository joueurRepository) {
        this.joueurRepository = joueurRepository;
    }

    @Transactional
    public Joueur creerJoueur(Joueur joueur) {
        return joueurRepository.save(joueur);
    }

    @Transactional
    public Joueur mettreAJour(Joueur joueur) {
        return joueurRepository.save(joueur);
    }

    public List<Joueur> getTousLesJoueurs() {
        return joueurRepository.findAll();
    }

    public Joueur getJoueur(Long id){
        return joueurRepository.getReferenceById(id);
    }

    public Joueur findByNomJoueur(String nomJoueur){
        return joueurRepository.findByNomJoueur(nomJoueur);
    }

    @Transactional
    public void deleteJoueur(Long id) {
        joueurRepository.deleteById(id);
    }
}
