import java.util.Iterator;
public class TreeSort{
	/** Sorts an array using TreeSort with a balanced BST implementation 
	 * @param a an array to sort
	 */
	public static <E extends Comparable <? super E>> void sort( E[] a){
		Tree <E> tree = new Treap<>();
		sort(tree, a);
	}
	
	/**
	 * Sorts an array using TreeSort with a specified BST
	 * @param tree tree to use
	 * @param a an array to sort
	 */
	public static <E extends Comparable <? super E>> void sort(Tree <E> tree, E[] a){
		for(int i = 0; i < a.length; i++) {
			tree.add(a[i]);
		}
		int k = 0;
		Iterator<E> it = tree.iterator();
		while(it.hasNext()) {
			a[k++] = it.next();
		}
	}
}
