package adigozalpour.morteza.tree.example;

import adigozalpour.morteza.tree.Node;
import adigozalpour.morteza.tree.Tree;
import adigozalpour.morteza.tree.strategies.base.TreeManipulatorStrategy;

public class Main {

    public static void main( String[] args ) {
        new TreeSimulator( new Tree<>( Node.of( 0 ), TreeManipulatorStrategy.Type.READ_UNCOMMITTED ) ).simulate();
        new TreeSimulator( new Tree<>( Node.of( 0 ), TreeManipulatorStrategy.Type.REENTRANT_LOCK ) ).simulate();
        new TreeSimulator( new Tree<>( Node.of( 0 ), TreeManipulatorStrategy.Type.SYNCHRONIZED ) ).simulate();
    }
}
