package mips_generator;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class POMIPSGenerator implements MIPSGenerator {

        @Override
    public void generateMIPS(@NotNull String tac, @NotNull String fileName) {
        String[] tmp = {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9"};
        int tmpIndex = 0;

        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(".section .text\n");
            out.println(".global __start\n");
            out.println("__start:");

            for (String tacLine : tac.split("\n")) {
                String[] components = tacLine.split(" ");
                String var = components[0];
                String c1 = components[2];

                if(components.length > 3) {
                    String op = components[3];
                    String c2 = components[4];

                    switch (op) {
                        case "+":
                            out.println("\tadd " + var + ", " + c1 + ", " + c2);
                            break;
                        case "-":
                            out.println("\tsub " + var + ", " + c1 + ", " + c2);
                            break;
                        case "*":
                            // Check if c1 or c2 is a constant, or if both are constants or if none is a constant
                            if(c1.matches("-?\\d+") && c2.matches("-?\\d+")) {
                                out.println("\tli " + tmp[tmpIndex] + ", " + c1);
                                tmpIndex++;

                                if(tmpIndex < tmp.length -1) {
                                    tmpIndex++;
                                } else {
                                    tmpIndex = 1;
                                }
                                out.println("\tli " + tmp[tmpIndex] + ", " + c2);
                                out.println("\tmul " + tmp[tmpIndex -1] + ", " + tmp[tmpIndex]);
                            }

                            break;
                        case "/":
                            out.println("\tdiv " + var + ", " + c1 + ", " + c2);
                            break;
                        case "and":
                            out.println("\tand " + var + ", " + c1 + ", " + c2);
                            break;

                        case "or":
                            out.println("\tor " + var + ", " + c1 + ", " + c2);
                            break;
                    }
                } else {
                    if(c1.matches("-?\\d+")) {
                            out.println("\tli " + tmp[tmpIndex] + ", " + c1);
                    } else {
                            out.println("\tmove " + tmp[tmpIndex] + ", " + c1);
                    }
                }

                if(tmpIndex < tmp.length -1) {
                    tmpIndex++;
                } else {
                    tmpIndex = 0;
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
