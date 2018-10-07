package adigozalpour.morteza.tree.strategies.base;

import adigozalpour.morteza.tree.Node;
import adigozalpour.morteza.tree.Tree;

import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;

/**
 *
 * @param <T>
 */
public abstract class AbstractTreeManipulatorStrategy<T> implements TreeManipulatorStrategy<T> {

    protected final Tree<T> tree;

    public AbstractTreeManipulatorStrategy( Tree<T> tree ) {
        this.tree = tree;
    }

    public abstract TreeManipulatorStrategy.Type getType();

    @Override
    public Optional<Node<T>> find( T data ) {
        return findInTree(tree.getRoot(),data);
    }

    @Override
    public Optional<Node<T>> find( Node<T> node ) {
        return findInTree(tree.getRoot(),node.getData());
    }

    protected Optional<Node<T>> findInTree( Node<T> startNode, T data) {
        if(startNode.getData().equals( data )) {
            return  Optional.of(startNode);
        }else {
            for ( Node<T> child: startNode.getChildren()) {
                if(child.getData().equals( data )) {
                    return Optional.of(child);
                }else {
                    Optional<Node<T>> findInChild = findInTree( child,data);
                    if(findInChild.isPresent()) {
                        return findInChild;
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete( Node<T> node ) {
        Optional<Node<T>> actualNode = find(node);
        if(node.isRoot()) {
            throw new IllegalArgumentException( "You can not remove root from tree" );
        }
        Optional<Node<T>> parent = find(node).get().getParent();
        doActionOnParent(parent, (p) -> p.removeChild( node ));
    }

    @Override
    public void insert( Node<T> parent, Node<T> child ) {
        if(parent.equals(child)) {
            throw new IllegalArgumentException( "You can not add a node to itself" );
        }
        doActionOnParent( find( parent ), (p) -> p.addChild( child ));
    }

    protected void doActionOnParent( Optional<Node<T>> parent, Consumer<Node<T>> consumer ) {
        parent.ifPresentOrElse( consumer, () -> {throw new IllegalArgumentException( "Parent is not found" );});
    }

    @Override
    public int calculateCount() {
        Stack<Node<T>> s = new Stack<Node<T>>();
        s.push(tree.getRoot());
        int cnt = 0;
        while (!s.empty()) {
            Node<T> t = s.pop();
            cnt++;
            s.addAll( t.getChildren() );
        }
        return cnt;
    }
}
