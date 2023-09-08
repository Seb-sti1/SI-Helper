package fr.seb.space;

import fr.seb.Expression;
import fr.seb.vectors.Point;
import fr.seb.vectors.Vector;

import java.util.List;

public interface Space {

    enum VECTOR {X, Y, Z}

    Space getFather();
    List<Space> getFathers();
    int getId();

    Point getFixedPoint();

    Vector getUnitaryVector(VECTOR VECTOR);

    Expression<Vector> getRotationVector();


}
