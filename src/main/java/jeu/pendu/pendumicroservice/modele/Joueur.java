package jeu.pendu.pendumicroservice.modele;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
public class Joueur {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "nom_Joueur")
    private String nomJoueur;

    @ManyToOne
    @JoinColumn(name = "partie_id")
    private Partie partie; // La partie à laquelle ce joueur appartient

    @Getter
    @Column(name = "tour_Joueur")
    private boolean tourJoueur;

    @Getter
    @Column(name = "erreurs")
    private int erreurs = 0;



    public Joueur() {
        // Constructeur sans paramètre pour JPA
    }

    public Joueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
        this.tourJoueur = false;
    }

    public void setErreurs(int erreurs) {
        this.erreurs = erreurs;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public Partie getPartie() {
        return partie;
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    public void setTourJoueur(boolean tourJoueur) {
        this.tourJoueur = tourJoueur;
    }
}
