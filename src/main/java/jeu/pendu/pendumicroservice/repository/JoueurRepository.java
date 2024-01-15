package jeu.pendu.pendumicroservice.repository;


import jeu.pendu.pendumicroservice.modele.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoueurRepository extends JpaRepository<Joueur, Long> {

    Joueur findByNomJoueur(String nomJoueur);
}
