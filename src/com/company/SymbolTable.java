package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class SymbolTable {

    public static int getIndexOfLabel(String[][] st, String label, int lLine){
        for(int i=0;i<lLine;i++){
            for(int j=0;j<2;j++){
                if(st[i][j].equals(label)){
                    return i;
                }
            }
        }
        return 0;
    }


    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("input.txt");
        Scanner read = new Scanner(myFile);
        int line_count=0, LC=0, symbolLine=0, litLine = 0, lastIndex=0,dsAddress=0,poolLine=0;
        final int max = 100;
        String[][] symbolTable = new String[max][2];
        String[][] litTable = new String[max][2];
        String[] poolTable = new String[max];
        while(read.hasNextLine()){
            String line = read.nextLine();
            String[] tokens = line.split("\t");
            if(line_count==0){
                LC = Integer.parseInt(tokens[1]);
                LC--;
            }
            else {
                if(!tokens[0].equals("") && !tokens[1].equalsIgnoreCase("DC") && !tokens[1].equalsIgnoreCase("DS")){
                    symbolTable[symbolLine][0] = tokens[0];
                    symbolTable[symbolLine][1] = Integer.toString(LC);
                    symbolLine++;
                    LC++;
                }
                else if( tokens[1].equalsIgnoreCase("DC")){
                    symbolTable[symbolLine][0] = tokens[0];
                    symbolTable[symbolLine][1] = Integer.toString(LC-1);
                    symbolLine++;
                }

                else if(tokens[1].equalsIgnoreCase("DS")){
                    symbolTable[symbolLine][0] = tokens[0];
                    dsAddress = LC + Integer.parseInt(tokens[2]);
//                    System.out.println("dsAddress="+dsAddress);
                    symbolTable[symbolLine][1] = Integer.toString(LC-1);
                    symbolLine++;
                    LC = dsAddress-1;
                }

                if(tokens.length==3 && tokens[2].contains("=")){
                    int index = tokens[2].indexOf('=');
                    litTable[litLine][0] = tokens[2].substring(index);
                    litTable[litLine][1] = Integer.toString(LC-1);
                    litLine++;
                }
                //HANDLING ADVANCE ASSEMBLY DIRECTIVES
                if(tokens[1].equalsIgnoreCase("LTORG")){
                    LC--;
                    int lastAdd = LC;
                    for(int i=lastIndex;i<litLine;i++) {
                        litTable[i][1] = Integer.toString(lastAdd);
                        lastAdd++;
                    }
                    LC++;
                    lastIndex = litLine;
                }
                else if(tokens[1].equalsIgnoreCase("ORIGIN")){
                    int indexPlus = tokens[2].indexOf('+');
                    String label = tokens[2].substring(0,indexPlus);
                    String valueToAdd = tokens[2].substring(indexPlus+1);
                    int indexLabel = getIndexOfLabel(symbolTable,label,litLine);
                    int addressLabel = Integer.parseInt(symbolTable[indexLabel][1]);
                    LC = addressLabel + Integer.parseInt(valueToAdd);
                }
                else if(tokens[1].equalsIgnoreCase("EQU")){
                    String labelAddress = tokens[2];
                    System.out.println("label "+labelAddress);
                    String symbolAddressChange = tokens[0];
                    System.out.println("Symbol address change="+symbolAddressChange);
                    System.out.println("Lc="+LC);
                    int indexLabel = getIndexOfLabel(symbolTable,labelAddress,litLine);
                    int addressLabel = Integer.parseInt(symbolTable[indexLabel][1]);
                    int indexLabelChange = getIndexOfLabel(symbolTable,symbolAddressChange,litLine);
                    symbolTable[indexLabelChange][1] = Integer.toString(addressLabel);
                }

                if(tokens[1].equalsIgnoreCase("STOP") || tokens[1].equalsIgnoreCase("END")){
                    LC--;
                }
            }
            line_count++;
            LC++;
        }
//        int lastAdd = Integer.parseInt(symbolTable[symbolLine-1][1]);
        dsAddress = LC;
        int lastAdd = dsAddress;
        lastAdd--;
//        System.out.println("lastIndex= "+lastIndex);
        for(int i=lastIndex;i<litLine;i++) {
            litTable[i][1] = Integer.toString(lastAdd);
            lastAdd++;
        }
        System.out.println("\n\n	SYMBOL TABLE		");
        System.out.println("--------------------------");
        System.out.println("SYMBOL\tADDRESS");
        System.out.println("--------------------------");
        for(int i=0;i<symbolLine;i++)
            System.out.println(symbolTable[i][0]+"\t"+symbolTable[i][1]+"\t");
        System.out.println("--------------------------");
        System.out.println("\n\n	LITERAL TABLE		");
        System.out.println("--------------------------");
        System.out.println("LITERAL\tADDRESS");
        System.out.println("--------------------------");
        for(int i=0;i<litLine;i++)
            System.out.println(litTable[i][0]+"\t"+litTable[i][1]+"\t");
        System.out.println("--------------------------");

        for(int i=0;i<litLine;i++)
        {
            if(litTable[i][0]!=null && litTable[i+1][0]!=null )
            {
                if(i==0)
                {
                    poolTable[poolLine]=Integer.toString(i+1);
                    poolLine++;
                }
                else if(Integer.parseInt(litTable[i][1])<(Integer.parseInt(litTable[i+1][1]))-1)
                {
                    poolTable[poolLine]= String.valueOf(i+2);
                    poolLine++;
                }
            }
        }
        //print pool table
        System.out.println("\n\n   POOL TABLE		");
        System.out.println("-----------------");
        System.out.println("LITERAL NUMBER");
        System.out.println("-----------------");
        for(int i=0;i<poolLine;i++)
            System.out.println(poolTable[i]);
        System.out.println("------------------");


        read.close();
    }
}
