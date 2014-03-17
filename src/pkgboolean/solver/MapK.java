
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
        ArrayList<MinMaxTerm> prime_implicant_chart=new ArrayList();
        ArrayList<MinMaxTerm> ftable = this.mainTable.mintables();
        boolean withoutP = false;
        boolean eslct=false;
        
        while (!withoutP) {
            ArrayList<MinMaxTerm> neoList = new ArrayList();
            for (int i = 0; i < ftable.size(); i++) {
                MinMaxTerm temp = ftable.get(i);
                
                for (int j = i + 1; j < ftable.size(); j++) {
                    MinMaxTerm neoPattern = temp.compareTerms(ftable.get(j));
                    if (neoPattern != null) {
                        temp.setParticipate(true);
                        ftable.get(j).setParticipate(true);
                        if (neoList.indexOf(neoPattern) == -1) {
                            neoList.add(neoPattern);
                        }
                    }
                }
            }
            
            withoutP = true;
            
            for (int i = 0; i < ftable.size(); i++) {
                if (!ftable.get(i).isParticipate()) {
                    prime_implicant_chart.add(ftable.get(i));
                } else {
                    withoutP = false;
                }
            }
            
            if (!neoList.isEmpty()) {
                ftable = neoList;
            }
            
        }
        
        for (int i = 0; i < prime_implicant_chart.size(); i++) {
            System.out.println(prime_implicant_chart.get(i).getTerm());
        }
              
        return null;
    }
    
    public TruthTable getTable() {
        return this.mainTable;
    }
}
