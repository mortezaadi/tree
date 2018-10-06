package adigozalpour.morteza.tree;

import adigozalpour.morteza.tree.strategies.base.TreeManipulatorStrategy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TreeTest {

    private static TreeManipulatorStrategy<Integer> mockedManipulator;
    private static Tree<Integer> tree;

    @BeforeAll
    public static void setup() {
        mockedManipulator = Mockito.mock( TreeManipulatorStrategy.class );
        tree = new Tree<>( Node.of( 1), mockedManipulator);
    }

    @Test
    @DisplayName( "Tree creation with root node and custom manipulator" )
    public void treeWithNodeAndManipulatorCanBeCreated() {
        assertNotNull( tree );
    }

    @Test
    @DisplayName( "Tree creation single root" )
    public void treeCanBeCreatedWithDefaultManipulator() {
        Tree<Integer> tree2 = new Tree<>( Node.of( 1 ) );
        assertNotNull( tree2 );
        assertEquals( tree2.manipulator.getType(), TreeManipulatorStrategy.Type.READ_UNCOMMITTED );
    }

    @Test
    @DisplayName( "Tree creation with predefined manipulator" )
    public void treeCanBeCreatedWithProvidedManipulator() {
        Tree<Integer> tree2 = new Tree<>( Node.of( 1 ), TreeManipulatorStrategy.Type.SYNCHRONIZED );
        assertNotNull( tree2 );
        assertEquals( tree2.manipulator.getType(), TreeManipulatorStrategy.Type.SYNCHRONIZED );
    }

    @Test
    public void insertCallShouldBeHandledByTreeManipulatorStrategy() {
        tree.insert( Node.of( 1 ), Node.of( 2 ));
        Mockito.verify( mockedManipulator ,Mockito.times( 1 )).insert( Node.of( 1 ), Node.of( 2 ) );
    }


    @Test
    public void findNodeCallShouldBeHandledByTreeManipulatorStrategy() {
        tree.findNode( Node.of( 1 ));
        Mockito.verify( mockedManipulator ,Mockito.times( 1 )).find( Node.of( 1 ) );
    }

    @Test
    public void deleteCallShouldBeHandledByTreeManipulatorStrategy() {
        tree.delete( Node.of( 1 ));
        Mockito.verify( mockedManipulator ,Mockito.times( 1 )).delete( Node.of( 1 ) );
    }


    @Test
    public void calculateCountOfTreeWithOneElement() {
        int i = tree.calculateCount();
        assertEquals(1,i);
    }

    @Test
    public void calculateCountOfTree() {
        Tree<Integer> tree2 = new Tree<>( Node.of( 1 ));
        tree2.insert( Node.of(1),Node.of( 2 ) );
        tree2.insert( Node.of(1),Node.of( 3 ) );
        tree2.insert( Node.of(1),Node.of( 4 ) );
        tree2.insert( Node.of(4),Node.of( 5 ) );
        tree2.insert( Node.of(5),Node.of( 6 ) );
        int i = tree2.calculateCount();
        assertEquals(6,i);
    }
}
