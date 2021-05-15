package fr.seb;

import fr.seb.function.Addition;
import fr.seb.function.Product;
import fr.seb.function.Scalar;
import fr.seb.vectors.Variable;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {


        /*
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
*/

    }

    public static void tests() {
        Variable x = new Variable("x", 1);
        x.invertSign();
        pl(x);

        Scalar mun = new Scalar(-1);
        Expression<Variable> p = Product.Create(x, mun);
        pl(x);
        pl(p);

        Variable H = new Variable("H", 1);
        Variable e = new Variable("e", 1);

        Addition<Variable> a = new Addition<>(Arrays.asList(H, e, H, x));
        pl(a);
        Addition<Variable> b = new Addition<>(Arrays.asList(e, H, x));
        pl(b);

        Addition<Variable> c = Addition.CreateVariable(a, b);
        pl(c);

        Product d = Product.Create(a, b);
        pl(d);

        pl("");
    }

    public static void p(Object o) {
        System.out.print(o.toString());
    }

    public static void pl(Object o) {
        System.out.println(o.toString());
    }

    public static void pl(Object o, boolean dollars) {
        System.out.printf("$%s$\n\n", o.toString());
    }
}
