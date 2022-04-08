/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author
 */
import java.util.ArrayList;
/*
* The class Knapsack to calculate the set with maximum values
* from 2 given item arrays 1. with weights 2. with values
* and with given weight's Capacity 
*/
public class Knapsack {
    // The main table to calculate the maximum value of 
    // possible sets
    int[][] maxValueTable;
    // Additional array that contains indices of chosen items
    // used in getSet() method
    ArrayList<Integer> chosen;
    
    // to initiate Knapsack and its calculation
    // 3 @param are required
    // 1. item array with weights
    // 2. item array with values
    // 3. Capacity of the Knapsack
    public Knapsack(int[] weights, int[] values, int Capacity) {
        // The main table with rows=Capacity+1 and columns=items+1
        maxValueTable = new int[Capacity+1][weights.length+1];
        chosen = new ArrayList();
        
        // the 1st row and 1st column cells equal 0
        for (int i=0; i < maxValueTable.length; i++) {
            maxValueTable[i][0] = 0;
        }
        for (int i=0; i < maxValueTable[0].length; i++) maxValueTable[0][i] = 0;
        //invoking the method to build the main table
        this.buildMaxValueTable(weights, values);
        
    }
    
    // The method to build the main table
    public void buildMaxValueTable(int[] weights, int[] values) {
        for (int j=1; j<maxValueTable[j].length; j++) {
            for (int w=1; w < maxValueTable.length; w++) {
                if (weights[j-1] > w){
                    maxValueTable[w][j] = maxValueTable[w][j-1];
                } else {
                    if ((maxValueTable[w][j-1] < maxValueTable[w - weights[j-1]][j-1] + values[j-1]))
                        maxValueTable[w][j] = maxValueTable[w - weights[j-1]][j-1] + values[j-1];
                    else maxValueTable[w][j] = maxValueTable[w][j-1];                    
                }                
            }
        }
    }
    
    // The auxiliary method used in buildmaxValueTable method
    public int max(int a, int b) {
        if (a > b) return a;
        return b;
    }
    
    // Method to get the set of items chosen for the Knapsack
    // It wasn't necessary. It's just curiuos to unwind the
    // maximum result of the main table
    public void getSet(int[] weights, int[] values) {
        int w = maxValueTable.length - 1;
        int j = maxValueTable[w].length - 1;
        
        while (w != 0 || j != 0) {
            if (maxValueTable[w][j] == maxValueTable[w][j-1]) j = j - 1;
            else if (maxValueTable[w][j] == maxValueTable[w - weights[j-1]][j-1] 
                    + values[j-1]) {
                chosen.add(j-1);
                w = w - weights[j-1];
                j = j - 1;
            } else j = j - 1;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int a, i, k, n, b, Capacity, tempWeight, tempValue, bestValue, bestWeight;
        int remainder, nDigits;
        int Weights[] = {1, 4, 6, 2, 5, 10, 8, 3, 9, 1, 4, 2, 5, 8, 9, 1};
        int Values[] = { 10, 5, 30, 8, 12, 30, 50, 10, 2, 10, 40, 80, 100, 25, 10, 5 };
        int A[];
        
        A = new int[16];

        Capacity = 20; // Max pounds that can be carried
        n = 16;  // number of items in the store
        b=0;

        tempWeight = 0;
        tempValue = 0;
        bestWeight = 0;
        bestValue = 0;
/*
     for ( i=0; i<65536; i++) {
                remainder = i;

                // Initialize array to all 0's
                for ( a=0; a<16; a++) {
                    A[a] = 0;
                }

                // Populate binary representation of counter i
                //nDigits = Math.ceil(Math.log(i+0.0));
                nDigits = 16;

                for ( a=0; a<nDigits; a++ ) {
                    A[a] = remainder % 2;
                    remainder = remainder / 2;
                }

                // fill knapsack based upon binary representation
                for (k = 0; k < n; k++) {

                    if ( A[k] == 1) {
                        if (tempWeight + Weights[k] <= Capacity) {
                            tempWeight = tempWeight + Weights[k];
                            tempValue = tempValue + Values[k];
                        }
                    }
                }

                // if this knapsack is better than the last one, save it
                if (tempValue > bestValue) {
                    bestValue = tempValue;
                    bestWeight = tempWeight;
                    b++;
                }
                tempWeight = 0;
                tempValue = 0;
        }
        System.out.printf("Weight: %d Value %d\n", bestWeight, bestValue);
        System.out.printf("Number of valid knapsack's: %d\n", b);*/
        
        System.out.print("The tables with results.\n");
        Knapsack testKnapsack = new Knapsack(Weights, Values, Capacity);
        for (int w=0; w < testKnapsack.maxValueTable.length; w++) {
            for (int j=0; j < testKnapsack.maxValueTable[j].length; j++) {
                System.out.print(testKnapsack.maxValueTable[w][j] + ";");
            }
            System.out.println();
        }
        
        testKnapsack.getSet(Weights, Values);
        System.out.println("The algorihm has chosen:");
        bestWeight = 0;
        bestValue = 0;
        for(int index: testKnapsack.chosen) {
            //System.out.printf("Index=%d\n", index);
            System.out.printf("Item with index=%d, weight=%d and value=%d\n", index, Weights[index], Values[index]);
            bestWeight = bestWeight + Weights[index];
            bestValue = bestValue + Values[index];            
        }
        System.out.printf("With total weight=%d and total value=%d\n", bestWeight, bestValue);
    }    
}
