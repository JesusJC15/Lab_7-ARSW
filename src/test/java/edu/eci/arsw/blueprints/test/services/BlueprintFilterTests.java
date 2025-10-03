package edu.eci.arsw.blueprints.test.services;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.services.BlueprintFilter;
import edu.eci.arsw.blueprints.services.RedundancyBlueprintFilter;
import edu.eci.arsw.blueprints.services.SubsamplingBlueprintFilter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BlueprintFilterTests {

    @Test
    public void testRedundancyBlueprintFilter() {
        Point[] points = new Point[]{
                new Point(0, 0),
                new Point(0, 0), // repeated
                new Point(1, 1),
                new Point(1, 1), // repeated
                new Point(2, 2)
        };
        Blueprint bp = new Blueprint("author", "redundancy", points);
        BlueprintFilter filter = new RedundancyBlueprintFilter();
        Blueprint filtered = filter.filter(bp);

        assertEquals(3, filtered.getPoints().size());
        assertEquals(0, filtered.getPoints().get(0).getX());
        assertEquals(1, filtered.getPoints().get(1).getX());
        assertEquals(2, filtered.getPoints().get(2).getX());
    }

    @Test
    public void testSubsamplingBlueprintFilter() {
        Point[] points = new Point[]{
                new Point(0, 0),
                new Point(1, 1),
                new Point(2, 2),
                new Point(3, 3),
                new Point(4, 4)
        };
        Blueprint bp = new Blueprint("author", "subsampling", points);
        BlueprintFilter filter = new SubsamplingBlueprintFilter();
        Blueprint filtered = filter.filter(bp);

        assertEquals(3, filtered.getPoints().size());
        assertEquals(0, filtered.getPoints().get(0).getX());
        assertEquals(2, filtered.getPoints().get(1).getX());
        assertEquals(4, filtered.getPoints().get(2).getX());
    }
}