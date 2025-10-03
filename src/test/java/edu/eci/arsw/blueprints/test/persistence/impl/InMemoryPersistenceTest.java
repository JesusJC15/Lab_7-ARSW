package edu.eci.arsw.blueprints.test.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryPersistenceTest {

    @Test
    public void saveNewAndLoadTest() throws BlueprintPersistenceException, BlueprintNotFoundException {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Point[] pts0 = new Point[]{new Point(40, 40), new Point(15, 15)};
        Blueprint bp0 = new Blueprint("mack", "mypaint", pts0);

        ibpp.saveBlueprint(bp0);

        Point[] pts = new Point[]{new Point(0, 0), new Point(10, 10)};
        Blueprint bp = new Blueprint("john", "thepaint", pts);

        ibpp.saveBlueprint(bp);

        assertNotNull(ibpp.getBlueprint(bp.getAuthor(), bp.getName()),
                "Loading a previously stored blueprint returned null.");

        assertEquals(bp, ibpp.getBlueprint(bp.getAuthor(), bp.getName()),
                "Loading a previously stored blueprint returned a different blueprint.");
    }

    @Test
    public void saveExistingBpTest() throws BlueprintPersistenceException {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Point[] pts = new Point[]{new Point(0, 0), new Point(10, 10)};
        Blueprint bp = new Blueprint("john", "thepaint", pts);

        ibpp.saveBlueprint(bp);

        Point[] pts2 = new Point[]{new Point(10, 10), new Point(20, 20)};
        Blueprint bp2 = new Blueprint("john", "thepaint", pts2);

        assertThrows(BlueprintPersistenceException.class, () -> {
            ibpp.saveBlueprint(bp2);
        }, "An exception was expected after saving a second blueprint with the same name and author.");
    }

    @Test
    public void getNonExistingBlueprintTest() {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        assertThrows(BlueprintNotFoundException.class, () -> {
            ibpp.getBlueprint("ghost", "phantom");
        }, "Expected BlueprintNotFoundException for non-existing blueprint");
    }

    @Test
    public void getBlueprintsByAuthorTest() throws Exception {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Point[] pts1 = new Point[]{new Point(0, 0), new Point(10, 10)};
        Point[] pts2 = new Point[]{new Point(20, 20), new Point(30, 30)};

        Blueprint bp1 = new Blueprint("alice", "house", pts1);
        Blueprint bp2 = new Blueprint("alice", "garden", pts2);

        ibpp.saveBlueprint(bp1);
        ibpp.saveBlueprint(bp2);

        assertEquals(2, ibpp.getBlueprintsByAuthor("alice").size(),
                "Author should have 2 blueprints");
    }

    @Test
    public void getBlueprintsByAuthorNotFoundTest() {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        assertThrows(BlueprintNotFoundException.class, () -> {
            ibpp.getBlueprintsByAuthor("nonexistent");
        });
    }
}