package adigozalpour.morteza.tree.strategies.base;

import adigozalpour.morteza.tree.Node;
import adigozalpour.morteza.tree.Tree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractTreeManipulatorStrategyStrategyTest {

    public AbstractTreeManipulatorStrategy manipulator;
    public Tree<String> tree;

    @BeforeEach
    public void setup() {
        tree = new Tree<>( Node.of( "root" ) );
        tree.insert( Node.of( "root" ), Node.of( "child" ) );
        tree.insert( Node.of( "child" ), Node.of( "descendent" ) );
        manipulator = new AbstractTreeManipulatorStrategy( tree) {
            @Override
            public TreeManipulatorStrategy.Type getType() {
                return TreeManipulatorStrategy.Type.READ_UNCOMMITTED;
            }

            @Override
            public int count() {
                return 10;
            }
        };
    }

    @Test
    public void getType() {
        assertTrue( manipulator.getType() == TreeManipulatorStrategy.Type.READ_UNCOMMITTED );
    }

    @Test
    @DisplayName( "Can Query child node" )
    public void canFindNodeWhenDataIsInChild() {
        var node = manipulator.find( "child" );
        assertEquals( Node.of( "child" ), node.get() );
    }

    @Test
    @DisplayName( "Can Query descendent node" )
    public void canFindNodeWhenDataIsInDescendent() {
        var node = manipulator.find( Node.of( "descendent"));
        assertEquals( Node.of( "descendent" ), node.get() );
    }

    @Test
    @DisplayName( "Tree creation root with 3 depth descendent" )
    public void nodeCanBeCreatedWithDescendent() {
        assertTrue( tree.findNode( Node.of( "child" ) ).get().getParent().get().equals( Node.of( "root" ) ) );
        assertTrue( tree.findNode( Node.of( "descendent") ).get().getParent().get().equals( Node.of( "child" ) ) );
    }


    @Test
    public void calculateCountOfTree() {
        int i = manipulator.calculateCount();
        assertEquals(3,i);
    }

}
