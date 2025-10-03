package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RedundancyBlueprintFilter implements BlueprintFilter {
    @Override
    public Blueprint filter(Blueprint bp) {
        List<Point> points = bp.getPoints();
        if (points.isEmpty()) return bp;
        List<Point> filtered = new ArrayList<>();
        Point prev = null;
        for (Point p : points) {
            if (prev == null || !(p.getX() == prev.getX() && p.getY() == prev.getY())) {
                filtered.add(p);
            }
            prev = p;
        }
        return new Blueprint(bp.getAuthor(), bp.getName(), filtered.toArray(new Point[0]));
    }
}
