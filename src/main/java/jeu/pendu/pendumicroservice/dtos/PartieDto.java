package jeu.pendu.pendumicroservice.dtos;

import jeu.pendu.pendumicroservice.dtos.builder.Builder;

import java.util.List;

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

    public Long getJoueurActuelId() {
        return joueurActuelId;
    }

    public void setJoueurActuelId(Long joueurActuelId) {
        this.joueurActuelId = joueurActuelId;
    }

    public String getNomPartie() {
        return nomPartie;
    }

    public void setNomPartie(String nomPartie) {
        this.nomPartie = nomPartie;
    }

    public String getEtatPartie() {
        return etatPartie;
    }

    public void setEtatPartie(String etatPartie) {
        this.etatPartie = etatPartie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtatMot() {
        return etatMot;
    }

    public void setEtatMot(String etatMot) {
        this.etatMot = etatMot;
    }

    public int getErreurs() {
        return erreurs;
    }

    public void setErreurs(int erreurs) {
        this.erreurs = erreurs;
    }

    public List<Character> getLettresDevinees() {
        return lettresDevinees;
    }

    public void setLettresDevinees(List<Character> lettresDevinees) {
        this.lettresDevinees = lettresDevinees;
    }

    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    public void setNomsJoueurs(List<String> nomsJoueurs) {
        this.nomsJoueurs = nomsJoueurs;
    }
}
