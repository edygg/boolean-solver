package pkgboolean.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

/**
 *
 * @author Edilson
 */
public class Function {

    public static final String sopPattern = "([a-zA-z][\\']{0,1})*([\\+]([a-zA-z][\\']{0,1})*)*";
    public static final String dontCarePattern = "([s|p][\\(]([0-9]|[1-2][0-9]|[3][0-1])([\\,][0-9]|[\\,][1-2][0-9]|[\\,][3][0-1]){0,30}[\\)])([\\+][d][\\(]([0-9]|[1-2][0-9]|[3][0-1])([\\,][0-9]|[\\,][1-2][0-9]|[\\,][3][0-1]){0,30}[\\)]){0,1}";
    private ArrayList<Variable> listVariables;
    private String function;

    public Function(String function) throws InvalidDataException {
        this.listVariables = new ArrayList();
        this.function = function;
        if (!this.validate()) {
            this.function = "";
            throw new InvalidDataException("Function not write in the correct format");
        }

        if (this.function.matches(sopPattern)) {
            this.identifyVariables();
        } else {
            if (!this.parseCompactFunction()) {
                throw new InvalidDataException("Please check the function again");
            }
        }
        Collections.sort(this.listVariables);
    }

    private boolean validate() {
        //SOP
        if (this.function.matches(sopPattern)) {
            return true;
        }

        //Dont Care
        if (this.function.matches(dontCarePattern)) {
            return true;
        }

        this.function = "";
        return false;
    }

    private boolean parseCompactFunction() throws InvalidDataException {
        String[] parts = this.function.split("[+]");
        TreeSet<Integer> termsSet = new TreeSet();

        parts[0] = parts[0].replace("s(", "");
        parts[0] = parts[0].replace("p(", "");
        parts[0] = parts[0].replace(")", "");

        String terms[] = parts[0].split(",");

        for (int i = 0; i < terms.length; i++) {
            if (!termsSet.contains(Integer.parseInt(terms[i]))) {
                termsSet.add(Integer.parseInt(terms[i]));
            } else {
                return false;
            }
        }
        
        if (parts.length == 2) {
            parts[1] = parts[1].replace("d(", "");
            parts[1] = parts[1].replace(")", "");
            
            String dontCareterms[] = parts[1].split(",");

            for (int i = 0; i < dontCareterms.length; i++) {
                if (!termsSet.contains(Integer.parseInt(dontCareterms[i]))) {
                    termsSet.add(Integer.parseInt(dontCareterms[i]));
                } else {
                    return false;
                }
            }
        }
        
        int highestTerm = termsSet.last();
        int iterations = 1;
        if (highestTerm < 2) {
            iterations = 1;
        } else if (highestTerm < 4) {
            iterations = 2;
        } else if (highestTerm < 8) {
            iterations = 3;
        } else if (highestTerm < 16) {
            iterations = 4;
        } else if (highestTerm < 32) {
            iterations = 5;
        } else {
            //No se pueden mas de 5 variables para esta nomenclatura
            return false;
        }

        int variableChar = 65;

        for (int i = 0; i < iterations; i++) {
            this.listVariables.add(new Variable((char) variableChar, false));
            variableChar++;
        }

        return true;
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
