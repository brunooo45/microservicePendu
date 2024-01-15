package jeu.pendu.pendumicroservice.controleur;

import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.dtos.PartieDto;
import jeu.pendu.pendumicroservice.exception.JoueurInexistantException;
import jeu.pendu.pendumicroservice.facade.FacadePartie;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ControleurPartie {

    private final FacadePartie facadePartie;

    public ControleurPartie(FacadePartie facadePartie) {
        this.facadePartie = facadePartie;
    }

    @SneakyThrows
    @PostMapping("/partiePendu")
    public ResponseEntity<PartieDto> creerPartie(@RequestParam String nomPartie, @RequestParam String nomJoueur){
        PartieDto partieCree = facadePartie.creerPartie(nomPartie, nomJoueur);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(partieCree.getId())
                .toUri();

        return ResponseEntity.created(location).body(partieCree);
    }

    @SneakyThrows
    @GetMapping("/partiePendu/{id}")
    public ResponseEntity<PartieDto> getPartie(@PathVariable Long id) {
        return ResponseEntity.ok(facadePartie.getPartie(id));
    }

    @SneakyThrows
    @GetMapping("/partiePendu/{joueurId}")
    public ResponseEntity<PartieDto> getPartieEnCours(@PathVariable Long joueurId) {
        return ResponseEntity.ok(facadePartie.getPartieEnCours(joueurId));
    }

    @SneakyThrows
    @GetMapping("/partiePendu/enAttente")
    public ResponseEntity<List<PartieDto>> getPartiesEnAttente() {
        return ResponseEntity.ok(facadePartie.getPartiesEnAttente());
    }

    @SneakyThrows
    @GetMapping("/partiePendu/all")
    public ResponseEntity<List<PartieDto>> getAllParties() {
        return ResponseEntity.ok(facadePartie.getAllParties());
    }

    @SneakyThrows
    @PostMapping("/rejoindre")
    public ResponseEntity<PartieDto> rejoindrePartie(@RequestParam String nomPartie, @RequestParam String motDePasse, @RequestParam String nomJoueur) {
        PartieDto partieRejointe = facadePartie.rejoindrePartie(nomPartie, motDePasse, nomJoueur);
        return ResponseEntity.ok(partieRejointe);
    }

    @SneakyThrows
    @PostMapping("/lancer/{partieId}")
    public ResponseEntity<PartieDto> lancerPartie(@PathVariable Long partieId) {
        PartieDto partieLancee = facadePartie.lancePartie(partieId);
        return ResponseEntity.ok(partieLancee);
    }

    @SneakyThrows
    @PostMapping("/jouerTour")
    public ResponseEntity<PartieDto> jouerTour(@RequestParam Long partieId, @RequestParam Long joueurId, @RequestParam String proposition) {
        PartieDto partieMiseAJour = facadePartie.jouerTour(partieId, joueurId, proposition);
        return ResponseEntity.ok(partieMiseAJour);
    }

    @SneakyThrows
    @GetMapping("/vainqueur/{partieId}")
    public ResponseEntity<JoueurDto> getVainqueur(@PathVariable Long partieId) {
        return ResponseEntity.ok(facadePartie.getVainqueur(partieId));
    }

    @SneakyThrows
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Void> deletePartie(@PathVariable Long id) {
        facadePartie.deletePartie(id);
        return ResponseEntity.noContent().build();
    }

}