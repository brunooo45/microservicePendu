package jeu.pendu.pendumicroservice.services;

import jakarta.transaction.Transactional;
import jeu.pendu.pendumicroservice.constantes.MotsSecrets;
import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.dtos.PartieDto;
import jeu.pendu.pendumicroservice.exception.*;
import jeu.pendu.pendumicroservice.mapper.JoueurMapper;
import jeu.pendu.pendumicroservice.mapper.PartieMapper;
import jeu.pendu.pendumicroservice.modele.Joueur;
import jeu.pendu.pendumicroservice.modele.Partie;
import jeu.pendu.pendumicroservice.repository.DaoJoueur;
import jeu.pendu.pendumicroservice.repository.DaoPartie;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PartieService {

    private static final int MAX_ERREURS_PAR_JOUEUR = 6;
    private final DaoPartie daoPartie;
    private final JoueurService joueurService;
    private final Random random = new Random();
    private final DaoJoueur daoJoueur;

    public PartieService(DaoPartie daoPartie, JoueurService joueurService, DaoJoueur daoJoueur) {
        this.daoPartie = daoPartie;
        this.joueurService = joueurService;
        this.daoJoueur = daoJoueur;
    }

    public Partie ajouterJoueur(Partie partie, Joueur joueur) {
        List<Joueur> joueursActuels = partie.getJoueurs();
        joueursActuels.add(joueur);
        partie.setJoueurs(joueursActuels);
        //partie = daoPartie.mettreAJourPartie(partie);
        return partie;
    }

    private String genererMotDePasse() {
        Random mdp = new Random();
        int nombre = 100000 + mdp.nextInt(900000);
        return String.valueOf(nombre);
    }

    @Transactional
    public PartieDto creerPartie(String nomPartie, String nomJoueur) throws JoueurInexistantException {
        Random random = new Random();
        String motSecret = MotsSecrets.MOTS.get(random.nextInt(MotsSecrets.MOTS.size()));
        String motDePasse = genererMotDePasse();

        Partie nouvellePartie = new Partie(motSecret, nomPartie, motDePasse);
        Joueur joueur = JoueurMapper.toEntity(joueurService.getJoueurParNom(nomJoueur));
        nouvellePartie = daoPartie.creerPartie(nouvellePartie);
        joueur.setPartie(nouvellePartie);
        joueur = daoJoueur.creerJoueur(joueur);
        nouvellePartie = ajouterJoueur(nouvellePartie, joueur);
        Partie partieEnregistree = daoPartie.creerPartie(nouvellePartie);

        partieEnregistree = daoPartie.mettreAJourPartie(partieEnregistree);
        return PartieMapper.toDto(partieEnregistree);
    }

    public List<PartieDto> obtenirToutesLesParties() {
        List<Partie> parties = daoPartie.getToutesLesParties();
        return parties.stream().map(PartieMapper::toDto).collect(Collectors.toList());
    }

    public PartieDto obtenirPartieById(Long partieId) throws PartieInexistanteException {
        Partie partie = daoPartie.getPartieByIdOptional(partieId)
                .orElseThrow(PartieInexistanteException::new);
        return PartieMapper.toDto(partie);
    }

    public PartieDto obtenirPartieByNom(String nomPartie) throws PartieInexistanteException {
        Partie partie = daoPartie.getPartieByNomPartie(nomPartie)
                .orElseThrow(PartieInexistanteException::new);
        return PartieMapper.toDto(partie);
    }

    @Transactional
    public PartieDto rejoindrePartie(String nomPartie, String motDePasse, String nomJoueur) throws JoueurInexistantException, PartieInexistanteException, MotDePasseIncorrectException {
        Partie partie = daoPartie.getPartieByNomPartie(nomPartie)
                .orElseThrow(PartieInexistanteException::new);
        if (!partie.getMotDePasse().equals(motDePasse)) {
            throw new MotDePasseIncorrectException();
        }

        Joueur joueur = JoueurMapper.toEntity(joueurService.getJoueurParNom(nomJoueur));
        if (joueur == null) {
            throw new JoueurInexistantException();
        }

        partie = ajouterJoueur(partie, joueur);
        Partie partieMiseAJour = daoPartie.mettreAJourPartie(partie);
        return PartieMapper.toDto(partieMiseAJour);
    }

    @Transactional
    public PartieDto lancerPartie(Long partieId) throws PartieInexistanteException, NombredeJoueursIncorrectException {
        Partie partie = obtenirPartieValidee(partieId);

        // Vérifier le nombre de joueurs
        if (partie.getJoueurs().isEmpty() || partie.getJoueurs().size() > 3) {
            throw new NombredeJoueursIncorrectException();
        }
        Random random = new Random();
        Joueur joueurActuel = partie.getJoueurs().get(random.nextInt(partie.getJoueurs().size()));
        partie.setJoueurActuelId(joueurActuel.getId());

        partie.setEtatPartie("En cours");

        partie = daoPartie.mettreAJourPartie(partie);

        return PartieMapper.toDto(partie);
    }


    @Transactional
    public PartieDto tourJoueur(Long partieId, Long joueurId, String proposition) throws JoueurInexistantException, PartieInexistanteException, PasTonTourException {
        Partie partie = obtenirPartieValidee(partieId);
        Joueur joueur = obtenirJoueurValide(joueurId, partie);
        if (!joueur.isTourJoueur()) {
            throw new PasTonTourException();
        }
        traiterProposition(partie, joueur,proposition);
        if (!estGagnee(partie.getId())) {
            partie = passerAuJoueurSuivant(partie.getId());
        } else {
            partie = terminerPartie(partie.getId());
        }

        partie = daoPartie.mettreAJourPartie(partie);
        return PartieMapper.toDto(partie);


    }

    private Partie obtenirPartieValidee(Long partieId) throws PartieInexistanteException {
        return daoPartie.getPartieByIdOptional(partieId)
                .orElseThrow(PartieInexistanteException::new);
    }

    private Joueur obtenirJoueurValide(Long joueurId, Partie partie) throws JoueurInexistantException {
        return partie.getJoueurs().stream()
                .filter(j -> j.getId().equals(joueurId))
                .findFirst()
                .orElseThrow(JoueurInexistantException::new);
    }

    public JoueurDto obtenirVainqueur(Long partieId) throws PartieInexistanteException {
        Partie partie = daoPartie.getPartieByIdOptional(partieId)
                .orElseThrow(PartieInexistanteException::new);
        if (!estGagnee(partieId)) {
            return null;
        }

        return partie.getJoueurs().stream()
                .map(JoueurMapper::toDto)
                .filter(JoueurDto::isTourJoueur)
                .findFirst()
                .orElse(null);
    }

    public Partie passerAuJoueurSuivant(Long partieId) throws PartieInexistanteException {
        Partie partie = obtenirPartieValidee(partieId);
        // Trouver l'index du joueur actuel
        int indexActuel = partie.getJoueurs().indexOf(partie.getJoueurs().stream()
                .filter(Joueur::isTourJoueur)
                .findFirst()
                .orElse(null));
        // Réinitialiser le tour du joueur actuel
        if (indexActuel != -1) {
            partie.getJoueurs().get(indexActuel).setTourJoueur(false);
        }
        // Passer au joueur suivant
        int indexSuivant = (indexActuel + 1) % partie.getJoueurs().size();
        partie.getJoueurs().get(indexSuivant).setTourJoueur(true);
        return partie;
    }

    private void traiterProposition(Partie partie,Joueur joueur, String proposition) throws PartieInexistanteException, JoueurInexistantException {
        if (proposition.length() == 1) {
            char lettreProposee = proposition.charAt(0);
            traiterLettreProposee(partie, joueur, lettreProposee);
        } else {
            traiterMotPropose(partie, proposition);
        }
    }

    private void traiterLettreProposee(Partie partie, Joueur joueur, char lettreProposee) throws JoueurInexistantException {
        String motSecret = partie.getMotSecret();
        StringBuilder etatMot = new StringBuilder(partie.getEtatMot());
        boolean lettreTrouvee = false;

        for (int i = 0; i < motSecret.length(); i++) {
            if (motSecret.charAt(i) == lettreProposee && etatMot.charAt(i) == '_') {
                etatMot.setCharAt(i, lettreProposee);
                lettreTrouvee = true;
            }
        }

        if (!lettreTrouvee) {
            ajouterErreurJoueur(joueur.getId(),partie);
            if (aAtteintMaxErreurs(joueur.getId(), partie)) {
                partie.getJoueurs().remove(joueur);
                partie.setJoueurs(partie.getJoueurs());
            }
        }

        partie.setEtatMot(etatMot.toString());
    }

    public void ajouterErreurJoueur(Long joueurId,Partie partie ) throws JoueurInexistantException {
        Joueur joueur = obtenirJoueurValide(joueurId, partie);
        joueur.setErreurs(joueur.getErreurs() + 1);
    }

    public boolean aAtteintMaxErreurs(Long joueurId, Partie partie) throws JoueurInexistantException {
        Joueur joueur = obtenirJoueurValide(joueurId, partie);
        int maxErreurs = partie.getJoueurs().size() > 1 ? MAX_ERREURS_PAR_JOUEUR : Partie.MAX_ERREURS;
        return joueur.getErreurs() >= maxErreurs;
    }


    private void traiterMotPropose(Partie partie, String motPropose) throws PartieInexistanteException {
        if (motPropose.equals(partie.getMotSecret())) {
            partie.setEtatMot(motPropose);
            partie = terminerPartie(partie.getId());
        } else {
            partie.setErreurs(partie.getErreurs() + 1);
            if (estPartieTerminee(partie)) {
                partie = terminerPartie(partie.getId());
            }
        }
    }



    public Partie terminerPartie(Long partieId) throws PartieInexistanteException {
        Partie partie = daoPartie.getPartieByIdOptional(partieId)
                .orElseThrow(PartieInexistanteException::new);
        boolean estTerminee = estPartieTerminee(partie);
        if (estTerminee) {
            partie.setEtatPartie("Terminée");
            partie = daoPartie.mettreAJourPartie(partie);
        }
        return partie;
    }

    private boolean estPartieTerminee(Partie partie) {
        boolean motDevine = !partie.getEtatMot().contains("_");
        boolean maxErreursAtteint = partie.getErreurs() >= Partie.MAX_ERREURS;

        return motDevine || maxErreursAtteint;
    }

    public boolean estGagnee(Long partieId) throws PartieInexistanteException {
        Partie partie = obtenirPartieValidee(partieId);
        return !partie.getEtatMot().contains("_");
    }

    @Transactional
    public void supprimerPartie(Long partieId) throws PartieInexistanteException {
        if (daoPartie.getPartieByIdOptional(partieId).isEmpty()) {
            throw new PartieInexistanteException();
        }
        daoPartie.supprimerPartie(partieId);
    }




}
