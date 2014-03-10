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
        solve_function();
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void printTruthTable() {
        int rows = (int) Math.pow(2, this.function.getVariableCount());
        for (int i = 0; i < this.function.getVariableCount(); i++) {
            System.out.print(this.function.getVariables().get(i) + " ");
        }
        System.out.print("Function" + " ");
        System.out.println();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < this.function.getVariableCount()+1; j++) {
                if (j==this.function.getVariableCount()) {
                    System.out.print(this.table_function[i]);
                }
                else{
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
                            temp*=0;
                        }
                        ++j;
                    } else {
                        temp*=getNum(rows,temp_sub.charAt(j));
                    }
                }
                //System.out.println("El temp "+ temp_sub+" termina en : "+ temp);
                temp_f += temp;
                //System.out.println("Temp final_"+rows +" ="+temp_f);
            }
            if (temp_f>=1) {
                this.table_function[rows]=1;
            }
            else{
                this.table_function[rows]=0;
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
    
        public ArrayList<String> mintables(){
        
        ArrayList<Integer> pos=new ArrayList();
        for (int i = 0; i < this.table_function.length; i++) {
            if (this.table_function[i] == 1) {
                pos.add(i);
            }
        }
        ArrayList<String> rtable=new ArrayList();
        String temp;
        for (int i = 0; i < pos.size(); i++) {
            temp="";
            for (int j = 0; j < this.function.getVariableCount() ; j++) {
                temp+=Integer.toString(this.table[pos.get(i)][j]);
            }
            rtable.add(temp);
        }
        for (int i = 0; i < rtable.size(); i++) {
            System.out.println(rtable.get(i));
        }
        
        return rtable;
    }
    
        public ArrayList<String> maxtables(){
        
        ArrayList<Integer> pos=new ArrayList();
        for (int i = 0; i < this.table_function.length; i++) {
            if (this.table_function[i] == 0) {
                pos.add(i);
            }
        }
        ArrayList<String> rtable=new ArrayList();
        String temp;
        for (int i = 0; i < pos.size(); i++) {
            temp="";
            for (int j = 0; j < this.function.getVariableCount() ; j++) {
                temp+=Integer.toString(this.table[pos.get(i)][j]);
            }
            rtable.add(temp);
        }
        for (int i = 0; i < rtable.size(); i++) {
            System.out.println(rtable.get(i));
        }
        
        return rtable;
    }
}
