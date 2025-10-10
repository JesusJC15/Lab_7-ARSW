/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.services.BlueprintsServices;



/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    private final BlueprintsServices blueprintsServices;

    @Autowired
    public BlueprintAPIController(BlueprintsServices blueprintsServices) {
        this.blueprintsServices = blueprintsServices;
    }

    @GetMapping
    public ResponseEntity<?> getAllBlueprints(){
        try {
            return new ResponseEntity<>(blueprintsServices.getAllBlueprints(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{author}")
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable String author){
        try {
            return new ResponseEntity<>(blueprintsServices.getBlueprintsByAuthor(author), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<?> getBlueprintsByAuthorAndBpName(@PathVariable String author, @PathVariable String bpname){
        try {
            return new ResponseEntity<>(blueprintsServices.getBlueprint(author, bpname), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createNewBlueprint(@RequestBody Blueprint bp){
        try {
            blueprintsServices.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{author}/{bpname}")
    public ResponseEntity<?> updateBlueprint(@PathVariable String author, @PathVariable String bpname, @RequestBody Blueprint bp) {
        try {
            blueprintsServices.updateBlueprint(author, bpname, bp);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{author}/{bpname}")
    public ResponseEntity<?> deleteBlueprint(@PathVariable String author, @PathVariable String bpname) {
        try {
            blueprintsServices.deleteBlueprint(author, bpname);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

