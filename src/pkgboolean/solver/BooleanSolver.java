/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkgboolean.solver;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Edilson
 */
public class BooleanSolver {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        String functionInput = JOptionPane.showInputDialog("Ingrese la función: ");
        String number = JOptionPane.showInputDialog("Ingrese la cantidad de variables: ");
        try {
            Function function = new Function(functionInput, Integer.parseInt(number));
            System.out.println("Function: " + function.getFunction());
            System.out.println("# variables: " + function.getVariableCount());
            System.out.println("variables: " + function.getVariables());
            System.out.println("Minterms: " + function.getMinTerms());
            System.out.println("Maxterms: " + function.getMaxTerms());
            System.out.println("Don't Care: " + function.getDontCareTerms());
            TruthTable tt=new TruthTable(function);
            tt.printTruthTable();
            ArrayList<MinMaxTerm> mmt = tt.getDontCareTerms();
            for (int i = 0; i < mmt.size(); i++) {
                System.out.println(mmt.get(i).getTerm());
            }
            System.out.println("\n\n\n");
            MapK mp = new MapK(tt);
            String finalFunction = mp.simplifyaFunctionMinTerms(false);
            System.out.println(finalFunction);
            
        } catch (InvalidDataException ex) {
            ex.printStackTrace();
        }
    }
    
}
