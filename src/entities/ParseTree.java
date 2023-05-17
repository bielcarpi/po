package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

/**
 * Represents a Parse Tree
 *
 * @see ParseTreeNode
 */
public class ParseTree {

    private ParseTreeNode root;
    private static String scope = "global"; //Aux variable to keep track of the current scope

    /**
     * Creates a Parse Tree with a given root
     * @param root The root of the Parse Tree
     */
    public ParseTree(ParseTreeNode root) {
        this.root = root;
    }

    /**
     * Returns the root of the Parse Tree
     * @return  The root of the Parse Tree
     */
    public ParseTreeNode getRoot() {
        return root;
    }

    /**
     * Returns a String representation of the Parse Tree
     * @return A String representation of the Parse Tree
     */
    @Override
    public String toString() {
        Function<ParseTreeNode, List<ParseTreeNode>> getChildrenFunc = ParseTreeNode::getChildren;
        StringBuilder sb = new StringBuilder();
        printTreeRec("", root, getChildrenFunc, true, sb);
        sb.deleteCharAt(sb.length() - 1); //Remove last newline
        return sb.toString();
    }

    /**
     * Cleans the Parse Tree
     * @param pt The Parse Tree to clean
     */
    public static void cleanTree(ParseTree pt){
        cleanUselessNodes(pt.root, pt);
        cleanProductions(pt.root);
    }

    /**
     * Removes nodes that are not needed
     * @param node The node to clean
     */
    private static void cleanUselessNodes(ParseTreeNode node, ParseTree pt){
        //Bottom-up Recursive

        if(node.getSelf() instanceof TokenType) return; //We found a token, so we don't need to clean this node

        ArrayList<ParseTreeNode> childrenDone = new ArrayList<>();
        while(!childrenDone.containsAll(node.getChildren())){ //While we have not cleaned all children
            for(ParseTreeNode child: node.getChildren()){
                if(!childrenDone.contains(child)){
                    cleanUselessNodes(child, pt);
                    childrenDone.add(child);
                    break;
                }
            }
        }

        //If we have childs that are VOID or EOL tokens, we can remove them
        List<TokenType> tokensToRemove = Arrays.asList(TokenType.EOL, TokenType.VOID, TokenType.OPEN_BRACE,
                TokenType.CLOSE_BRACE, TokenType.OPEN_PAREN, TokenType.CLOSE_PAREN, TokenType.COMMA, TokenType.ARROW);
        node.getChildren().removeIf(child -> tokensToRemove.contains(child.getSelf()));

        //If we have only one child, we can remove this node and move the child up
        if(node.getChildren().size() == 1 && !node.getSelf().equals("<llistaArguments>")){
            if(node.getParent() == null){ //If we are the root, we can just replace the root
                pt.root = node.getChildren().get(0);
                pt.root.setParent(null);
                return;
            }

            node.getParent().replaceChild(node, node.getChildren());
        }

        //If we have no childs, we can remove this node
        if(node.getChildren().isEmpty() && node.getParent() != null)
            node.getParent().getChildren().remove(node);
    }



    /**
     * Cleans productions with the following rules:
     *  - Removes nodes that have a & in their name, and substitutes them with its first child (a terminal)
     *  - Removes nodes that are non-terminals and contain the name of their parent + Prima in their name
     *  - Removes assignacioVariable
     *
     * The nodes with a & in their name (ex. <sentenciaWhile&>) can be substituted by its first child (in this
     *  particular case, the terminal "while")
     * @param node The node to clean
     */
    private static void cleanProductions(ParseTreeNode node){
        //Top-Down Recursive

        if(node.getSelf() instanceof TokenType) return; //We found a token, so we don't need to clean this node

        //If the node has a & in its name, we can substitute it with its first child
        if(node.getSelf() instanceof String && node.getSelf().toString().contains("&")){
            node.setSelf(node.getChildren().get(0).getSelf(), node.getChildren().get(0).getToken());
            node.getChildren().remove(0);

            //If now that we removed we have only one child, and it is non-terminal, we can remove that node and move its childs up
            if(node.getChildren().size() == 1 && node.getChildren().get(0).getSelf() instanceof String &&
                    !node.getChildren().get(0).toString().contains("sentencia"))
                node.replaceChild(node.getChildren().get(0), node.getChildren().get(0).getChildren());
        }

        //If the node is a non-terminal and contains the name of his parent + Prima in his own name, we can remove it (example, <expressioMulPrima> contains <expressioMul>)
        if(node.getSelf() instanceof String && node.getParent() != null && node.getParent().getSelf() instanceof String
                && node.getSelf().toString().contains(node.getParent().getSelf().toString().replace("<", "").replace(">", "") + "Prima")){
            node.getParent().replaceChild(node, node.getChildren());
        }

        //If the node is <assignacioVariable>, we can remove it and move its childs up
        if(node.getSelf() instanceof String && node.getSelf().toString().equals("<assignacioVariable>"))
            node.getParent().replaceChild(node, node.getChildren());

        //If the node is <llistaArguments>, we can remove it and move its childs to the previous child
        if(node.getSelf().equals("<llistaArguments>")){
            node.setSelf(node.getParent().getChildren().get(0).getSelf(), node.getParent().getChildren().get(0).getToken());
            node.getParent().getChildren().remove(node.getParent().getChildren().indexOf(node) - 1);

            //If the parent node has a single child now, we can remove it and move its childs up
            if(node.getParent().getChildren().size() == 1)
                node.getParent().getParent().replaceChild(node.getParent(), node.getParent().getChildren());
        }


        ArrayList<ParseTreeNode> childrenDone = new ArrayList<>();
        while(!childrenDone.containsAll(node.getChildren())){ //While we have not cleaned all children
            for(ParseTreeNode child: node.getChildren()){
                if(!childrenDone.contains(child)){
                    cleanProductions(child);
                    childrenDone.add(child);
                    break;
                }
            }
        }
    }


    /**
     * Runs the TAC Optimization on the provided Parse Tree.
     * The modified Parse Tree will be called TACOTree (Three Address Code Optimized Tree), as
     *  it will have a maximum of 3 branches per node on operations.
     * @param pt The Parse Tree to optimize
     */
    public static void runTACOptimization(ParseTree pt){
        innerTACOptimization(pt.root);
    }

    private static void innerTACOptimization(ParseTreeNode node){
        //Top-Down Recursive

        //If we have a VAR or TYPE node, we can remove it entirely
        if(node.getSelf().equals(TokenType.VAR) || node.getSelf().equals(TokenType.TYPE)){
            // Add variable to the symbol table, without type assigned yet
            SymbolTableVariableEntry entry = new SymbolTableVariableEntry(
                    node.getChildren().get(0).getToken().getData(),
                    scope,
                    TokenType.UNKNOWN,
                    1);

            SymbolTable.getInstance().insert(entry);

            //If VAR has only one child, it means it's a declaration, so we can remove it. If not, it's an assignation
            if(node.getChildren().size() == 1 || node.getSelf().equals(TokenType.TYPE)){
                node.getParent().getChildren().remove(node);

                //If the parent node has a single child now, we can remove it and move its childs up
                if(node.getParent().getChildren().size() == 1 && node.getParent().getParent() != null)
                    node.getParent().getParent().replaceChild(node.getParent(), node.getParent().getChildren());
                return;
            }

            node.setSelf("assignacio", null);
        }

        //If we have a FUNC token, we can substitute it for its first child (ID: the name of the FUNC) and remove the second child (the parameters)
        if(node.getSelf().equals(TokenType.FUNC)){
            // Insert function entry to the symbol table with unknown type for now.
            SymbolTable.getInstance().insert(
                    new SymbolTableFunctionEntry(
                            node.getChildren().get(0).getToken().getData(),
                            TokenType.UNKNOWN,
                            // If function has params, there will be 3 childs (ID, llistaParams, sentencies)
                            // If number of childs equals 2, there are no params to the function
                            // Careful with case where function is void
                            node.getChildren().size() == 2 && !node.getChildren().get(1).getSelf().equals("<llistaParametres>")?
                                    0 : node.getChildren().get(1).getChildren().size()
                    ));

            // Change the current scope to the new function. The first child of the FUNC will always be the ID
            scope = node.getChildren().get(0).getToken().getData();

            // If the function has params, we need to add them to the symbol table
            if (node.getChildren().size() == 3) { // If number of childs equals 3, there are params to the function
                for (ParseTreeNode param : node.getChildren().get(1).getChildren()) {
                    SymbolTable.getInstance().insert(
                            new SymbolTableVariableEntry(
                                    param.getToken().getData(),
                                    scope,
                                    TokenType.UNKNOWN,
                                    1));
                }
            }

            // Replace the FUNC node with the ID (which is the first child) because we don't need the FUNC keyword
            node.setSelf(node.getChildren().get(0).getSelf(), node.getChildren().get(0).getToken());
            // The node has been switched, and we need to remove the node itself from the children (which is the first node)
            node.getChildren().remove(0);
            if(node.getChildren().get(0).getSelf().equals("<llistaParametres>")) //If we have parameters, remove them
                node.getChildren().remove(0);

            // Only one child is left, so substitute it with its childs.
            if (node.getChildren().size() != 0) // Make sure first the function is not void
                node.replaceChild(node.getChildren().get(0), node.getChildren().get(0).getChildren());

            return;
        }


        //If we are llistaComposta and the parent is llistaComposta too, move ourselves to the level of our parent
        if(node.getSelf().equals("<llistaComposta>") && node.getParent().getSelf().equals("<llistaComposta>")){
            node.getParent().getChildren().remove(node);
            node.getParent().getParent().addChild(node);
            node.setParent(node.getParent().getParent());
        }


        //If we have some expression, replace the name with exp
        if(node.getSelf() instanceof String && node.getSelf().toString().contains("exp")){
            node.setSelf("exp", null);
        }

        //If an exp has more than 3 children, transform it into exp + 2 last children (the new exp = all children - the latest 2)
        if(node.getSelf().equals("exp") && node.getChildren().size() > 3){
            ArrayList<ParseTreeNode> newNodeChildren = new ArrayList<>();
            while(node.getChildren().size() != 2){
                newNodeChildren.add(node.getChildren().get(0));
                node.getChildren().remove(0);
            }

            node.getChildren().add(0, new ParseTreeNode(node, "exp", newNodeChildren));
        }

        //If the node is <sentencia>, change name to assignacio (<sentencia> can now only be assignacio: = <exp> | ++ | --)
        if(node.getSelf().equals("<sentencia>") || node.getSelf().toString().contains("assignacioFor"))
            node.setSelf("assignacio", null);

        if(node.getChildren() == null || node.getChildren().isEmpty())
            return;

        ArrayList<ParseTreeNode> childrenDone = new ArrayList<>();
        while(!childrenDone.containsAll(node.getChildren())){ //While we have not cleaned all children
            for(ParseTreeNode child: node.getChildren()){
                if(!childrenDone.contains(child)){
                    innerTACOptimization(child);
                    childrenDone.add(child);
                    break;
                }
            }
        }


        //If we're <llistaComposta>, delete ourselves and put our children in our level
        if(node.getSelf().equals("<llistaComposta>") && node.getParent() != null && (node.getParent().getSelf() == TokenType.MAIN || node.getParent().getSelf() == TokenType.ID || node.getParent().getSelf() == TokenType.OPT))
            node.getParent().replaceChild(node, node.getChildren());
        else if(node.getSelf().equals("<llistaComposta>"))
            node.setSelf("llista", null);

        //If we're <llistaDeclaracions>, <llistaElsif> or <llistaOpt> delete ourselves and put our children in our level
        if((node.getSelf().equals("<llistaDeclaracions>") || node.getSelf().equals("<llistaElsif>") ||
            node.getSelf().equals("<llistaOpt>")) && node.getParent() != null)
            node.getParent().replaceChild(node, node.getChildren());

        //If we're FOR or WHILE and our last child is not a llista, substitute it with a llista and put it inside the llista
        if((node.getSelf().equals(TokenType.FOR) || node.getSelf().equals(TokenType.WHILE) || node.getSelf().equals(TokenType.ELSIF))
                && !node.getChildren().get(node.getChildren().size() - 1).getSelf().equals("llista")){
            ParseTreeNode llista = new ParseTreeNode(node, "llista", new ArrayList<>());
            ParseTreeNode aux = node.getChildren().get(node.getChildren().size() - 1);
            node.getChildren().remove(aux);
            node.getChildren().add(llista);
            llista.addChild(aux);
        }

        //If we're IF or OPT and our second child is not a llista, substitute it with a llista and put it inside the llista
        if((node.getSelf().equals(TokenType.IF) || node.getSelf().equals(TokenType.OPT)) && !node.getChildren().get(1).getSelf().equals("llista")){
            ParseTreeNode llista = new ParseTreeNode(node, "llista", new ArrayList<>());
            ParseTreeNode aux = node.getChildren().get(1);
            node.getChildren().remove(aux);
            node.getChildren().add(1, llista);
            llista.addChild(aux);
        }

        //Check if scope changed
        if(node.getSelf().equals(TokenType.ID) && node.getToken().getData().equals(scope))
            scope = "global"; //Return to global scope
    }



    /**
     * Print a tree structure in a pretty ASCII format.
     * Extracted from <a href="https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram-in-java">StackOverflow</a>
     * @param prefix Current prefix. Use "" in initial call!
     * @param node The current node. Pass the root node of your tree in initial call.
     * @param getChildrenFunc A {@link Function} that returns the children of a given node.
     * @param isTail Is node the last of its siblings. Use true in initial call. (This is needed for pretty printing.)
     * @param <T> The type of your nodes. Anything that has a toString can be used.
     */
    private <T> void printTreeRec(String prefix, T node, Function<T, List<T>> getChildrenFunc, boolean isTail, StringBuilder sb) {
        String nodeName = node.toString();
        String nodeConnection = isTail ? "└── " : "├── ";
        sb.append(prefix).append(nodeConnection).append(nodeName).append("\n");
        List<T> children = getChildrenFunc.apply(node);
        if(children == null || children.isEmpty())
            return;
        for (int i = 0; i < children.size(); i++) {
            String newPrefix = prefix + (isTail ? "    " : "│   ");
            printTreeRec(newPrefix, children.get(i), getChildrenFunc, i == children.size() - 1, sb);
        }
    }
}
