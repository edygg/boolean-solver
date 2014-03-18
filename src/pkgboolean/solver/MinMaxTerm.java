/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgboolean.solver;

import java.util.ArrayList;

/**
 *
 * @author Edilson
 */
public class MinMaxTerm {

    private ArrayList<Integer> positions;
    private String term;
    private boolean min;    
    private boolean participate;

    public MinMaxTerm(String term, boolean min, ArrayList<Integer> positions) {
        this.term = term;
        this.min = min;
        this.positions = positions;
    }

    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public String getTerm() {
        return this.term;
    }

    public boolean isMin() {
        return min;
    }

    public boolean searchTerm(int pos) {
        return this.positions.indexOf(pos) != -1;
    }
    
    public boolean isParticipate() {
        return participate;
    }

    public void setParticipate(boolean participate) {
        this.participate = participate;
    }

    public MinMaxTerm compareTerms(MinMaxTerm other) {
        String str1 = this.term;
        String str2 = other.term;

        if (str1.length() != str2.length()) {
            return null;
        }

        ArrayList<Integer> terms1 = this.positions;
        ArrayList<Integer> terms2 = other.positions;

        for (int i = 0; i < terms1.size(); i++) {
            if (terms2.indexOf(terms1.get(i)) != -1) {
                return null;
            }
        }

        String ret = "";
        int count = 0;
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                ret += "-";
                count++;
            } else {
                ret += Character.toString(str1.charAt(i));
            }
        }
        
        if (count != 1) {
            return null;
        }
        
        boolean onlyDashes = true;
        for (int i = 0; i < ret.length(); i++) {
            if (ret.charAt(i) != '-') {
                onlyDashes = false;
                break;
            }
        }
        if (!onlyDashes) {
            ArrayList<Integer> neoList = new ArrayList();

            for (int i = 0; i < terms1.size(); i++) {
                neoList.add(terms1.get(i));
            }

            for (int i = 0; i < terms2.size(); i++) {
                neoList.add(terms2.get(i));
            }

            return new MinMaxTerm(ret, min, neoList);
        } else {
            return null;
        }

    }

    @Override
    public String toString() {
        return term;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        return this.term.equals(((MinMaxTerm) obj).term);
    }

}
