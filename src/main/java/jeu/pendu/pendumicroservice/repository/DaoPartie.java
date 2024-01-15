package jeu.pendu.pendumicroservice.repository;

import jeu.pendu.pendumicroservice.modele.Partie;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DaoPartie {

    private final PartieRepository partieRepository;

    public DaoPartie(PartieRepository partieRepository) {
        this.partieRepository = partieRepository;
    }



    public Partie creerPartie(Partie partie) {
        return partieRepository.save(partie);
    }


    public Partie getPartieById(Long id) {
        return partieRepository.getReferenceById(id);
    }

    public Optional<Partie> getPartieByIdOptional(Long id) {
        return partieRepository.findById(id);
    }


    public List<Partie> getToutesLesParties() {
        return partieRepository.findAll();
    }


    public Partie mettreAJourPartie(Partie partie) {
        return partieRepository.save(partie);
    }


    public void supprimerPartie(Long id) {
        partieRepository.deleteById(id);
    }

    public Optional<Partie> getPartieByNomPartie(String nomPartie) {
        return partieRepository.findByNomPartie(nomPartie);
    }

}
