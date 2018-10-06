package adigozalpour.morteza.tree.strategies;

import adigozalpour.morteza.tree.Node;
import adigozalpour.morteza.tree.Tree;
import adigozalpour.morteza.tree.strategies.base.AbstractTreeManipulatorStrategy;
import adigozalpour.morteza.tree.strategies.base.TreeManipulatorStrategy;

import java.util.concurrent.atomic.AtomicInteger;

public class ReadUnCommitedStrategy<T> extends AbstractTreeManipulatorStrategy<T> {

    private AtomicInteger size = new AtomicInteger( 1);

    public ReadUnCommitedStrategy( Tree tree ) {
        super( tree );
    }

    @Override
    public TreeManipulatorStrategy.Type getType() {
        return TreeManipulatorStrategy.Type.READ_UNCOMMITTED;
    }

    @Override
    public void delete( Node node ) {
        size.decrementAndGet();
        try {
            super.delete( node );
        }
        catch ( Exception e ) {
            size.incrementAndGet();
            throw e;
        }
    }

    @Override
    public void insert( Node parent, Node child ) {
        size.incrementAndGet();
        try {
            super.insert( parent, child );
        }
        catch ( Exception e ) {
            size.decrementAndGet();;
            throw e;
        }
    }

    @Override
    public int count() {
        return size.get();
    }
}
