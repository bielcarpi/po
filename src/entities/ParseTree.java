package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Represents a Parse Tree
 *
 * @see ParseTreeNode
 */
public class ParseTree {

    private ParseTreeNode root;

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
     */
    public void cleanTree(){
        cleanUselessNodes(root);
        cleanProductionsWithTerminals(root);
        cleanProductionsWithAsterisc(root);
        cleanProductionsWithAmpersand(root);
    }

    /**
     * Removes nodes that are not needed
     * @param node The node to clean
     */
    private void cleanUselessNodes(ParseTreeNode node){
        //Bottom-up Recursive

        if(node.getSelf() instanceof TokenType) return; //We found a token, so we don't need to clean this node

        ArrayList<ParseTreeNode> childrenDone = new ArrayList<>();
        while(!childrenDone.containsAll(node.getChildren())){ //While we have not cleaned all children
            for(ParseTreeNode child: node.getChildren()){
                if(!childrenDone.contains(child)){
                    cleanUselessNodes(child);
                    childrenDone.add(child);
                    break;
                }
            }
        }

        //If we have childs that are VOID or EOL tokens, we can remove them
        List<TokenType> tokensToRemove = Arrays.asList(TokenType.EOL, TokenType.VOID, TokenType.OPEN_BRACE,
                TokenType.CLOSE_BRACE, TokenType.OPEN_PAREN, TokenType.CLOSE_PAREN, TokenType.COMMA);
        node.getChildren().removeIf(child -> tokensToRemove.contains(child.getSelf()));

        //If we have only one child, we can remove this node and move the child up
        if(node.getChildren().size() == 1){
            if(node.getParent() == null){ //If we are the root, we can just replace the root
                root = node.getChildren().get(0);
                return;
            }

            node.getParent().getChildren().set(node.getParent().getChildren().indexOf(node), node.getChildren().get(0));
        }

        //If we have no childs, we can remove this node
        if(node.getChildren().isEmpty())
            node.getParent().getChildren().remove(node);
    }

    /**
     * Removes nodes that only have terminals as childs
     * @param node The node to clean
     */
    private void cleanProductionsWithTerminals(ParseTreeNode node){
        //Top-Down Recursive

        if(node.getSelf() instanceof TokenType) return; //We found a token, so we don't need to clean this node

        //If one of our childs only has terminals as its childs, we can remove this node and move the childs up
        List<ParseTreeNode> childsAux = new ArrayList<>(node.getChildren());
        for(ParseTreeNode child: childsAux){
            if(child.getSelf() instanceof TokenType) continue; //We found a token, so we don't need to clean this node
            if(child.getChildren().stream().allMatch(c -> c.getSelf() instanceof TokenType)){
                node.getChildren().addAll(node.getChildren().indexOf(child), child.getChildren());
                node.getChildren().remove(child);
            }

            //If the node still exists, we can clean it
            if(node.getChildren().contains(child))
                cleanProductionsWithTerminals(child);
        }

    }


    /**
     * Removes nodes that have a * in their name
     * These nodes are not needed to be in the tree (marked with *), so we can remove them
     * @param node The node to clean
     */
    private void cleanProductionsWithAsterisc(ParseTreeNode node){
        //Top-Down Recursive

        if(node.getSelf() instanceof TokenType) return; //We found a token, so we don't need to clean this node

        //If a child is non-terminal but has a * in its name, we can remove this node and move the childs up
        List<ParseTreeNode> childsAux = new ArrayList<>(node.getChildren());
        for(ParseTreeNode child: childsAux){
            if(child.getSelf() instanceof TokenType) continue; //We found a token, so we don't need to clean this node
            if(child.getSelf() instanceof String && child.getSelf().toString().contains("*")){
                node.getChildren().addAll(node.getChildren().indexOf(child), child.getChildren());
                node.getChildren().remove(child);
            }

            //If the node still exists, we can clean it
            if(node.getChildren().contains(child))
                cleanProductionsWithAsterisc(child);
        }

        //If we added new childs in the process, do the same for them
        for(ParseTreeNode child: node.getChildren())
            if(!childsAux.contains(child)) cleanProductionsWithAsterisc(child);
    }

    /**
     * Removes nodes that have a & in their name, and substitutes them with its first child (a terminal)
     * The nodes with a & in their name (ex. <sentenciaWhile&>) can be substituted by its first child (in this
     *  particular case, the terminal "while")
     * @param node The node to clean
     */
    private void cleanProductionsWithAmpersand(ParseTreeNode node){
        //Top-Down Recursive

        if(node.getSelf() instanceof TokenType) return; //We found a token, so we don't need to clean this node

        if(node.getSelf() instanceof String && node.getSelf().toString().contains("&")){
            node.setSelf(node.getChildren().get(0).getSelf());
            node.getChildren().remove(0);

            //If now that we removed we have only one child, and it is non-terminal, we can remove that node and move its childs up
            if(node.getChildren().size() == 1 && node.getChildren().get(0).getSelf() instanceof String){
                node.getChildren().addAll(node.getChildren().get(0).getChildren());
                node.getChildren().remove(0);
            }
        }

        for(ParseTreeNode child: node.getChildren())
            cleanProductionsWithAmpersand(child);
    }




    /**
     * Print a tree structure in a pretty ASCII format.
     * Extracted from <a href="https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram-in-java">...</a>
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
