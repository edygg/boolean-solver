
package pkgboolean.solver;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Edilson
 */
public class Function {
    private ArrayList<Variable> listVariables;
    private Stack<Variable> variables;
    private Stack<Character> operators;
    private String function;

    public Function(String function) throws InvalidDataException {
        this.function = function;
        if (!this.validate()) {
            this.function = "";
            throw new InvalidDataException("Function not in SOP or POS");
        }
    }
    
    private final boolean validate() {
        String sopPattern = "[a-zA-z()+*]{1,}";
        
        if (this.function.matches(function)) {
           return true; 
        }
        
        return false;
    }
    
    public String getFunction() {
        return function;
    }

}
