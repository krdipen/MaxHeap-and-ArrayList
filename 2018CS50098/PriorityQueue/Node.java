package PriorityQueue;

public class Node<T extends Comparable> implements Comparable<Node<T>> {
	public T value;
	public Integer time;
	public Node(T value,int time) {
		this.value=value;
		this.time=time;
	}
	@Override
    public int compareTo(Node<T> node) {
        return this.value.compareTo(node.value); 
    }
}
