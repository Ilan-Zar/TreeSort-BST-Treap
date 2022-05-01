import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * BST is a class that creates a Binary Search Tree
 * @author Ilan Zar
 */
public class BST <E> implements Tree<E>{
	/**
	 * Two attributes exist in the BST class
	 * One generic Node holding the root
	 * One integer holding the current size
	 * All attributes are set to default values for simplicity.
	 */
	private Node<E> root = null;
	private int size = 0;
	
	private static class Node<E>{
		private E element;
		private Node<E> left;
		private Node<E> right;
		private Node<E> parent;
		/**
		 * A four parameter constructor that creates a new node with the given values.
		 * @param element holds the value for this.element
		 * @param left holds the value for this.left
		 * @param right holds the value for this.right
		 * @param parent holds the value for this.parent
		 */
		Node(E element, Node<E> left, Node<E> right, Node<E> parent){
			this.element = element;
			this.left = left;
			this.right = right;
			this.parent = parent;
		}
	}
	/**
	 * A no argument constructor that creates a new BST of generic type which is specified at runtime.
	 */
	public BST(){}
	/**
	 * Adds the specified element to this tree (duplicates are not allowed)
	 * @param e element to add
	 * @return true if the element was added (the tree was modified) 
	 */
	@Override
	public boolean add(E e) {
		boolean res = true;
		Node<E> temp = new Node<E>(e, null, null, null);
		if(size == 0)
			root = temp;
		else {
			Node<E> pos = root;
			Node<E> trail = null;
			while(pos != null && pos.element != e) {
				trail = pos;
				if(lessThan(e, pos.element))
					pos = pos.left;
				else
					pos = pos.right;
			}
			if(pos != null) 
				res = false;
			else if(lessThan(e, trail.element)) {
				trail.left = temp;
				temp.parent = trail;
			}
			else {
				trail.right = temp;
				temp.parent = trail;
			}	
		}
		if(res)
			size++;
		return res;
	}
	/**
	 * A helper method that compares values of two elements.
	 * @param x The first element
	 * @param y The second element
	 * @return True only if x is less than y
	 * Basic structure was referenced from the Max Heap implementation found here:
	 * https://algs4.cs.princeton.edu/24pq/MaxPQ.java.html
	 */
	private boolean lessThan(E x, E y) {
		return ((Comparable)x).compareTo(y) < 0;
	}
	/**
	 * Adds all of the elements in the specified collection to this tree.
	 * (duplicates are not allowed)
	 * @param c Collection containing the elements
	 * @return true if the tree was changed as a result of the call
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean res = false;
		for(E e: c) {
			if(add(e))
				res = true;
		}
		return res;
	}
	/**
	 * A helper method that handles deletion of a node with no children
	 * @param pos Holds the node of the specified node.
	 */
	private void removeNoChild(Node<E> pos){
		if(lessThan(pos.parent.element, pos.element))
			pos.parent.right = null;
		else
			pos.parent.left = null;
		pos = null;
	}
	/**
	 * A helper method that handles deletion of a node with one child
	 * @param pos Holds the node of the specified node.
	 */
	private void removeOneChild(Node<E> pos) {
		if(lessThan(pos.parent.element, pos.element)) {
			if(pos.left != null) {
				pos.parent.right = pos.left;
				pos.left.parent = pos.parent;
			}
			else {
				pos.parent.right = pos.right;
				pos.right.parent = pos.parent;
			}
		}
		else {
			if(pos.left != null) {
				pos.parent.left = pos.left;
				pos.left.parent = pos.parent;
			}
			else {
				pos.parent.left = pos.right;
				pos.right.parent = pos.parent;
			}
		}
		pos = null;
	}
	/**
	 * A helper method that handles deletion of a node with two children
	 * @param pos Holds the node of the specified node.
	 */
	private void removeTwoChild(Node<E> pos) {
		Node<E> temp = pos.right;
		while(temp.left != null) {
			temp = temp.left;
		}
		if(temp.right != null)
			removeOneChild(temp);
		else
			removeNoChild(temp);
		pos.element = temp.element;
	}
	/**
	 * A helper method that handles deletion of a node that happens to be the root
	 * @param pos Holds the node of the specified node.
	 */
	private void removeRoot(Node<E> pos) {
		if(pos.left != null && pos.right != null)
			removeTwoChild(pos);
		else if(pos.left != null) {
			root = root.left;
			pos = null;
		}
		else if(pos.right != null) {
			root = root.right;
			pos = null;
		}
		else
			root = null;
	}
	/**
	 * Removes object from tree 
	 * @param o
	 * @return Returns true if this tree contains the specified element.
	 */
	@Override
	public boolean remove(Object o) {
		boolean res = false;
		E tempcast = (E) o;
		Node<E> pos = containsNode(tempcast);
		if(pos != null) {
			res = true;
			if(pos.element == root.element) {
				removeRoot(root);
			}
			else {
				if(pos.left != null && pos.right != null) 
					removeTwoChild(pos);
				else if(pos.left != null || pos.right != null)
					removeOneChild(pos);
				else 
					removeNoChild(pos);
			}
			size--;
		}
		return res;
	}
	/**
	 * Searches for object in tree
	 * @param o
	 * @return Returns true if this tree contains the specified element.
	 */
	@Override
	public boolean contains(Object o) {
		boolean res = false;
		E tempcast = (E) o;
		Node<E> pos = containsNode(tempcast);
		if(pos != null)
			res = true;
		return res;
	}
	/**
	 * A helper method that searches for element in tree
	 * @param e Holds the element of the specified node.
	 * @return returns node if found
	 */
	private Node<E> containsNode(E e){
		Node<E> pos = root;
		while(pos != null && pos.element != e) {
			if(lessThan(e, pos.element))
				pos = pos.left;
			else
				pos = pos.right;
		}	
		return pos;
	}
	/** Returns an iterator over the elements of this tree in-order
	 * @return the iterator described above
	 */
	@Override
	public Iterator<E> iterator() {
		Iterator<E> it = new It();
		return it;
	}
	
	private class It implements Iterator<E>{
		private Node<E> cur;
		private Node<E> next;
		/**
		 * A no argument constructor that creates a new instance of It and sets the next Node to the first Node in the Tree.
		 */
		public It() {
			cur = root;
			next = null;
			if(cur != null) {
				while(cur != null) {
					next = cur;
					cur = cur.left;
				}
			}
		}
		/**
		 * @return Returns true if next element exists
		 */
		@Override
		public boolean hasNext() {
			return next != null;
		}
		/**
		 * @return Returns element of next node if there is a next node
		 * @exception throws new NoSuchElementException if it attempts to be called when there is no next node
		 * @see NoSuchElementException
		 */
		@Override
		public E next() {
			cur = next;
			if(hasNext()) {
				if(next.right != null) {
					next = next.right;
					while(next.left != null) {
						next = next.left;
					}
				}
				else if(next != root){
					next = next.parent;
					while(lessThan(next.element, cur.element) && next != root)
							next = next.parent;
					if(lessThan(next.element, cur.element))
						next = null;
				}
				else 
					next = null;
			}
			else
				throw new NoSuchElementException();
			return cur.element;
		}
		
	}
	/** Returns the number of elements in the tree. 
	 * @return number of elements
	 */
	@Override
	public int size() {
		return size;
	}

}
