
package pkgboolean.solver;

import java.util.ArrayList;

/**
 *
 * @author Edilson
 */
public class MapK {
    
    private TruthTable mainTable;
    
    public MapK(TruthTable table) {
        this.mainTable = table;
    }
    
    public Function simplifyaFunctionMinTerms() {
        
        ArrayList<MinMaxTerm> ftable = this.mainTable.mintables();
        int countDifferences = 1;
        boolean findPattern = true;
        
        while (findPattern && countDifferences < this.mainTable.getFunction().getVariableCount()) {
            ArrayList<MinMaxTerm> neoList = new ArrayList();
            findPattern = false;
            for (int i = 0; i < ftable.size(); i++) {
                MinMaxTerm temp = ftable.get(i);

                for (int j = i + 1; j < ftable.size(); j++) {
                    MinMaxTerm neoPattern = temp.compareTerms(ftable.get(j));
                    if (neoPattern != null && neoList.indexOf(neoPattern) == -1) {
                        findPattern = true;
                        neoList.add(neoPattern);
                    }
                }
            }
            
            if (!neoList.isEmpty()) {
                ftable = neoList;
                countDifferences++;
            }
        }
        
        for (int i = 0; i < ftable.size(); i++) {
            System.out.println(ftable.get(i));
        }
              
        return null;
    }
    
    public TruthTable getTable() {
        return this.mainTable;
    }
}
