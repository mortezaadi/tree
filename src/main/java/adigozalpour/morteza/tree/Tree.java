package adigozalpour.morteza.tree;

import adigozalpour.morteza.tree.strategies.ReadUnCommitedStrategy;
import adigozalpour.morteza.tree.strategies.ReenterantLockStrategy;
import adigozalpour.morteza.tree.strategies.SynchronizedStrategy;
import adigozalpour.morteza.tree.strategies.base.TreeManipulatorStrategy;

import java.util.Optional;
import java.util.Stack;

/**
 * Tree structure.
 *
 * @param <T> type of data
 */
public class Tree<T>  {

    private final Node<T> root;

    protected final TreeManipulatorStrategy<T> manipulator;

    /**
     * Default constructor. The manipulation strategy will be {@link ReadUnCommitedStrategy}
     * to specify different strategy use {@link #Tree(Node, TreeManipulatorStrategy.Type)}
     * @param root {@link Node} root of the tree
     *
     * @see #Tree(Node, TreeManipulatorStrategy)
     * @see ReadUnCommitedStrategy
     */
    public Tree( Node<T> root ) {
        this( root, TreeManipulatorStrategy.Type.READ_UNCOMMITTED );
    }

    /**
     *
     * @param root {@link Node} root of the tree
     * @param manipulatorType {@link TreeManipulatorStrategy.Type} type of the manipulation strategy.
     *
     * @see TreeManipulatorStrategy
     */
    public Tree( Node<T> root, TreeManipulatorStrategy.Type manipulatorType) {
        this.root = root;
        manipulator = getManipulator( manipulatorType );
    }

    protected Tree( Node<T> root, TreeManipulatorStrategy<T> manipulator ) {
        this.root = root;
        this.manipulator = manipulator;
    }

    /**
     *
     * @param node {@link Node} node to be find.
     * @return Optional founded node.
     */
    public Optional<Node<T>> findNode( Node<T> node) {
        return manipulator.find(node);
    }

    public Node<T> getRoot() {
        return root;
    }

    /**
     * Expand tree.
     * @param parent {@link Node} parent
     * @param node {@link Node} child to be added to parend.
     *
     * @see TreeManipulatorStrategy#insert(Node, Node)
     *
     * @throws IllegalArgumentException if parent and node are same or if parent not exist in the tree.
     */
    public void insert( Node<T> parent, Node<T> node) {
        manipulator.insert( parent,node );
    }

    /**
     * delete node from tree
     * @param node {@link Node} to be deleted from tree.
     *
     * @throws IllegalArgumentException if node is not exist or node refer to root of the tree
     */
    public void delete(Node<T> node) {
       manipulator.delete( node );
    }


    public int getSize() {
       return manipulator.count();
    }

    public TreeManipulatorStrategy.Type getStrategy() {
        return manipulator.getType();
    }

    public int calculateCount() {
        Stack<Node<T>> s = new Stack<Node<T>>();
        s.push(root);
        int cnt = 0;
        while (!s.empty()) {
            Node<T> t = s.pop();
            cnt++;
            s.addAll( t.getChildren() );
        }
        return cnt;
    }

    private TreeManipulatorStrategy<T> getManipulator( TreeManipulatorStrategy.Type type ) {
        TreeManipulatorStrategy<T> m = null;
        switch ( type ) {
        case REENTRANT_LOCK:
            m = new ReenterantLockStrategy<>( this );
            break;
        case SYNCHRONIZED:
            m = new SynchronizedStrategy<>( this );
            break;
        default:
            m = new ReadUnCommitedStrategy<>( this );
        }
        return m;
    }
}
