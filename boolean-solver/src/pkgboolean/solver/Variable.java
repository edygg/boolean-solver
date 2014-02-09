
package pkgboolean.solver;

/**
 *
 * @author Edilson
 */
public class Variable {
    
    private char value;
    private boolean not;

    public Variable(char value, boolean not) throws InvalidDataException {
        if (Character.isLetter(value)) {
            this.value = value;
        } else {
            throw new InvalidDataException("The value is not a letter");
        }
        
        this.not = not;
    }
    
    public boolean isNot() {
        return not;
    }

    public char getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (this.not) {
            return Character.toString(value) + "'";
        } else {
            return Character.toString(value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Variable) {
            return ((Variable) o).value == this.value && ((Variable) o).not == this.not;
        } 
        
        return false;
    }
    
    
}
