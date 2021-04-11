package fr.seb.si;

import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.function.Addition;
import fr.seb.function.Derivation;
import fr.seb.function.WedgeProduct;
import fr.seb.space.Space;
import fr.seb.vectors.Point;
import fr.seb.vectors.Vector;
import fr.seb.vectors.VectorNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Velocity {

    Space R;
    Point P;

    public Velocity(Point P, Space R) {
        this.P = P;
        this.R = R;
    }

    public void changePoint(Point newPoint) {
        this.P = newPoint;
    }

    public Expression<Vector> calculate(@NotNull Space R_fixed) {
        if (R.getFathers().contains(R_fixed)) {

            if (R.getFather() != R_fixed) {
                Space R_start = R;

                List<Expression<Vector>> list = new ArrayList<>();


                while (R_start != R_fixed) {
                    list.add(new Velocity(P, R_start).calculate(R_start.getFather()));

                    R_start = R_start.getFather();
                }

                return new Addition<>(list);
            } else {
                if (P == R.getFixedPoint()) {
                    if (P == R_fixed.getFixedPoint()) {
                        return new VectorNull();
                    } else {
                        return new Derivation<>(Utils.getVector(R_fixed.getFixedPoint(), P), R).derive(R_fixed);
                    }
                } else {
                    return Addition.CreateVector(new Velocity(R.getFixedPoint(), R).calculate(R_fixed),
                            new WedgeProduct(Utils.getVector(R.getFixedPoint(), P), Utils.getProjectionVector(R, R_fixed)));
                }
            }
        }
        throw new Error("Impossible to calculate this velocity");

    }

}
