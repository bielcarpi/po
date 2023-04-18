package entities;

import java.util.ArrayList;
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
        return sb.toString();
    }

    public void cleanTree(){
        cleanTree(root);
        cleanTree(root);
    }

    private void cleanTree(ParseTreeNode node){
        if(node.getSelf() instanceof TokenType) return; //We found a token, so we don't need to clean this node

        ArrayList<ParseTreeNode> childrenDone = new ArrayList<>();
        while(!childrenDone.containsAll(node.getChildren())){ //While we have not cleaned all children
            for(ParseTreeNode child: node.getChildren()){
                if(!childrenDone.contains(child)){
                    cleanTree(child);
                    childrenDone.add(child);
                    break;
                }
            }
        }


        //If we have childs that are VOID or EOL tokens, we can remove them
        node.getChildren().removeIf(child -> (child.getSelf() == TokenType.EOL) || (child.getSelf() == TokenType.VOID));

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
     * Print a tree structure in a pretty ASCII fromat.
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
