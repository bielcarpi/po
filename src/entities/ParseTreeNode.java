package entities;

import java.util.ArrayList;

/**
 * Represents a node of the Parse Tree
 *
 * @see ParseTree
 */
public class ParseTreeNode {

    private ParseTreeNode parent;
    private Object self;
    private Token token;
    private ArrayList<ParseTreeNode> children;

    /**
     * Creates a Parse Tree Node with a given parent, self and children
     * @param parent The parent of the node
     * @param self The node itself String or TokenType
     * @param children The children of the node
     */
    public ParseTreeNode(ParseTreeNode parent, Object self, ArrayList<ParseTreeNode> children) {
        this.parent = parent;
        this.self = self;
        this.children = children;
        this.token = null;
    }

    /**
     * Returns the parent of the node
     * @return The parent of the node
     */
    public ParseTreeNode getParent() {
        return parent;
    }

    /**
     * Returns the node itself
     * @return The node itself
     */
    public Object getSelf() {
        return self;
    }

    /**
     * Returns the children of the node
     * @return The children of the node
     */
    public ArrayList<ParseTreeNode> getChildren() {
        return children;
    }

    /**
     * Adds a child to the node
     * @param parseTreeNode The child to add
     */
    public void addChild(ParseTreeNode parseTreeNode) {
        children.add(parseTreeNode);
        parseTreeNode.parent = this;
    }

    /**
     * Replaces a child of the node
     * @param childToReplace The child to replace
     * @param children The children to replace with
     */
    public void replaceChild(ParseTreeNode childToReplace, ArrayList<ParseTreeNode> children) {
        if(!this.children.contains(childToReplace))
            return;

        for(ParseTreeNode child: children)
            child.parent = this;

        this.children.addAll(this.children.indexOf(childToReplace), children);
        this.children.remove(childToReplace);
    }

    /**
     * Sets the self of the node
     * @param self The self of the node
     */
    public void setSelf(Object self, Token token) {
        this.self = self;
        this.token = token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return self.toString();
    }


    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    /**
     * Sets the parent of the node
     * @param parent The parent of the node
     */
    public void setParent(ParseTreeNode parent) {
        this.parent = parent;
    }
}
