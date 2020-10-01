package Trie;

import Util.NodeInterface;

public class TrieNode<T> implements NodeInterface<T> {
	public T obj;
	public TrieNode<T>[] next;
	public TrieNode() {
		this.obj=null;
		next=new TrieNode[95];
	}
    @Override
    public T getValue() {
        return obj;
    }
}