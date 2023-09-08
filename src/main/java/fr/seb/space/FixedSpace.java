/*
 * Copyright (c) 2023. Seb-sti1. See LICENSE.
 */

package fr.seb.space;

import fr.seb.Expression;
import fr.seb.vectors.Point;
import fr.seb.vectors.Vector;
import fr.seb.vectors.VectorNull;

import java.util.ArrayList;
import java.util.List;

public class FixedSpace implements Space {

    final Vector x;
    final Vector y;
    final Vector z;

    final Point center;

    public FixedSpace() {
        this.x = new Vector("x",0, this, VECTOR.X);
        this.y = new Vector("y",0, this, VECTOR.Y);
        this.z = new Vector("z",0, this, VECTOR.Z);

        this.center = new Point("O", null, new VectorNull());
    }

    @Override
    public List<Space> getFathers() {
        return new ArrayList<>();
    }

    @Override
    public Vector getUnitaryVector(VECTOR VECTOR) {
        switch (VECTOR) {
            case X:
                return x;
            case Y:
                return y;
            case Z:
                return z;
        }
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Point getFixedPoint() {
        return this.center;
    }

    @Override
    public Space getFather() {
        return null;
    }

    @Override
    public Expression<Vector> getRotationVector() {
        return null;
    }
}
