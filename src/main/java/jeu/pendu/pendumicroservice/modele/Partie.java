package jeu.pendu.pendumicroservice.modele;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Partie {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "nom_partie")
    private String nomPartie;

    @Getter
    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Getter
    @Column(name = "mot_secret")
    private String motSecret;

    @Getter
    @Column(name = "etat_mot")
    private String etatMot;

    @Getter
    @ElementCollection
    @CollectionTable(name = "lettres_devinees", joinColumns = @JoinColumn(name = "partie_id"))
    @Column(name = "lettre")
    private List<Character> lettresDevinees = new ArrayList<>();

    @Getter
    @ElementCollection
    @CollectionTable(name = "lettres_ratees", joinColumns = @JoinColumn(name = "partie_id"))
    @Column(name = "lettre")
    private List<Character> lettresRatees = new ArrayList<>();

    @Getter
    @Column(name = "erreurs")
    private int erreurs;

    @OneToMany(mappedBy = "partie", cascade = CascadeType.ALL, orphanRemoval = true)
    //@Column(name = "joueurs")
    private List<Joueur> joueurs = new ArrayList<>();

    @Getter
    @Column(name = "etat_Partie")
    private String etatPartie;

    @Getter
    @Column(name = "joueur_actuel_id")
    private Long joueurActuelId;

    @Transient
    public static final int MAX_ERREURS = 8;

    public Partie() {
    }

    public Partie(String motSecret, String nomPartie, String motDePasse) {
        this.nomPartie = nomPartie;
        this.motDePasse = motDePasse;
        this.motSecret = motSecret;
        this.etatMot = motSecret.replaceAll(".", "_");
        this.erreurs = 0;
        this.etatPartie = "En attente";
        this.joueurActuelId = null;
    }

    public void setLettresRatees(List<Character> lettresRatees) {
        this.lettresRatees = lettresRatees;
    }

    public void setJoueurActuelId(Long joueurActuelId) {
        this.joueurActuelId = joueurActuelId;
    }

    public void setNomPartie(String nomPartie) {
        this.nomPartie = nomPartie;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMotSecret(String motSecret) {
        this.motSecret = motSecret;
    }

    public void setEtatMot(String etatMot) {
        this.etatMot = etatMot;
    }

    public void setLettresDevinees(List<Character> lettresDevinees) {
        this.lettresDevinees = lettresDevinees;
    }

    public void setErreurs(int erreurs) {
        this.erreurs = erreurs;
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public void setJoueurs(List<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public void setEtatPartie(String etatPartie) {
        this.etatPartie = etatPartie;
    }
}
