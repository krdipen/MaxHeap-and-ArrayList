package RedBlack;

import java.util.ArrayList;

import Util.RBNodeInterface;

import java.util.List;

public class RedBlackNode<T extends Comparable, E> implements RBNodeInterface<E> {
	public RedBlackNode<T,E> parent;
	public RedBlackNode<T,E> left;
	public RedBlackNode<T,E> right;
	public int status;
	public int color;
	public T key;
	public List<E> list=new ArrayList<E>();
	
	public RedBlackNode() {
		this.parent=null;;
		this.left=null;
		this.right=null;
		this.status=0;
		this.color=0;
		this.key=null;
		this.list=null;
	}
	
	public RedBlackNode(T key,E obj) {
		this.parent=null;;
		this.left=null;
		this.right=null;
		this.status=0;
		this.color=0;
		this.key=key;
		this.list.add(obj);
	}
	
    @Override
    public E getValue() {
        return list.get(0);
    }

    @Override
    public List<E> getValues() {
        return list;
    }
}
