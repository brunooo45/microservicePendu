package jeu.pendu.pendumicroservice.controleur;

import jeu.pendu.pendumicroservice.dtos.JoueurDto;
import jeu.pendu.pendumicroservice.facade.FacadeJoueur;
import jeu.pendu.pendumicroservice.modele.Joueur;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ControleurJoueur {

    private final FacadeJoueur facadeJoueur;

    public ControleurJoueur(FacadeJoueur facadeJoueur) {
        this.facadeJoueur = facadeJoueur;
    }

    @SneakyThrows
    @PostMapping("/joueurPendu")
    public ResponseEntity<JoueurDto> creerJoueur(@RequestBody Joueur joueur) {
        JoueurDto joueurCree = facadeJoueur.creerJoueur(joueur);

        // Créer l'URI de la ressource créée
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(joueurCree.getId())
                .toUri();

        return ResponseEntity.created(location).body(joueurCree);
    }

    @SneakyThrows
    @GetMapping("/joueurPendu")
    public ResponseEntity<List<JoueurDto>> getTousLesJoueurs() {
        return ResponseEntity.ok(facadeJoueur.getTousLesJoueurs());
    }

    @SneakyThrows
    @GetMapping("/joueurPendu/{id}")
    public ResponseEntity<JoueurDto> getJoueur(@PathVariable Long id) {
        return ResponseEntity.ok(facadeJoueur.getJoueur(id));
    }

    @SneakyThrows
    @DeleteMapping("joueurPendu/{id}")
    public ResponseEntity<Void> deleteJoueur(@PathVariable Long id) {
        facadeJoueur.deleteJoueur(id);
        return ResponseEntity.noContent().build();
    }
}
