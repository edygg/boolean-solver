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
    public static final String dontCareMaxTermPattern = "([p][\\(]([0-9]|[1-2][0-9]|[3][0-1])([\\,][0-9]|[\\,][1-2][0-9]|[\\,][3][0-1]){0,30}[\\)])([\\+][d][\\(]([0-9]|[1-2][0-9]|[3][0-1])([\\,][0-9]|[\\,][1-2][0-9]|[\\,][3][0-1]){0,30}[\\)]){0,1}";
    public static final String dontCareMinTermPattern = "([s][\\(]([0-9]|[1-2][0-9]|[3][0-1])([\\,][0-9]|[\\,][1-2][0-9]|[\\,][3][0-1]){0,30}[\\)])([\\+][d][\\(]([0-9]|[1-2][0-9]|[3][0-1])([\\,][0-9]|[\\,][1-2][0-9]|[\\,][3][0-1]){0,30}[\\)]){0,1}";

    private ArrayList<Variable> listVariables;
    private String function;

    public Function(String function, int numberVaribales) throws InvalidDataException {
        if (function.isEmpty()) {
            throw new InvalidDataException("Function is empty");
        }
        
        this.listVariables = new ArrayList();
        this.function = function;
        if (!this.validate()) {
            this.function = "";
            throw new InvalidDataException("Function not write in the correct format");
        }
        
        if (this.function.matches(sopPattern)) {
            this.identifyVariables();
        } else {
            if (!this.parseCompactFunction(numberVaribales)) {
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

    private boolean parseCompactFunction(int numberVariables) throws InvalidDataException {
        if (numberVariables > 5) {
            return false;
        }
        
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
        
        if (iterations > numberVariables) {
            return false;
        }
        
        int variableChar = 65;
        
        for (int i = 0; i < numberVariables; i++) {
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

    public boolean isSopPattern() {
        return this.function.matches(sopPattern);
    }

    public boolean isDontCareMaxTermsPattern() {
        return this.function.matches(dontCareMaxTermPattern);
    }

    public boolean isDontCareMinTermsPattern() {
        return this.function.matches(dontCareMinTermPattern);
    }

    public ArrayList<Integer> getMinTerms() {
        ArrayList<Integer> retList = new ArrayList();

        if (isSopPattern()) {
            return null;
        } else if (isDontCareMinTermsPattern()) {
            String[] parts = this.function.split("[+]");

            parts[0] = parts[0].replace("s(", "");
            parts[0] = parts[0].replace(")", "");

            String terms[] = parts[0].split(",");

            for (int i = 0; i < terms.length; i++) {
                retList.add(Integer.parseInt(terms[i]));
            }

            return retList;
        } else {
            String[] parts = this.function.split("[+]");
            TreeSet<Integer> termsSet = new TreeSet();

            parts[0] = parts[0].replace("p(", "");
            parts[0] = parts[0].replace(")", "");

            String terms[] = parts[0].split(",");

            for (int i = 0; i < terms.length; i++) {
                termsSet.add(Integer.parseInt(terms[i]));
            }
            
            if (parts.length == 2) {
                parts[1] = parts[1].replace("d(", "");
                parts[1] = parts[1].replace(")", "");

                String dontCareterms[] = parts[1].split(",");

                for (int i = 0; i < dontCareterms.length; i++) {
                    termsSet.add(Integer.parseInt(dontCareterms[i]));
                }
            }

            for (int i = 0; i < (int) Math.pow(2, this.getVariableCount()); i++) {
                if (!termsSet.contains(i)) {
                    retList.add(i);
                }
            }

            return retList;
        }
    }

    public ArrayList<Integer> getMaxTerms() {
        ArrayList<Integer> retList = new ArrayList();

        if (isSopPattern()) {
            return null;
        } else if (isDontCareMaxTermsPattern()) {
            String[] parts = this.function.split("[+]");

            parts[0] = parts[0].replace("p(", "");
            parts[0] = parts[0].replace(")", "");

            String terms[] = parts[0].split(",");

            for (int i = 0; i < terms.length; i++) {
                retList.add(Integer.parseInt(terms[i]));
            }

            return retList;
        } else {
            String[] parts = this.function.split("[+]");
            TreeSet<Integer> termsSet = new TreeSet();

            parts[0] = parts[0].replace("s(", "");
            parts[0] = parts[0].replace(")", "");

            String terms[] = parts[0].split(",");

            for (int i = 0; i < terms.length; i++) {
                termsSet.add(Integer.parseInt(terms[i]));
            }
            
            if (parts.length == 2) {
                parts[1] = parts[1].replace("d(", "");
                parts[1] = parts[1].replace(")", "");

                String dontCareterms[] = parts[1].split(",");

                for (int i = 0; i < dontCareterms.length; i++) {
                    termsSet.add(Integer.parseInt(dontCareterms[i]));
                }
            }


            for (int i = 0; i < (int) Math.pow(2, this.getVariableCount()); i++) {
                if (!termsSet.contains(i)) {
                    retList.add(i);
                }
            }
            
            return retList;
        }
    }

    public ArrayList<Integer> getDontCareTerms() {
        if (isSopPattern()) {
            return null;
        }
        
        String[] parts = this.function.split("[+]");
        ArrayList<Integer> retList = new ArrayList();
        
        if (parts.length == 2) {
            parts[1] = parts[1].replace("d(", "");
            parts[1] = parts[1].replace(")", "");

            String dontCareterms[] = parts[1].split(",");

            for (int i = 0; i < dontCareterms.length; i++) {
                retList.add(Integer.parseInt(dontCareterms[i]));
            }
        }
        
        return retList;
    }
}
