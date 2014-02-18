package pkgboolean.solver;

import java.util.ArrayList;

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
        
        return ret;
    }
    
    public int getVariableCount() {
        return this.listVariables.size();
    }
    
    public String getFunction() {
        return function;
    }

    @Override
    public String toString() {
        return function;
    }
    
    
}
