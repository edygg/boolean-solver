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
            throw new InvalidDataException("Function not write in the correct format");
        }

        this.identifyVariables();
        Collections.sort(this.listVariables);
    }

    private boolean validate() {
        String sopPattern = "([a-zA-z][\\']{0,1}[\\+]{0,1})*";
        //SOP
        if (this.function.matches(sopPattern)) {
            return true;
        }
        
        String dontCarePattern = "([s|p|d][(]([0-31][\\\\,]{0,1})*[)])";
        //Dont Care
        if (this.function.matches(dontCarePattern)) {
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
        return this.listVariables;
    }

    public int getVariableCount() {
        return this.listVariables.size();
    }

    public String getFunction() {
        return function;
    }

}
