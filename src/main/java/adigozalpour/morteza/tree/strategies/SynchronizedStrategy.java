package adigozalpour.morteza.tree.strategies;

import adigozalpour.morteza.tree.Node;
import adigozalpour.morteza.tree.Tree;
import adigozalpour.morteza.tree.strategies.base.AbstractTreeManipulatorStrategy;
import adigozalpour.morteza.tree.strategies.base.TreeManipulatorStrategy;

import java.util.Optional;
import java.util.function.Consumer;

public class SynchronizedStrategy<T> extends AbstractTreeManipulatorStrategy<T> {

    public volatile int size = 1;

    public SynchronizedStrategy( Tree tree ) {
        super( tree );
    }

    @Override
    public synchronized TreeManipulatorStrategy.Type getType() {
        return TreeManipulatorStrategy.Type.SYNCHRONIZED;
    }

    @Override
    public synchronized void delete( Node node ) {
        super.delete( node );
        size--;
    }

    @Override
    public synchronized void insert( Node parent, Node child ) {
        super.insert( parent, child );
        size++;
    }

    @Override
    protected synchronized Optional<Node<T>> findInTree( Node<T> startNode, T data ) {
        return super.findInTree( startNode, data );
    }

    @Override
    public synchronized int count() {
        return size;
    }

    @Override
    protected synchronized void doActionOnParent( Optional<Node<T>> parent, Consumer<Node<T>> consumer ) {
        super.doActionOnParent( parent, consumer );
    }
}
