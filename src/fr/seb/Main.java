package fr.seb;

import fr.seb.function.*;
import fr.seb.si.Velocity;
import fr.seb.space.Space;
import fr.seb.space.FixedSpace;
import fr.seb.space.Space.VECTOR;
import fr.seb.space.SpinningSpace;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;
import fr.seb.vectors.Vector;

public class Main {

    public static void main(String[] args) {

        /*Variable theta = new Variable("\\theta", new boolean[]{false, false, true});
        Variable phi = new Variable("\\phi", new boolean[]{false, false, true});

        Variable lambda = new Variable("\\lambda", new boolean[]{false, true});
        Variable gamma = new Variable("\\gamma", new boolean[]{false, true});

        Space R0 = new FixedSpace();
        Space R1 = new SpinningSpace(R0, 1, R0.getFixedPoint(), Space.VECTOR.X, Space.VECTOR.X, theta, Space.VECTOR.Y, Space.VECTOR.Y);
        Space R2 = new SpinningSpace(R1, 2, R0.getFixedPoint(), Space.VECTOR.Y, Space.VECTOR.X, phi, Space.VECTOR.Z, Space.VECTOR.Y);

        Vector x2 = R2.getUnitaryVector(Space.VECTOR.X);
        Vector y2 = R2.getUnitaryVector(Space.VECTOR.Y);

        Expression<Vector> expr = new Addition<>(new ScalarProduct(lambda, x2), new ScalarProduct(gamma, y2));

        p(new Derivation<>(expr, R0));
        pl("\\\\");

        for (int i = 1; i < 4; i++) {
            p(expr.derive(i, R0));
            pl("\\\\");
        }

        pl(expr.derive(R0).calcul());*/

        Variable alpha = new Variable("\\alpha", new boolean[]{false, false, false, true});
        Variable beta = new Variable("\\beta", new boolean[]{false, false, false, true});

        Variable a = new Variable("a", new boolean[]{true});
        Variable b = new Variable("b", new boolean[]{true});

        Space R0 = new FixedSpace();
        Space R1 = new SpinningSpace(R0, 1, R0.getFixedPoint(), VECTOR.X, VECTOR.X, alpha, VECTOR.Y, VECTOR.Y);

        Point A = new Point("A", R0.getFixedPoint(), new ScalarProduct(a, R1.getUnitaryVector(VECTOR.Y)));

        Space R2 = new SpinningSpace(R1, 2, A, VECTOR.Z, VECTOR.Z, beta, VECTOR.X, VECTOR.X);

        Point G = new Point("G", A, new ScalarProduct(b, R2.getUnitaryVector(VECTOR.X)));

        Velocity v = new Velocity(G, R2);
        Expression<Vector> velocityExpression = v.calculate(R0).calcul();
        pl(velocityExpression);

        pl("");

        pl(velocityExpression.derive(R0).calcul());

    }

    public static void p(Object o) {
        System.out.print(o.toString());
    }

    public static void pl(Object o) {
        System.out.println(o.toString());
    }
}
