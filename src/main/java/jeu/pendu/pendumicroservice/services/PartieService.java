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
import jeu.pendu.pendumicroservice.repository.DaoPartie;
import jeu.pendu.pendumicroservice.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PartieService {

    private static final int MAX_ERREURS_PAR_JOUEUR = 6;
    private final DaoPartie daoPartie;
    private final JoueurService joueurService;
    private final Random random = new Random();

    public PartieService(DaoPartie daoPartie, JoueurService joueurService) {
        this.daoPartie = daoPartie;
        this.joueurService = joueurService;
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
    public PartieDto creerPartie(String nomPartie, String nomJoueur) throws JoueurInexistantException, PartieDejaExistanteException {
        if (daoPartie.getPartieByNomPartie(nomPartie).isPresent()) {
            throw new PartieDejaExistanteException();
        }
        Random random = new Random();
        String motSecret = StringUtils.normaliser(MotsSecrets.MOTS.get(random.nextInt(MotsSecrets.MOTS.size())));
        String motDePasse = genererMotDePasse();

        Partie nouvellePartie = new Partie(motSecret, nomPartie, motDePasse);
        Joueur joueur = JoueurMapper.toEntity(joueurService.getJoueurParNom(nomJoueur), joueurService);
        nouvellePartie = daoPartie.creerPartie(nouvellePartie);
        joueur.setPartie(nouvellePartie);
        joueur = joueurService.mettreAJour(joueur);
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
    public PartieDto rejoindrePartie(String nomPartie, String motDePasse, String nomJoueur) throws JoueurInexistantException, PartieInexistanteException, MotDePasseIncorrectException, JoueurDejaDansLaPartieException {
        Partie partie = daoPartie.getPartieByNomPartie(nomPartie)
                .orElseThrow(PartieInexistanteException::new);
        if (!partie.getMotDePasse().equals(motDePasse)) {
            throw new MotDePasseIncorrectException();
        }

        Joueur joueur = JoueurMapper.toEntity(joueurService.getJoueurParNom(nomJoueur), joueurService);
        if (joueur == null) {
            throw new JoueurInexistantException();
        }

        if (partie.getJoueurs().contains(joueur)) {
            throw new JoueurDejaDansLaPartieException();
        }
        joueur.setPartie(partie);
        joueur = joueurService.mettreAJour(joueur);
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
        joueurActuel.setTourJoueur(true);
        joueurActuel.setPartie(partie);
        joueurActuel = joueurService.mettreAJour(joueurActuel);
        partie.setJoueurActuelId(joueurActuel.getId());

        partie.setEtatPartie("En cours");

        partie = daoPartie.mettreAJourPartie(partie);

        return PartieMapper.toDto(partie);
    }


    @Transactional
    public PartieDto tourJoueur(Long partieId, String proposition) throws JoueurInexistantException, PartieInexistanteException, PasTonTourException {
        Partie partie = obtenirPartieValidee(partieId);
        Joueur joueur = obtenirJoueurValide(partie.getJoueurActuelId(), partie);
        //vérifier si la partie est en cours
        if (!partie.getEtatPartie().equals("En cours")) {
            throw new PartieInexistanteException();
        }
        if (!joueur.isTourJoueur()) {
            throw new PasTonTourException();
        }
        traiterProposition(partie, joueur,proposition);
        // Passer au joueur suivant si la partie n'est pas terminée
        if (!estGagnee(partie.getId()) && !estPartieTerminee(partie)) {
            partie = passerAuJoueurSuivant(partie.getId());
        } else {
            partie = terminerPartie(partie.getId());
        }

        partie = daoPartie.mettreAJourPartie(partie);
        joueur = joueurService.mettreAJour(joueur);
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

    public JoueurDto obtenirVainqueur(Long partieId) throws PartieInexistanteException, JoueurInexistantException {
        Partie partie = daoPartie.getPartieByIdOptional(partieId)
                .orElseThrow(PartieInexistanteException::new);
        if (!estGagnee(partieId)) {
            return null;
        }
        return joueurService.getJoueur(partie.getJoueurActuelId());

        //return partie.getJoueurs().stream()
                //.map(JoueurMapper::toDto)
                //.filter(JoueurDto::isTourJoueur)
                //.findFirst()
                //.orElse(null);
    }

    public Partie passerAuJoueurSuivant(Long partieId) throws PartieInexistanteException, JoueurInexistantException {
        Partie partie = obtenirPartieValidee(partieId);
        // Trouver l'index du joueur actuel
        int indexActuel = partie.getJoueurs().indexOf(partie.getJoueurs().stream()
                .filter(Joueur::isTourJoueur)
                .findFirst()
                .orElse(null));
        // Réinitialiser le tour du joueur actuel
        if (indexActuel != -1) {
            partie.getJoueurs().get(indexActuel).setTourJoueur(false);
            joueurService.mettreAJour(partie.getJoueurs().get(indexActuel));
        }
        // Passer au joueur suivant
        int indexSuivant = (indexActuel + 1) % partie.getJoueurs().size();
        while (aAtteintMaxErreurs(partie.getJoueurs().get(indexSuivant).getId(), partie)) {
            indexSuivant = (indexSuivant + 1) % partie.getJoueurs().size();
        }
        partie.getJoueurs().get(indexSuivant).setTourJoueur(true);
        joueurService.mettreAJour(partie.getJoueurs().get(indexActuel));
        partie.setJoueurActuelId(partie.getJoueurs().get(indexSuivant).getId());
        partie = daoPartie.mettreAJourPartie(partie);
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

    private void traiterLettreProposee(Partie partie, Joueur joueur, char lettreProposee) throws JoueurInexistantException, PartieInexistanteException {
        String motSecret = StringUtils.normaliser(partie.getMotSecret());
        char lettreNormalisee = StringUtils.normaliser(String.valueOf(lettreProposee)).charAt(0);
        StringBuilder etatMot = new StringBuilder(partie.getEtatMot());
        boolean lettreTrouvee = false;
        List<Character> lettresDevinees = partie.getLettresDevinees();
        List<Character> lettresRater = partie.getLettresRatees();

        for (int i = 0; i < motSecret.length(); i++) {
            if (motSecret.charAt(i) == lettreNormalisee && etatMot.charAt(i) == '_') {
                etatMot.setCharAt(i, lettreNormalisee);
                lettreTrouvee = true;
                lettresDevinees.add(lettreNormalisee);
                partie.setLettresDevinees(lettresDevinees);
                daoPartie.mettreAJourPartie(partie);
            }
        }
        if (!lettreTrouvee) {
            if (!partie.getLettresRatees().contains(lettreNormalisee)) {
                lettresRater.add(lettreNormalisee);
                partie.setLettresRatees(lettresRater);
                daoPartie.mettreAJourPartie(partie);
            }
            if (partie.getJoueurs().size() == 1) {
                partie.setErreurs(partie.getErreurs() + 1);
                daoPartie.mettreAJourPartie(partie);
            } else {
                ajouterErreurJoueur(joueur.getId(),partie);
                joueurService.mettreAJour(joueur);
                //if (aAtteintMaxErreurs(joueur.getId(), partie)) {
                //partie.getJoueurs().remove(joueur);
                //partie.setJoueurs(partie.getJoueurs());
                //joueur.setPartie(null);
                //joueurService.mettreAJour(joueur);
                // }
            }
        }
        if (estPartieTerminee(partie)) {
            partie = terminerPartie(partie.getId());
        }
        partie.setEtatMot(etatMot.toString());
        daoPartie.mettreAJourPartie(partie);
    }

    public void ajouterErreurJoueur(Long joueurId,Partie partie ) throws JoueurInexistantException {
        Joueur joueur = obtenirJoueurValide(joueurId, partie);
        joueur.setErreurs(joueur.getErreurs() + 1);
        joueurService.mettreAJour(joueur);
    }

    public boolean aAtteintMaxErreurs(Long joueurId, Partie partie) throws JoueurInexistantException {
        Joueur joueur = obtenirJoueurValide(joueurId, partie);
        int maxErreurs = partie.getJoueurs().size() > 1 ? MAX_ERREURS_PAR_JOUEUR : Partie.MAX_ERREURS;
        return joueur.getErreurs() >= maxErreurs;
    }


    private void traiterMotPropose(Partie partie, String motPropose) throws PartieInexistanteException, JoueurInexistantException {
        String motProposeNormalise = StringUtils.normaliser(motPropose);
        if (motProposeNormalise.equals(partie.getMotSecret())) {
            partie.setEtatMot(motProposeNormalise);
            partie = terminerPartie(partie.getId());
        } else {
            if (partie.getJoueurs().size() == 1) {
                partie.setErreurs(partie.getErreurs() + 1);
            } else {
                ajouterErreurJoueur(partie.getJoueurActuelId(), partie);
                joueurService.mettreAJour(obtenirJoueurValide(partie.getJoueurActuelId(), partie));
                daoPartie.mettreAJourPartie(partie);
            }
        }
        if (estPartieTerminee(partie)) {
            partie = terminerPartie(partie.getId());
            daoPartie.mettreAJourPartie(partie);
        }
    }



    public Partie terminerPartie(Long partieId) throws PartieInexistanteException {
        Partie partie = daoPartie.getPartieByIdOptional(partieId)
                .orElseThrow(PartieInexistanteException::new);
        boolean estTerminee = estPartieTerminee(partie);
        if (estTerminee) {
            partie.setEtatPartie("Terminée");
            partie = daoPartie.mettreAJourPartie(partie);
            //je veux parcourir la liste des joueurs et supprimer la partie de chaque joueur
            for (Joueur joueur : partie.getJoueurs()) {
                joueur.setPartie(null);
                joueur.setTourJoueur(false);
                joueur.setErreurs(0);
                joueurService.mettreAJour(joueur);
            }
        }
        return partie;
    }

    private boolean estPartieTerminee(Partie partie) {
        boolean motDevine = !partie.getEtatMot().contains("_");
        boolean maxErreursAtteint = partie.getErreurs() >= Partie.MAX_ERREURS;
        boolean joueurPerdu = partie.getJoueurs().stream()
                .allMatch(j -> j.getErreurs() >= MAX_ERREURS_PAR_JOUEUR);

        return motDevine || maxErreursAtteint || joueurPerdu;
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
