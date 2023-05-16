package mips_generator;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class POMIPSGenerator implements MIPSGenerator {

        @Override
    public void generateMIPS(@NotNull String tac, @NotNull String fileName) {
        try (PrintWriter out = new PrintWriter(fileName)) {
            for (String tacLine : tac.split("\n")) {
                String[] components = tacLine.split(" ");
                String var = components[0];
                String c1 = components[2];

                if(components.length > 3) {
                    String op = components[3];
                    String c2 = components[4];

                    switch (op) {
                        case "+":
                            out.println("add " + var + ", " + c1 + ", " + c2);
                            break;
                        case "-":
                            out.println("sub " + var + ", " + c1 + ", " + c2);
                            break;
                        case "*":
                            out.println("mul " + var + ", " + c1 + ", " + c2);
                            break;
                        case "/":
                            out.println("div " + var + ", " + c1 + ", " + c2);
                            break;
                        case "and":
                            out.println("and " + var + ", " + c1 + ", " + c2);
                            break;

                        case "or":
                            out.println("or " + var + ", " + c1 + ", " + c2);
                            break;
                    }
                } else {
                    out.println("li " + var + ", " + c1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
