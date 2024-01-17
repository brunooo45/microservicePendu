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
    public ResponseEntity<PartieDto> creerPartie(@RequestParam String nom_Partie, @RequestParam String nom_Joueur){
        PartieDto partieCree = facadePartie.creerPartie(nom_Partie, nom_Joueur);

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
    @PostMapping("partiePendu/rejoindre")
    public ResponseEntity<PartieDto> rejoindrePartie(@RequestParam String nom_partie, @RequestParam String mot_de_passe, @RequestParam String nom_Joueur) {
        PartieDto partieRejointe = facadePartie.rejoindrePartie(nom_partie, mot_de_passe, nom_Joueur);
        return ResponseEntity.ok(partieRejointe);
    }

    @SneakyThrows
    @PostMapping("partiePendu/lancer/{partieId}")
    public ResponseEntity<PartieDto> lancerPartie(@PathVariable Long partieId) {
        PartieDto partieLancee = facadePartie.lancePartie(partieId);
        return ResponseEntity.ok(partieLancee);
    }

    @SneakyThrows
    @PostMapping("partiePendu/jouerTour")
    public ResponseEntity<PartieDto> jouerTour(@RequestParam Long partieId, @RequestParam String proposition) {
        PartieDto partieMiseAJour = facadePartie.jouerTour(partieId, proposition);
        return ResponseEntity.ok(partieMiseAJour);
    }

    @SneakyThrows
    @GetMapping("partiePendu/vainqueur/{partieId}")
    public ResponseEntity<JoueurDto> getVainqueur(@PathVariable Long partieId) {
        return ResponseEntity.ok(facadePartie.getVainqueur(partieId));
    }

    @SneakyThrows
    @DeleteMapping("partiePendu/supprimer/{id}")
    public ResponseEntity<Void> deletePartie(@PathVariable Long id) {
        facadePartie.deletePartie(id);
        return ResponseEntity.noContent().build();
    }

}