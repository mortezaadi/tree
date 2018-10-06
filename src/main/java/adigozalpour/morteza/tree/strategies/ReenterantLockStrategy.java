package adigozalpour.morteza.tree.strategies;

import adigozalpour.morteza.tree.Node;
import adigozalpour.morteza.tree.Tree;
import adigozalpour.morteza.tree.strategies.base.AbstractTreeManipulatorStrategy;
import adigozalpour.morteza.tree.strategies.base.TreeManipulatorStrategy;

import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class ReenterantLockStrategy<T> extends AbstractTreeManipulatorStrategy<T> {

    ReadWriteLock lock = new ReentrantReadWriteLock(  );

    public volatile int size = 1;

    public ReenterantLockStrategy( Tree<T> tree ) {
        super( tree );
    }

    @Override
    public TreeManipulatorStrategy.Type getType() {
        return TreeManipulatorStrategy.Type.REENTRANT_LOCK;
    }

    @Override
    public int count() {
        lock.readLock().lock();
        try {
            return size;
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    protected Optional<Node<T>> findInTree( Node<T> startNode, T data ) {
        lock.readLock().lock();
        try {
            return super.findInTree( startNode, data );
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void delete( Node<T> node ) {
        lock.writeLock().lock();
        size--;
        try {
            super.delete( node );
        }catch ( Exception e ) {
            size++;
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void insert( Node<T> parent, Node<T> child ) {
        lock.writeLock().lock();
        size++;
        try {
            super.insert( parent, child );
        }catch ( Exception e ) {
            size--;
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    protected void doActionOnParent( Optional<Node<T>> parent, Consumer<Node<T>> consumer ) {
        lock.writeLock().lock();
        try {
            super.doActionOnParent( parent, consumer );
        }finally {
            lock.writeLock().unlock();
        }

    }
}
