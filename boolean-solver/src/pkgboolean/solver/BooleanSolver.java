/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkgboolean.solver;

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
        String functionInput = JOptionPane.showInputDialog(null);
        try {
            Function function = new Function(functionInput);
            TruthTable tt = new TruthTable(function);
            boolean[] result = tt.getFunctionTruthTable();
            
            for (int i = 0; i < result.length; i++) {
                System.out.print(result[i] + "\t");
            }
        } catch (InvalidDataException ex) {
            ex.printStackTrace();
        }
    }
    
}
