package mips_generator;

import entities.TAC;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class POMIPSGenerator implements MIPSGenerator {

        @Override
        public void generateMIPS(@NotNull TAC tac, @NotNull String fileName) {
                String[] tmp = {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9"};
                int tmpIndex = 0;

                try (PrintWriter out = new PrintWriter(fileName)) {
                        out.println(".section .text\n");
                        out.println(".global __start\n");
                        out.println("__start:");

                        for (String tacLine : tac.toString().split("\n")) {
                                String[] components = tacLine.split(" ");
                                String var = components[0];
                                String c1 = components[2];

                                if(components.length > 3) {
                                        String op = components[3];
                                        String c2 = components[4];

                                        switch (op) {
                                                case "+":
                                                        // Same as multiplication
                                                        if(c1.matches("-?\\d+") && c2.matches("-?\\d+")) {
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c1);
                                                                tmpIndex++;

                                                                if(tmpIndex < tmp.length -1) {
                                                                        tmpIndex++;
                                                                } else {
                                                                        tmpIndex = 1;
                                                                }
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c2);
                                                                out.println("\tadd " + tmp[tmpIndex -1] + ", " + tmp[tmpIndex]);
                                                        } else if (c1.matches("-?\\d+") && !c2.matches("-?\\d+")) {
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c1);
                                                                tmpIndex++;

                                                                if(tmpIndex < tmp.length -1) {
                                                                        tmpIndex++;
                                                                } else {
                                                                        tmpIndex = 1;
                                                                }
                                                                out.println("\tadd " + tmp[tmpIndex -1] + ", " + c2);
                                                        } else if (!c1.matches("-?\\d+") && c2.matches("-?\\d+")) {
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c2);
                                                                tmpIndex++;

                                                                if(tmpIndex < tmp.length -1) {
                                                                        tmpIndex++;
                                                                } else {
                                                                        tmpIndex = 1;
                                                                }
                                                                out.println("\tadd " + tmp[tmpIndex -1] + ", " + c1);
                                                        } else {
                                                                out.println("\tadd " + tmp[tmpIndex] + ", " + c1 + ", " + c2);
                                                        }
                                                        break;
                                                case "-":
                                                        if(c1.matches("-?\\d+") && c2.matches("-?\\d+")) {
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c1);
                                                                tmpIndex++;

                                                                if(tmpIndex < tmp.length -1) {
                                                                        tmpIndex++;
                                                                } else {
                                                                        tmpIndex = 1;
                                                                }
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c2);
                                                                out.println("\tsub " + tmp[tmpIndex -1] + ", " + tmp[tmpIndex]);
                                                        }
                                                        break;
                                                case "*":
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
                                                        } else if (c1.matches("-?\\d+") && !c2.matches("-?\\d+")) {
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c1);
                                                                tmpIndex++;

                                                                if(tmpIndex < tmp.length -1) {
                                                                        tmpIndex++;
                                                                } else {
                                                                        tmpIndex = 1;
                                                                }
                                                                out.println("\tmul " + tmp[tmpIndex -1] + ", " + c2);
                                                        } else if (!c1.matches("-?\\d+") && c2.matches("-?\\d+")) {
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c2);
                                                                tmpIndex++;

                                                                if(tmpIndex < tmp.length -1) {
                                                                        tmpIndex++;
                                                                } else {
                                                                        tmpIndex = 1;
                                                                }
                                                                out.println("\tmul " + tmp[tmpIndex -1] + ", " + c1);
                                                        } else {
                                                                out.println("\tmul " + tmp[tmpIndex] + ", " + c1 + ", " + c2);
                                                        }

                                                        break;
                                                case "/":
                                                        // The same as multiplication
                                                        if(c1.matches("-?\\d+") && c2.matches("-?\\d+")) {
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c1);
                                                                tmpIndex++;

                                                                if(tmpIndex < tmp.length -1) {
                                                                        tmpIndex++;
                                                                } else {
                                                                        tmpIndex = 1;
                                                                }
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c2);
                                                                out.println("\tdiv " + tmp[tmpIndex -1] + ", " + tmp[tmpIndex]);
                                                        }
                                                        break;
                                                case "and":
                                                        if(c1.matches("-?\\d+") && c2.matches("-?\\d+")) {
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c1);
                                                                tmpIndex++;

                                                                if(tmpIndex < tmp.length -1) {
                                                                        tmpIndex++;
                                                                } else {
                                                                        tmpIndex = 1;
                                                                }
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c2);
                                                                out.println("\tand " + tmp[tmpIndex -1] + ", " + tmp[tmpIndex]);
                                                        }
                                                        break;

                                                case "or":
                                                        if(c1.matches("-?\\d+") && c2.matches("-?\\d+")) {
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c1);
                                                                tmpIndex++;

                                                                if(tmpIndex < tmp.length -1) {
                                                                        tmpIndex++;
                                                                } else {
                                                                        tmpIndex = 1;
                                                                }
                                                                out.println("\tli " + tmp[tmpIndex] + ", " + c2);
                                                                out.println("\tor " + tmp[tmpIndex -1] + ", " + tmp[tmpIndex]);
                                                        }
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