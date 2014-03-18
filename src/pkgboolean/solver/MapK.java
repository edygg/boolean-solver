package pkgboolean.solver;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Edilson
 */
public class MapK {

    private TruthTable mainTable;

    public MapK(TruthTable table) {
        this.mainTable = table;
    }

    public String simplifyaFunctionMinTerms(boolean withMinTerms) throws InvalidDataException {
        /*Step 1*/
        ArrayList<MinMaxTerm> prime_implicant_chart = new ArrayList();
        ArrayList<MinMaxTerm> ftable = null;
        
        if (withMinTerms) {
            ftable = this.mainTable.mintables();
        } else {
            ftable = this.mainTable.maxtables();
        }
        
        boolean withoutP = false;
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
                    ArrayList<MinMaxTerm> dcs = this.mainTable.getDontCareTerms();
                    MinMaxTerm temporal = ftable.get(i);
                    if (dcs.indexOf(temporal) == -1) {
                        prime_implicant_chart.add(ftable.get(i));
                    }

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

        /*Step 2*/
        ArrayList<MinMaxTerm> final_form = new ArrayList();
        /*STEP 2.1 PIC*/
        ArrayList<Integer> pic_column = new ArrayList();
        ArrayList<Integer> temp;
        for (int i = 0; i < prime_implicant_chart.size(); i++) {
            temp = prime_implicant_chart.get(i).getPositions();
            for (int j = 0; j < temp.size(); j++) {
                if (pic_column.indexOf(temp.get(j)) == -1) {
                    pic_column.add(temp.get(j));
                }
            }
        }
        Collections.sort(pic_column);
        int pic_rows = prime_implicant_chart.size();
        int pic_columns = pic_column.size();
        int temp_2;
        char pic_table[][] = new char[pic_rows][pic_columns];
        for (int i = 0; i < pic_rows; i++) {
            for (int j = 0; j < pic_columns; j++) {
                pic_table[i][j] = '0';
            }
        }
        for (int i = 0; i < pic_rows; i++) {
            for (int j = 0; j < prime_implicant_chart.get(i).getPositions().size(); j++) {
                temp_2 = pic_column.indexOf(prime_implicant_chart.get(i).getPositions().get(j));
                if (temp_2 != -1) {
                    pic_table[i][temp_2] = 'X';
                }
            }
        }
        for (int i = 0; i < pic_columns; i++) {
            System.out.print(pic_column.get(i) + "\t");
        }
        System.out.println("");
        for (int i = 0; i < pic_rows; i++) {
            for (int j = 0; j < pic_columns; j++) {
                System.out.print(pic_table[i][j] + "\t");
            }
            System.out.println("");
        }
        /*STEP 2.1 PIC*/
        /*Quitar los Don't Cares(no aplica para el siguiente procedimiento)*/
        /*STEP 2.2 PIC*/
        //encontrando primos esenciales
        for (int i = 0; i < pic_columns; i++) {
            int count = 0;
            int essential_row = -1;
            for (int j = 0; j < pic_rows; j++) {
                if (pic_table[j][i] == 'X') {
                    count++;
                    essential_row = j;
                }
            }

            if (count == 1) {
                if (final_form.indexOf(prime_implicant_chart.get(essential_row)) == -1) {
                    final_form.add(prime_implicant_chart.get(essential_row));

                    for (int ch = 0; ch < pic_columns; ch++) {
                        if (pic_table[essential_row][ch] == 'X') {
                            for (int j = 0; j < pic_rows; j++) {
                                pic_table[j][ch] = '-';
                            }
                        }
                        pic_table[essential_row][ch] = '-';
                    }
                }
            }
        }
        System.out.println("\n\n");

        for (int i = 0; i < pic_columns; i++) {
            System.out.print(pic_column.get(i) + "\t");
        }
        System.out.println("");
        for (int i = 0; i < pic_rows; i++) {
            for (int j = 0; j < pic_columns; j++) {
                System.out.print(pic_table[i][j] + "\t");
            }
            System.out.println("");
        }

        for (int i = 0; i < final_form.size(); i++) {
            System.out.println(final_form.get(i).getTerm());
        }
        /*STEP 2.2 PIC*/
        /*STEP 2.3 PIC*/
        ArrayList<Integer> pic_col_2;
        ArrayList<MinMaxTerm> pic_row_2;
        char pic_temp[][];
        boolean insert;

        pic_col_2 = new ArrayList();
        pic_row_2 = new ArrayList();
        for (int i = 0; i < pic_rows; i++) {
            insert = true;
            for (int j = 0; j < pic_columns; j++) {
                if (pic_table[i][j] == 'X') {
                    if (pic_col_2.indexOf(pic_column.get(j)) == -1) {
                        pic_col_2.add(pic_column.get(j));

                    }
                    if (insert) {
                        pic_row_2.add(prime_implicant_chart.get(i));
                        insert = false;
                    }
                }
            }
        }
        Collections.sort(pic_col_2);
        do {

            pic_temp = new char[pic_row_2.size()][pic_col_2.size()];
            for (int i = 0; i < pic_row_2.size(); i++) {
                for (int j = 0; j < pic_col_2.size(); j++) {
                    pic_temp[i][j] = '0';
                }
            }

            for (int i = 0; i < pic_row_2.size(); i++) {
                ArrayList<Integer> tem = pic_row_2.get(i).getPositions();
                for (int j = 0; j < tem.size(); j++) {
                    if (pic_col_2.indexOf(tem.get(j)) != -1) {
                        pic_temp[i][pic_col_2.indexOf(tem.get(j))] = 'X';
                    }
                }
            }
            System.out.println("\n\nNUEVO\n");
            for (int i = 0; i < pic_col_2.size(); i++) {
                System.out.print(pic_col_2.get(i) + "\t");
            }
            System.out.println("");
            for (int i = 0; i < pic_row_2.size(); i++) {
                for (int j = 0; j < pic_col_2.size(); j++) {
                    System.out.print(pic_temp[i][j] + "\t");
                }
                System.out.println("");
            }
            //ultimo caso cuando queden todos en la misma columna, por lo tanto hay que escoger uno
            //encontrando primos esenciales
            ArrayList<Integer> max_count = novo_pic_rows(pic_temp, pic_row_2.size(), pic_col_2.size());
            for (int i = 0; i < max_count.size(); i++) {
                final_form.add(pic_row_2.get(max_count.get(i)));
            }
            System.out.println("\nMAX COUNT: " + max_count);

            for (int i = 0; i < max_count.size(); i++) {

                for (int k = 0; k < pic_col_2.size(); k++) {
                    if (pic_temp[max_count.get(i)][k] == 'X') {
                        for (int j = 0; j < pic_row_2.size(); j++) {
                            pic_temp[j][k] = '-';
                        }
                    }
                    pic_temp[max_count.get(i)][k] = '-';
                }
            }

            System.out.println("\n\nNUEVO\n");
            for (int i = 0; i < pic_col_2.size(); i++) {
                System.out.print(pic_col_2.get(i) + "\t");
            }
            System.out.println("");
            for (int i = 0; i < pic_row_2.size(); i++) {
                for (int j = 0; j < pic_col_2.size(); j++) {
                    System.out.print(pic_temp[i][j] + "\t");
                }
                System.out.println("");
            }
            pic_col_2 = new ArrayList();
            pic_row_2 = new ArrayList();
            pic_rows = pic_row_2.size();
            pic_columns = pic_col_2.size();
            for (int i = 0; i < pic_rows; i++) {
                insert = true;
                for (int j = 0; j < pic_columns; j++) {
                    if (pic_temp[i][j] == 'X') {
                        if (pic_col_2.indexOf(pic_column.get(j)) == -1) {
                            pic_col_2.add(pic_column.get(j));

                        }
                        if (insert) {
                            pic_row_2.add(prime_implicant_chart.get(i));
                            insert = false;
                        }
                    }
                }
            }
            Collections.sort(pic_col_2);

        } while (scanMatrix(pic_temp, pic_row_2.size(), pic_col_2.size()));
        /*STEP 2.3 PIC*/
        System.out.println("\n\nFINAL FORM\n\n");
        for (int i = 0; i < final_form.size(); i++) {
            System.out.println(final_form.get(i).getTerm());
        }
        
        String final_function = "";
        
        int variableLetter = 65;
        ArrayList<Variable> functionVariables = new ArrayList();
        for (int i = 0; i < (int) (Math.log(this.mainTable.getTableFunction().length) / Math.log(2)); i++) {
            functionVariables.add(new Variable((char) variableLetter, false));
            variableLetter++;
        }
        
        if (withMinTerms) {
            for (int i = 0; i < final_form.size(); i++) {
                String term = final_form.get(i).getTerm();
                for (int j = 0; j < term.length(); j++) {
                    if (term.charAt(j) != '-') {
                        if (term.charAt(j) == '1') {
                            final_function += functionVariables.get(j).toString();
                        } else {
                            final_function += functionVariables.get(j).denyVariable().toString();
                        }
                    }
                }
                
                if (i < final_form.size() - 1) {
                    final_function += "+";
                }
            }
        } else {
            for (int i = 0; i < final_form.size(); i++) {
                String term = final_form.get(i).getTerm();
                ArrayList<String> tmp = new ArrayList();
                
                final_function += "(";
                for (int j = 0; j < term.length(); j++) {
                    if (term.charAt(j) != '-') {
                        if (term.charAt(j) == '1') {
                            tmp.add(functionVariables.get(j).denyVariable().toString());
                        } else {
                            tmp.add(functionVariables.get(j).toString());
                        }
                    }
                }
                
                if (tmp.size() == 1) {
                    final_function += tmp.get(0);
                } else {
                    for (int j = 0; j < tmp.size(); j++) {
                        if (j < tmp.size() - 1) {
                            final_function += tmp.get(j) + "+";
                        } else {
                            final_function += tmp.get(j);
                        }
                    }
                }
                
                final_function += ")";
            }
        }
        
        return final_function;
    }

    private ArrayList<Integer> exists_implicants(char mat[][], int rows, int columns) {
        ArrayList<Integer> ret_value = new ArrayList();
        for (int i = 0; i < columns; i++) {
            int count = 0;
            int essential_row = -1;
            for (int j = 0; j < rows; j++) {
                if (mat[j][i] == 'X') {
                    count++;
                    essential_row = j;
                }
            }

            if (count == 1) {
                if (ret_value.indexOf(essential_row) == -1) {
                    ret_value.add(essential_row);

                }
            }
        }
        System.out.println("Ret Values: " + ret_value);
        return ret_value;
    }

    private ArrayList<Integer> novo_pic_rows(char mat[][], int rows, int columns) {
        ArrayList<Integer> novo = new ArrayList();
        int count_rows = 0;
        int temp_rows;
        for (int i = 0; i < rows; i++) {
            temp_rows = 0;
            for (int j = 0; j < columns; j++) {
                if (mat[i][j] == 'X') {
                    ++temp_rows;
                }
            }
            if (temp_rows > count_rows) {
                count_rows = temp_rows;
            }
        }
        for (int i = 0; i < rows; i++) {
            temp_rows = 0;
            for (int j = 0; j < columns; j++) {
                if (mat[i][j] == 'X') {
                    ++temp_rows;
                }
            }
            if (temp_rows == count_rows) {
                novo.add(i);
            }
        }
        return novo;
    }

    private boolean scanMatrix(char mat[][], int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (mat[i][j] == 'X') {
                    return true;
                }
            }
        }
        return false;
    }

    public TruthTable getTable() {
        return this.mainTable;
    }
}
