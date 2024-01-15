package jeu.pendu.pendumicroservice.repository;

import jeu.pendu.pendumicroservice.modele.Partie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartieRepository extends JpaRepository<Partie, Long> {
    Optional<Partie> findByNomPartie(String nomPartie);
}
