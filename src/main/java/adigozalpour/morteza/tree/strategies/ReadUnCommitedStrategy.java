package adigozalpour.morteza.tree.strategies;

import adigozalpour.morteza.tree.Node;
import adigozalpour.morteza.tree.Tree;
import adigozalpour.morteza.tree.strategies.base.AbstractTreeManipulatorStrategy;
import adigozalpour.morteza.tree.strategies.base.TreeManipulatorStrategy;

import java.util.concurrent.atomic.AtomicInteger;

public class ReadUnCommitedStrategy<T> extends AbstractTreeManipulatorStrategy<T> {

    private volatile int size =  1;

    public ReadUnCommitedStrategy( Tree tree ) {
        super( tree );
    }

    @Override
    public TreeManipulatorStrategy.Type getType() {
        return TreeManipulatorStrategy.Type.READ_UNCOMMITTED;
    }

    @Override
    public void delete( Node node ) {
        size--;
        try {
            super.delete( node );
        }
        catch ( Exception e ) {
            size++;
            throw e;
        }
    }

    @Override
    public void insert( Node parent, Node child ) {
        size++;
        try {
            super.insert( parent, child );
        }
        catch ( Exception e ) {
            size--;
            throw e;
        }
    }

    @Override
    public int count() {
        return size;
    }
}
