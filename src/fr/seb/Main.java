package fr.seb;

import fr.seb.function.Addition;
import fr.seb.function.Scalar;
import fr.seb.function.ScalarProduct;
import fr.seb.si.Acceleration;
import fr.seb.space.FixedSpace;
import fr.seb.space.Space;
import fr.seb.space.Space.VECTOR;
import fr.seb.space.SpinningSpace;
import fr.seb.vectors.Point;
import fr.seb.vectors.Variable;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        /*Variable H = new Variable("H", new boolean[]{true});
        Variable e = new Variable("e", new boolean[]{true});

        Variable x = new Variable("x", new boolean[]{true});

        x.invertSign();

        Scalar un = new Scalar(1);
        Scalar mun = new Scalar(-1);
        Scalar deux = new Scalar(2);

        //Expression<Variable> p = Product.Create(x, mun);

        Expression<Variable> a = new Addition<>(Arrays.asList(H, e, x));

        pl(a);

        a.invertSign();

        pl(a);

        pl("");*/



        Variable alpha = new Variable("\\alpha", new boolean[]{false, false, false, true});
        Variable beta = new Variable("\\beta", new boolean[]{false, false, false, true});

        Variable H = new Variable("H", new boolean[]{true});
        Variable e = new Variable("e", new boolean[]{true});
        Variable L = new Variable("L", new boolean[]{true});
        Variable X = new Variable("X", new boolean[]{false, false, false, false, true});
        Variable Z = new Variable("Z", new boolean[]{false, false, false, false, true});

        Space R0 = new FixedSpace();
        Space R1 = new SpinningSpace(R0, 1, R0.getFixedPoint(), VECTOR.Z, VECTOR.Z, alpha, VECTOR.X, VECTOR.X);

        Expression<Variable> a = new Addition<>(Arrays.asList(L, e));

        Point A = new Point("A", R0.getFixedPoint(), Addition.CreateVector(new ScalarProduct(H, R0.getUnitaryVector(VECTOR.Z)),
                                                                        new ScalarProduct(e, R1.getUnitaryVector(VECTOR.Y))));
        Point B = new Point("B", A, new ScalarProduct(L, R1.getUnitaryVector(VECTOR.X)));
        Point C = new Point("C", B, new ScalarProduct(X, R1.getUnitaryVector(VECTOR.X)));

        Space R2 = new SpinningSpace(R1, 2, A, VECTOR.X, VECTOR.X, beta, VECTOR.Y, VECTOR.Y);

        Space R3 = new SpinningSpace(R2, 3, B, VECTOR.X, VECTOR.X, new Scalar(0), VECTOR.Y, VECTOR.Y);
        Space R4 = new SpinningSpace(R3, 4, C, VECTOR.X, VECTOR.X, new Scalar(0), VECTOR.Y, VECTOR.Y);


        Point G4 = new Point("G4", C, new ScalarProduct(Z, R2.getUnitaryVector(VECTOR.Z)));

        Acceleration TRDQ2 = new Acceleration(G4, R4, R0);


        pl(TRDQ2.calculate().calcul().calcul().calcul().calcul().calcul());
        pl("");
        pl(Utils.dotProduct(TRDQ2.calculate().calcul().calcul().calcul().calcul().calcul(), R2.getUnitaryVector(VECTOR.Z)));


    }

    public static void p(Object o) {
        System.out.print(o.toString());
    }

    public static void pl(Object o) {
        System.out.println(o.toString());
    }
}
