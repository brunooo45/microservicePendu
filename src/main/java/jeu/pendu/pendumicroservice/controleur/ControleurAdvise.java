package jeu.pendu.pendumicroservice.controleur;

import jeu.pendu.pendumicroservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControleurAdvise {

    @ExceptionHandler(JoueurDejaExistantException.class)
    public ResponseEntity<String> handleJoueurDejaExistantException(JoueurDejaExistantException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(JoueurInexistantException.class)
    public ResponseEntity<String> handleJoueurInexistantException(JoueurInexistantException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PartieInexistanteException.class)
    public ResponseEntity<String> handlePartieInexistanteException(PartieInexistanteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MotDePasseIncorrectException.class)
    public ResponseEntity<String> handleMotDePasseIncorrectException(MotDePasseIncorrectException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NombredeJoueursIncorrectException.class)
    public ResponseEntity<String> handleNombredeJoueursIncorrectException(NombredeJoueursIncorrectException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasTonTourException.class)
    public ResponseEntity<String> handlePasTonTourException(PasTonTourException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("Une erreur est survenue : " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
