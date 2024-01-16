package jeu.pendu.pendumicroservice.dtos;

public class JoueurDto {

    private Long id;
    private String nomJoueur;
    private boolean tourJoueur;
    private int erreurs = 0;
    private PartieDto partieDto;

    public JoueurDto() {
    }

    public JoueurDto(Long id, String nomJoueur, boolean tourJoueur, int erreurs, PartieDto partieDto) {
        this.id = id;
        this.nomJoueur = nomJoueur;
        this.tourJoueur = tourJoueur;
        this.erreurs = erreurs;
        this.partieDto = partieDto;
    }

    public JoueurDto(Long id, String nomJoueur, boolean tourJoueur) {
        this.id = id;
        this.nomJoueur = nomJoueur;
        this.tourJoueur = tourJoueur;
    }

    public JoueurDto(Long id, String nomJoueur, boolean tourJoueur, int erreurs) {
        this.id = id;
        this.nomJoueur = nomJoueur;
        this.tourJoueur = tourJoueur;
        this.erreurs = erreurs;
    }

    public PartieDto getPartieDto() {
        return partieDto;
    }

    public void setPartieDto(PartieDto partieDto) {
        this.partieDto = partieDto;
    }

    public int getErreurs() {
        return erreurs;
    }

    public void setErreurs(int erreurs) {
        this.erreurs = erreurs;
    }

    public boolean isTourJoueur() {
        return tourJoueur;
    }

    public void setTourJoueur(boolean tourJoueur) {
        this.tourJoueur = tourJoueur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomJoueur() {
        return nomJoueur;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }
}
