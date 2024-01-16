package jeu.pendu.pendumicroservice.dtos;

import jeu.pendu.pendumicroservice.dtos.builder.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PartieDto {
    private Long id;
    private String etatMot;
    private int erreurs;
    private List<Character> lettresDevinees;
    private List<String> nomsJoueurs;
    private String etatPartie;
    private String nomPartie;
    private Long joueurActuelId;

    public PartieDto() {
    }

    public PartieDto(Builder builder) {
        // TODO document why this constructor is empty
    }

    public void setNomsJoueurs(List<String> nomsJoueurs) {
        this.nomsJoueurs = nomsJoueurs;
    }

    public void setJoueurActuelId(Long joueurActuelId) {
        this.joueurActuelId = joueurActuelId;
    }

    public void setNomPartie(String nomPartie) {
        this.nomPartie = nomPartie;
    }

    public void setEtatPartie(String etatPartie) {
        this.etatPartie = etatPartie;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEtatMot(String etatMot) {
        this.etatMot = etatMot;
    }

    public void setErreurs(int erreurs) {
        this.erreurs = erreurs;
    }

    public void setLettresDevinees(List<Character> lettresDevinees) {
        this.lettresDevinees = lettresDevinees;
    }
}
