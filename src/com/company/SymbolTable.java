package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SymbolTable {
    public static void main(String[] args) throws FileNotFoundException {
        File myFile = new File("input3.txt");
        Scanner read = new Scanner(myFile);
        int line_count=0, LC=0, symbolLine=0, litLine = 0, lastIndex=0,dsAddress=0;
        final int max = 100;
        String[][] symbolTable = new String[max][2];
        String[][] litTable = new String[max][2];
        while(read.hasNextLine()){
            String line = read.nextLine();
            String[] tokens = line.split("\t");
            if(line_count==0){
                LC = Integer.parseInt(tokens[1]);
            }
            else {
                if(!tokens[0].equals("") && !tokens[1].equalsIgnoreCase("DC") && !tokens[1].equalsIgnoreCase("DS")){
                    symbolTable[symbolLine][0] = tokens[0];
                    symbolTable[symbolLine][1] = Integer.toString(LC-1);
                    symbolLine++;
                }
                else if( tokens[1].equalsIgnoreCase("DC")){
                    symbolTable[symbolLine][0] = tokens[0];
                    symbolTable[symbolLine][1] = Integer.toString(LC-1);
                    symbolLine++;
                }

                else if(tokens[1].equalsIgnoreCase("DS")){
                    symbolTable[symbolLine][0] = tokens[0];
                    dsAddress = LC + Integer.parseInt(tokens[2]);
                    System.out.println("dsAddress="+dsAddress);
                    symbolTable[symbolLine][1] = Integer.toString(LC-1);
                    symbolLine++;
                    LC = dsAddress;
                }

                if(tokens.length==3 && tokens[2].contains("=")){
                    int index = tokens[2].indexOf('=');
                    litTable[litLine][0] = tokens[2].substring(index);
                    litTable[litLine][1] = Integer.toString(LC-1);
                    litLine++;
                }
                if(tokens[1].equalsIgnoreCase("LTORG")){
                    LC--;
                    int lastAdd = LC;
                    for(int i=0;i<litLine;i++) {
                        litTable[i][1] = Integer.toString(lastAdd);
                        lastAdd++;
                    }
                    LC++;
                    lastIndex = litLine;
                }
                if(tokens[1].equalsIgnoreCase("STOP") || tokens[1].equalsIgnoreCase("END")){
                    LC--;
                }
            }
            line_count++;
            LC++;
        }
//        int lastAdd = Integer.parseInt(symbolTable[symbolLine-1][1]);
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
    }
}
