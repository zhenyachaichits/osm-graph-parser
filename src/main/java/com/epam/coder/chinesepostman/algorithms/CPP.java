package com.epam.coder.chinesepostman.algorithms;

import com.epam.coder.model.SparseGraph;

import java.util.ArrayList;

public abstract class CPP {

    protected final SparseGraph sg;
    protected ArrayList<String> output;

    public CPP(SparseGraph sg) {
        this.sg = sg;
        this.output = new ArrayList<>();
    }

    public abstract void perform();

    public void printOut() {
        int line = 0;
        for (String s : output) {
            System.out.print(" ->");
            System.out.print(s);
            for (int i = 0; i < 4 - s.length(); i++) {
                System.out.print(" ");
            }
            
            line++;
            if (line == 10) {
                System.out.println();
                line = 0;
            }
        }
        System.out.println("Way cost" + sg.calculateWayCost(output));
    }

}
