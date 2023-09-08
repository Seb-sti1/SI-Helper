package fr.seb.vectors;

import fr.seb.Expression;

import java.util.ArrayList;
import java.util.List;

public class Point {

    final String name;
    final Point father;
    final Expression<Vector> fromFather;

    /**
     * A point
     * @param name the name of the point
     * @param father the point where the vector start
     * @param fromFather the vector from the point father to this one. (if name = B, father = A, then fromFather = \vec{AB}
     */
    public Point(String name, Point father, Expression<Vector> fromFather) {

        this.name = name;
        this.father = father;
        this.fromFather = fromFather;
    }

    public Point getFather() {
        return this.father;
    }

    public List<Point> getFathers() {
        if (this.father == null) {
            return new ArrayList<>();
        } else {
            List<Point> fathers = new ArrayList<>();

            Point p = father;
            while (p != null) {
                fathers.add(p);

                p = p.getFather();
            }
            return fathers;
        }
    }

    public Expression<Vector> getFromFather() {
        return this.fromFather;
    }

    @Override
    public String toString() {
        return name;
    }

}
