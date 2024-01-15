package jeu.pendu.pendumicroservice.dtos.builder;

import jeu.pendu.pendumicroservice.dtos.PartieDto;

import java.util.List;

public class Builder {
    private Long id;
    private String etatMot;
    private int erreurs;
    private List<Character> lettresDevinees;
    private List<String> nomsJoueurs;
    private String etatPartie;
    private String nomPartie;
    private Long joueurActuelId;

    public Builder id(Long id) {
        this.id = id;
        return this;
    }

    public Builder etatMot(String etatMot) {
        this.etatMot = etatMot;
        return this;
    }

    public Builder erreurs(int erreurs) {
        this.erreurs = erreurs;
        return this;
    }

    public Builder lettresDevinees(List<Character> lettresDevinees) {
        this.lettresDevinees = lettresDevinees;
        return this;
    }

    public Builder nomsJoueurs(List<String> nomsJoueurs) {
        this.nomsJoueurs = nomsJoueurs;
        return this;
    }

    public Builder etatPartie(String etatPartie) {
        this.etatPartie = etatPartie;
        return this;
    }

    public Builder nomPartie(String nomPartie) {
        this.nomPartie = nomPartie;
        return this;
    }

    public Builder joueurActuelId(Long joueurActuelId) {
        this.joueurActuelId = joueurActuelId;
        return this;
    }



    public PartieDto build() {
        return new PartieDto(this);
    }
}

