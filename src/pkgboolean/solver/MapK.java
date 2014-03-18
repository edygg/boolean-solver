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

    public Function simplifyaFunctionMinTerms() {
        /*Step 1*/
        ArrayList<MinMaxTerm> prime_implicant_chart = new ArrayList();
        ArrayList<MinMaxTerm> ftable = this.mainTable.mintables();
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
        
        
        
        /*STEP 2.3 PIC*/
        return null;
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
