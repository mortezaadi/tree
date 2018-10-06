package adigozalpour.morteza.tree;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NodeTest {


    @Test
    @DisplayName( "Node creation" )
    void nodeCanBeCreated() {
        var node = new Node<>();
        assertNotNull( node );
    }

    @Test
    @DisplayName( "Node creation with builder" )
    void nodeCanBeCreatedWithBuilder() {
        var node = Node.of( 2 );
        assertNotNull( node );
        assertEquals( Integer.valueOf( 2 ),node.getData());
    }


    @Test
    @DisplayName( "Node can not be parent of itself" )
    void nodeCanBeParentForItself() {
        var node = Node.of( 2 );
        IllegalArgumentException illegalArgumentException = assertThrows( IllegalArgumentException.class, () -> node.addChild( node ) );
        assertEquals( "A node can not be parent of itself", illegalArgumentException.getMessage());
    }

    @Test
    @DisplayName( "Node can have children" )
    void nodeCanContainChildren() {
        var node = Node.of( 10 );
        node.addChild( Node.of( 20 ) );
        node.addChild( Node.of( 30 ) );
        node.addChild( Node.of( 40 ) );
        assertEquals( 3, node.getChildren().size());
        assertTrue( node.getChildren().stream().allMatch( (n) -> n.getParent().get().equals(node)) );
    }

    @Test
    @DisplayName( "Node children should not modified outside of node" )
    void nodeShouldReturnImmutableListOfChilds() {
        var node = createSampleNode();
        assertThrows( UnsupportedOperationException.class, () -> node.getChildren().add( Node.of( 50 ) ));
    }

    @Test
    @DisplayName( "Node can allow a child to be removed from children list" )
    void aChildCanBeRemovedFromNodeChildren() {
        var node = Node.of( 10 );
        var node20 =  Node.of( 20 );
        node.setChildren( Arrays.asList( node20, Node.of( 30 ), Node.of( 40 ) ) );
        node.removeChild( node20 );
        assertEquals(2, node.getChildren().size() );
        assertTrue( node20.getParent().isEmpty());
    }

    @Test
    @DisplayName( "Node throws exception when removing a node which is not presented" )
    void returnFalseIfTheChildIsNotPresent() {
        var node = createSampleNode();
        assertThrows( IllegalArgumentException.class ,()-> node.removeChild( Node.of( 80 )));
        assertEquals(3, node.getChildren().size() );
    }


    @Test
    @DisplayName( "Node without parent and without child should be considered as a root" )
    void nodeWithoutParentIsRoot() {
       var node = Node.of( 1 );
       assertTrue(node.isRoot());
    }

    @Test
    @DisplayName( "Node without a child should be considered as a leaf" )
    void nodesWithoutChildAreLeaf() {
        var node1 = Node.of( 1 );
        var node2 = Node.of( 2 );
        node1.addChild( node2 );
        assertTrue( node2.isLeaf() );
        assertFalse( node1.isLeaf() );
    }

    @Test
    @DisplayName( "Node without parent and with child should be considered as a root" )
    void nodeWithoutParentAndWithChildIsRoot() {
        var node = createSampleNode();
        assertTrue(node.isRoot());
    }

    private Node<Integer> createSampleNode() {
        var node = Node.of( 10 );
        node.setChildren( Arrays.asList( Node.of( 20 ), Node.of( 30 ), Node.of( 40 ) ) );
        return node;
    }
}
