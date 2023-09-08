/*
 * Copyright (c) 2021-2023. Seb-sti1. See LICENSE.
 */

package fr.seb.latex;

import fr.seb.Expression;
import fr.seb.si.InertiaMatrix;
import fr.seb.space.Space;

import java.util.ArrayList;
import java.util.List;

public class LatexFile {

    List<String> userPackage;

    StringBuilder document;

    public LatexFile(String path, String name) {
        userPackage = new ArrayList<>();
        document = new StringBuilder();
    }

    public void addUserPackage(String userPack) {
        if (!userPackage.contains(userPack)) {
            userPackage.add(userPack);
        }
    }

    public <T> Expression<T> addCalculus(Expression<T> e) {
        return addResult(e.toString(), e.calcul());
    }

    public <T> Expression<T> addResult(String result, Expression<T> e) {
        this.addUserPackage("amsmath");

        Expression<T> ePrev = null;

        document.append("\\begin{align*}\n");

        document.append(result);

        while (!e.equals(ePrev)) {
            document.append("&=")
                    .append(e)
                    .append("\\\\")
                    .append("\n");

            ePrev = e;
            e = e.calcul();
        }

        document.append("\\end{align*}\n");

        return ePrev;
    }

    public void addProjectionFigures(List<Space> l) {
        addTabular(new ArrayList<>(l), 4);
    }

    public void addInertiaMatrix(List<InertiaMatrix> l) {
        List<Object> str = new ArrayList<>();

        for (InertiaMatrix I : l) {
            str.add("$" + I.toString() + "$");
        }
        addTabular(str, 6);
    }

    private void addTabular(List<Object> l, int column) {
        this.addUserPackage("tabular");

        int lines = l.size() / column;
        int k = 0;

        document.append("\\begin{tabular}{");

        for (int i = 0; i < column; i++) {
            document.append("c");
        }
        document.append("}\n");


        for (Object o : l) {
            document.append(o.toString());
            if (k % column != column - 1) {
                document.append(" & ");
            } else if (k / column != lines) {
                document.append("\\\\\n");
            }
            k++;
        }

        document.append("\\end{tabular}\n\n");
    }

    public void addVspace(int em) {
        document.append("\n\\vspace{").append(em).append("em}\n");
    }

    public String getDocument() {
        return document.toString();
    }
}
