/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgboolean.solver;

import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class TruthTable {

    private Function function;
    private int table[][];
    private int table_function[];

    public TruthTable(Function function) {
        this.function = function;
        int temp, k;
        table = new int[(int) Math.pow(2, this.function.getVariableCount())][this.function.getVariableCount()];
        table_function = new int[(int) Math.pow(2, this.function.getVariableCount())];
        for (int i = 0; i < (int) Math.pow(2, this.function.getVariableCount()); i++) {
            k = 0;
            for (int j = this.function.getVariableCount() - 1; j >= 0; j--) {

                temp = (int) ((i / (int) Math.pow(2, j)) % 2);
                table[i][k] = temp;
                ++k;
            }

        }
        for (int i = 0; i < (int) Math.pow(2, this.function.getVariableCount()); i++) {
            this.table_function[i] = 0;
        }
        if (this.function.isSopPattern()) {
            solve_function();
        } else {
            ArrayList<Integer> terms = this.function.getMinTerms();
            
            for (int i = 0; i < terms.size(); i++) {
                table_function[terms.get(i)] = 1;
            }
            
            ArrayList<Integer> dontCareTerms = this.function.getDontCareTerms();
            
            for (int i = 0; i < dontCareTerms.size(); i++) {
                table_function[dontCareTerms.get(i)] = 2;
            }
        }
    }

    public Function getFunction() {
        return function;
    }

    public void printTruthTable() {
        int rows = (int) Math.pow(2, this.function.getVariableCount());
        for (int i = 0; i < this.function.getVariableCount(); i++) {
            System.out.print(this.function.getVariables().get(i) + " ");
        }
        System.out.print("Function" + " ");
        System.out.println();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < this.function.getVariableCount() + 1; j++) {
                if (j == this.function.getVariableCount()) {
                    System.out.print(this.table_function[i]);
                } else {
                    System.out.print(this.table[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private void solve_function() {
        String subs[] = this.function.getFunction().split("\\+");
        int temp_f, temp, rows = 0;
        String temp_sub;
        while (rows < (int) Math.pow(2, this.function.getVariableCount())) {
            temp_f = 0;
            for (int i = 0; i < subs.length; i++) {
                temp_sub = subs[i];
                temp = 1;
                for (int j = 0; j < subs[i].length(); j++) {
                    if (j != subs[i].length() - 1 && temp_sub.charAt(j + 1) == '\'') {
                        if (getNum(rows, temp_sub.charAt(j)) == 1) {
                            temp *= 0;
                        }
                        ++j;
                    } else {
                        temp *= getNum(rows, temp_sub.charAt(j));
                    }
                }
                //System.out.println("El temp "+ temp_sub+" termina en : "+ temp);
                temp_f += temp;
                //System.out.println("Temp final_"+rows +" ="+temp_f);
            }
            if (temp_f >= 1) {
                this.table_function[rows] = 1;
            } else {
                this.table_function[rows] = 0;
            }
            ++rows;
        }
    }

    private int getNum(int row, char var) {
        int i;
        ArrayList<Variable> list = this.function.getVariables();
        for (i = 0; i < list.size(); i++) {
            if (var == list.get(i).getValue()) {
                break;
            }
        }
        return this.table[row][i];
    }
    
    public ArrayList<MinMaxTerm> mintables() {
        ArrayList<MinMaxTerm> mintable = new ArrayList();
        ArrayList<Integer> pos = new ArrayList();
        for (int i = 0; i < this.table_function.length; i++) {
            if (this.table_function[i] == 1 || this.table_function[i] == 2) {
                pos.add(i);
            }
        }
        String temp;
        for (int i = 0; i < pos.size(); i++) {
            temp = "";
            for (int j = 0; j < this.function.getVariableCount(); j++) {
                temp += Integer.toString(this.table[pos.get(i)][j]);
            }
            ArrayList<Integer> novo_list = new ArrayList();
            novo_list.add(pos.get(i));
            MinMaxTerm term = new MinMaxTerm(temp, true, novo_list);
            mintable.add(term);
        }

        return mintable;
    }

    public ArrayList<MinMaxTerm> maxtables() {
        ArrayList<MinMaxTerm> mintable = new ArrayList();
        ArrayList<Integer> pos = new ArrayList();
        for (int i = 0; i < this.table_function.length; i++) {
            if (this.table_function[i] == 0 || this.table_function[i] == 2) {
                pos.add(i);
            }
        }
        String temp;
        for (int i = 0; i < pos.size(); i++) {
            temp = "";
            for (int j = 0; j < this.function.getVariableCount(); j++) {
                temp += Integer.toString(this.table[pos.get(i)][j]);
            }
            ArrayList<Integer> novo_list = new ArrayList();
            novo_list.add(pos.get(i));
            MinMaxTerm term = new MinMaxTerm(temp, false, novo_list);
            mintable.add(term);
        }

        return mintable;
    }
}
