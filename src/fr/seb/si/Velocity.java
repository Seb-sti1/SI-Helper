package fr.seb.si;

import fr.seb.Expression;
import fr.seb.Utils;
import fr.seb.function.Addition;
import fr.seb.function.Derivation;
import fr.seb.function.WedgeProduct;
import fr.seb.space.Space;
import fr.seb.vectors.Point;
import fr.seb.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

public class Velocity {

    Space R_in;
    Space R_fixed;
    Point P;

    public Velocity(Point P, Space R_in, Space R_fixed) {
        this.P = P;
        this.R_in = R_in;
        this.R_fixed = R_fixed;
    }

    public List<Velocity> getDecomposition() {
        Space R_start = R_in;
        List<Velocity> list = new ArrayList<>();

        while (R_start != R_fixed) {
            list.add(new Velocity(P, R_start, R_start.getFather()));

            R_start = R_start.getFather();
        }

        return list;
    }

    public Expression<Vector> calculate() {
        if (R_in.getFathers().contains(R_fixed)) {

            if (R_in.getFather() != R_fixed) {
                List<Expression<Vector>> list = new ArrayList<>();

                for (Velocity vdec : getDecomposition()) {
                    list.add(vdec.calculate());
                }

                return Addition.CreateVector(list);
            } else {
                Expression<Vector> velocity = new Derivation<>(Utils.getVector(R_fixed.getFixedPoint(), R_in.getFixedPoint()), R_fixed);

                if (P != R_in.getFixedPoint()) {
                    velocity = Addition.CreateVector(velocity, new WedgeProduct(Utils.getVector(P, R_in.getFixedPoint()), Utils.getProjectionVector(R_in, R_fixed)));
                }

                return velocity;
            }
        }
        throw new Error("Impossible to calculate this velocity");
    }

    @Override
    public String toString() {
        return String.format("\\overrightarrow{V_{%s \\in %s/%s}}", P.toString(), R_in.getId(), R_fixed.getId());
    }
}
