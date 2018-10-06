package adigozalpour.morteza.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Node of tree.
 *
 * @param <T> type of data
 */
public class Node<T> {

    private T data;

    private Node<T> parent;

    private List<Node<T>> children = new ArrayList<>();

    public static <T> Node<T> of( T data) {
        Node<T> node = new Node<>();
        node.setData(data);
        return node;
    }

    public T getData() {
        return data;
    }

    public void setData( T data ) {
        this.data = data;
    }

    public Optional<Node<T>> getParent() {
        return Optional.ofNullable(parent);
    }

    public List<Node<T>> getChildren() {
        return Collections.unmodifiableList( children);
    }

    public void setChildren( List<Node<T>> children ) {
        this.children.addAll(children);
    }

    public void addChild(Node<T> child) {
        child.setParent( this );
        children.add( child );
    }

    public void removeChild(Node<T> child) {
        boolean removed = children.remove( child );
        if(removed) {
            child.setParent( null ); // to be eligible for garbage collection
        }else {
            throw new IllegalArgumentException( "No such a child exist" );
        }
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Node<?> node = (Node<?>) o;
        return Objects.equals( data, node.data );
    }

    @Override
    public int hashCode() {
        return Objects.hash( data );
    }

    @Override
    public String toString() {
        return "Node{" + "data=" + data + ", Number of children=" + children + '}';
    }

    private void setParent( Node<T> parent ) {
        if(this.equals( parent )) {
            throw new IllegalArgumentException( "A node can not be parent of itself" );
        }
        this.parent = parent;
    }


}
