
package pkgboolean.solver;

import java.util.Stack;

/**
 *
 * @author Edilson
 */
public class TruthTable {

    private boolean[][] variablesTruthTables;
    private Function function;
  
    public TruthTable(Function f) throws InvalidDataException {
        if (f == null) {
            throw new InvalidDataException("Function is null");
        }
        
        this.function = f;
        this.variablesTruthTables = new boolean[this.function.getVariableCount()][(int) Math.pow(2, this.function.getVariableCount())];
        this.fillVariablesTruthTables();
        
        for (int i = 0; i < variablesTruthTables.length; i++) {
            for (int j = 0; j < variablesTruthTables[0].length; j++) {
                System.out.print(variablesTruthTables[i][j] + "\t");
            }
            System.out.println();
        }
    }
    
    private void fillVariablesTruthTables() {
        for (int i = 0; i < this.variablesTruthTables.length; i++) {
            boolean tmp = false;
            for (int j = 0; j < this.variablesTruthTables[0].length; j++) {
               if (j != 0 && j % (int) (Math.pow(2, this.function.getVariableCount() - i) / 2) == 0) {
                  tmp = !tmp;
               }
               
               this.variablesTruthTables[i][j] = tmp;
            }
        }
    }
    
    public Function getFunction() {
        return function;
    }
    
    public boolean[] getFunctionTruthTable() throws InvalidDataException {
        //SOP
        String[] minterms = this.function.getFunction().split("[+]");
        Stack<boolean[]> mintermsTables = new Stack();
        
        for (int i = 0; i < minterms.length; i++) { // Recorrer el mintérmino
            Stack<boolean[]> currentMinterm = new Stack();
            
            for (int j = 0; j < minterms[i].length(); j++) { // Recorrer la cadena del mintérmino 
                if (Character.isLetter(minterms[i].charAt(j))) {
                    if (j + 1 < minterms[i].length()) {
                        if (minterms[i].charAt(j + 1) == '\'') {
                            boolean[] tmp = new boolean[this.variablesTruthTables[0].length];
                            int pos = this.function.getVariables().indexOf(new Variable(minterms[i].charAt(j), false));
                            for (int k = 0; k < tmp.length; k++) {
                                tmp[k] = !this.variablesTruthTables[pos][k];
                            }
                            currentMinterm.push(tmp);
                        }
                    } else {
                        System.out.println(minterms[i].charAt(j) + "   " + minterms[i]);
                        int pos = this.function.getVariables().indexOf(new Variable(minterms[i].charAt(j), false));
                        currentMinterm.push(this.variablesTruthTables[pos]);
                    }
                }
            }
            
            while (currentMinterm.size() > 1) {
                boolean[] operand1, operand2, tmp;
                operand1 = currentMinterm.pop();
                operand2 = currentMinterm.pop();
                tmp = new boolean[operand1.length];
                
                for (int j = 0; j < operand1.length; j++) {
                    tmp[j] = operand1[j] && operand2[j];
                }
                
                currentMinterm.push(tmp);
            }
            
            mintermsTables.push(currentMinterm.pop());
        }
        
        while (mintermsTables.size() > 1) {
            boolean[] operand1, operand2, tmp;
            operand1 = mintermsTables.pop();
            operand2 = mintermsTables.pop();
            tmp = new boolean[operand1.length];
            
            for (int i = 0; i < operand1.length; i++) {
                tmp[i] = operand1[i] || operand2[i];
            }
            
            mintermsTables.push(tmp);
        }
        
        return mintermsTables.pop();
    }
}
