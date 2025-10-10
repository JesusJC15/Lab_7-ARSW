/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

/**
 *
 * @author hcadavid
 */
@Service
public class BlueprintsServices {
    private final BlueprintsPersistence bpp;
    private final BlueprintFilter blueprintFilter;

    @Autowired
    public BlueprintsServices(BlueprintsPersistence bpp, BlueprintFilter blueprintFilter) {
        this.bpp = bpp;
        this.blueprintFilter = blueprintFilter;
    }

    public void addNewBlueprint(Blueprint bp) {
        try {
            bpp.saveBlueprint(bp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBlueprint(String author, String bpname, Blueprint updatedBlueprint) {
        try {
            bpp.updateBlueprint(author, bpname, updatedBlueprint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<Blueprint> getAllBlueprints() {
        Set<Blueprint> all = bpp.getAllBlueprints();
        if (blueprintFilter == null) return all;
        Set<Blueprint> filtered = new java.util.HashSet<>();
        for (Blueprint bp : all) {
            filtered.add(blueprintFilter.filter(bp));
        }
        return filtered;
    }

    /**
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        Blueprint bp = bpp.getBlueprint(author, name);
        return blueprintFilter != null ? blueprintFilter.filter(bp) : bp;
    }

    /**
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> bps = bpp.getBlueprintsByAuthor(author);
        if (blueprintFilter == null) return bps;
        Set<Blueprint> filtered = new java.util.HashSet<>();
        for (Blueprint bp : bps) {
            filtered.add(blueprintFilter.filter(bp));
        }
        return filtered;
    }

    public void deleteBlueprint(String author, String bpname) throws BlueprintNotFoundException {
        bpp.deleteBlueprint(author, bpname);
    }
    
}