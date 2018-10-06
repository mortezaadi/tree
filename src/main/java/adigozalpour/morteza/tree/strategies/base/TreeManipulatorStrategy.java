package adigozalpour.morteza.tree.strategies.base;

import adigozalpour.morteza.tree.Node;

import java.util.Optional;

/**
 * Skeleton of tree manipulator strategy.
 *
 * @param <T>
 */
public interface TreeManipulatorStrategy<T> {

    public enum Type {
        REENTRANT_LOCK,
        READ_UNCOMMITTED,
        SYNCHRONIZED
    }

    TreeManipulatorStrategy.Type getType();

    void delete( Node<T> node );

    void insert(Node<T> parent, Node<T> child);

    int count();

    Optional<Node<T>> find( T data);

    Optional<Node<T>> find( Node<T> node);

}
