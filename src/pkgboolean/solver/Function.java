package pkgboolean.solver;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Edilson
 */
public class Function {

    private ArrayList<Variable> listVariables;
    private String function;

    public Function(String function) throws InvalidDataException {
        this.listVariables = new ArrayList();
        this.function = function;
        if (!this.validate()) {
            this.function = "";
            throw new InvalidDataException("Function not in SOP");
        }

        this.identifyVariables();
    }

    private boolean validate() {
        String sopPattern = "([a-zA-z][\\']{0,1}[\\+]{0,1})*";
        //SOP
        if (this.function.matches(sopPattern)) {
            return true;
        }

        this.function = "";
        return false;
    }

    private void identifyVariables() throws InvalidDataException {
        for (int i = 0; i < this.function.length(); i++) {
            Variable tmp = null;
            if (Character.isLetter(this.function.charAt(i))) {
                /* 
                 Solo es para identificar las variables no importa
                 si es negada o no
                 */
                tmp = new Variable(this.function.charAt(i), false);
            }
            if (!this.listVariables.contains(tmp) && tmp != null) {
                this.listVariables.add(tmp);
            }
        }
    }

    public ArrayList<Variable> getVariables() {
        ArrayList<Variable> ret = new ArrayList();
        for (int i = 0; i < this.listVariables.size(); i++) {
            ret.add(this.listVariables.get(i));
        }
        Collections.sort(ret);
        return ret;
    }

    public int getVariableCount() {
        return this.listVariables.size();
    }

    public String getFunction() {
        return function;
    }

    public void printTruthTable() {
        int rows = (int) Math.pow(2, this.getVariableCount());
        for (int i = 0; i < this.getVariableCount(); i++) {
            System.out.print(this.getVariables().get(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < rows; i++) {
            for (int j = this.getVariableCount() - 1; j >= 0; j--) {
                System.out.print((i / (int) Math.pow(2, j)) % 2 + " ");
            }
            System.out.println();
        }
    }
}
