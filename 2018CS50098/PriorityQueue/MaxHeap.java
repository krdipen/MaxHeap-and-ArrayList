package PriorityQueue;

import java.util.ArrayList;

public class MaxHeap<T extends Comparable> implements PriorityQueueInterface<T> {
	
	
	public ArrayList<Node<T>> list=new ArrayList<Node<T>>();
	public int time=0;

    @Override
    public void insert(T element) {
    	time++;
    	Node<T> temp=new Node(element,time);
    	list.add(temp);
    	int index=list.size()-1;
    	while(true) {
    		if(index==0) {
        		return;
        	}
    		if(index%2==0) {
    			if(list.get(index/2-1).compareTo(list.get(index))<0) {
    				Node<T> tempo=list.get(index/2-1);
    				list.set(index/2-1,list.get(index));
    				list.set(index,tempo);
    				index=index/2-1;
    			}
    			else if(list.get(index/2-1).compareTo(list.get(index))==0) {
    				if(list.get(index/2-1).time-list.get(index).time>0) {
    					Node<T> tempo=list.get(index/2-1);
        				list.set(index/2-1,list.get(index));
        				list.set(index,tempo);
        				index=index/2-1;
    				}
    				else {
    					return;
    				}
    			}
    			else {
    				return;
    			}
    		}
    		else {
				if(list.get((index-1)/2).compareTo(list.get(index))<0) {
					Node<T> tempo=list.get((index-1)/2);
					list.set((index-1)/2,list.get(index));
					list.set(index,tempo);
					index=(index-1)/2;
				}
				else if(list.get((index-1)/2).compareTo(list.get(index))==0) {
					if(list.get((index-1)/2).time-list.get(index).time>0) {
						Node<T> tempo=list.get((index-1)/2);
						list.set((index-1)/2,list.get(index));
						list.set(index,tempo);
						index=(index-1)/2;
					}
					else {
						return;
					}
				}
				else {
					return;
				}
			}
    	}
    }

    @Override
    public T extractMax() {
    	if(list.size()>1) {
    		Node<T> tempo=list.get(0);
    		int length=list.size()-1;
    		list.set(0,list.remove(length));
    		int index=0;
    		while(true) {
    			if(2*index+1>=length) {
    				return tempo.value;
    			}
    			if(2*index+2<length) {
	        		if(list.get(2*index+1).compareTo(list.get(2*index+2))>0) {
	        			if(list.get(2*index+1).compareTo(list.get(index))>0) {
	        				Node<T> temp=list.get(2*index+1);
	        				list.set(2*index+1,list.get(index));
	        				list.set(index,temp);
	        				index=2*index+1;
	        			}
	        			else if(list.get(2*index+1).compareTo(list.get(index))<0) {
	        				return tempo.value;
	        			}
	        			else {
	        				if(list.get(2*index+1).time-list.get(index).time<0) {
	        					Node<T> temp=list.get(2*index+1);
		        				list.set(2*index+1,list.get(index));
		        				list.set(index,temp);
		        				index=2*index+1;
	        				}
	        				else {
	        					return tempo.value;
	        				}
	        			}
	        		}
	        		else if(list.get(2*index+1).compareTo(list.get(2*index+2))<0){
	        			if(list.get(2*index+2).compareTo(list.get(index))>0) {
	        				Node<T> temp=list.get(2*index+2);
	        				list.set(2*index+2,list.get(index));
	        				list.set(index,temp);
	        				index=2*index+2;
	        			}
	        			else if(list.get(2*index+2).compareTo(list.get(index))<0) {
	        				return tempo.value;
	        			}
	        			else {
	        				if(list.get(2*index+2).time-list.get(index).time<0) {
	        					Node<T> temp=list.get(2*index+2);
		        				list.set(2*index+2,list.get(index));
		        				list.set(index,temp);
		        				index=2*index+2;
	        				}
	        				else {
	        					return tempo.value;
	        				}
	        			}
	        		}
	        		else {
	        			if(list.get(2*index+1).time-list.get(2*index+2).time<0) {
	        				if(list.get(2*index+1).compareTo(list.get(index))>0) {
		        				Node<T> temp=list.get(2*index+1);
		        				list.set(2*index+1,list.get(index));
		        				list.set(index,temp);
		        				index=2*index+1;
		        			}
		        			else if(list.get(2*index+1).compareTo(list.get(index))<0) {
		        				return tempo.value;
		        			}
		        			else {
		        				if(list.get(2*index+1).time-list.get(index).time<0) {
		        					Node<T> temp=list.get(2*index+1);
			        				list.set(2*index+1,list.get(index));
			        				list.set(index,temp);
			        				index=2*index+1;
		        				}
		        				else {
		        					return tempo.value;
		        				}
		        			}
	        			}
	        			else {
	        				if(list.get(2*index+2).compareTo(list.get(index))>0) {
		        				Node<T> temp=list.get(2*index+2);
		        				list.set(2*index+2,list.get(index));
		        				list.set(index,temp);
		        				index=2*index+2;
		        			}
		        			else if(list.get(2*index+2).compareTo(list.get(index))<0) {
		        				return tempo.value;
		        			}
		        			else {
		        				if(list.get(2*index+2).time-list.get(index).time<0) {
		        					Node<T> temp=list.get(2*index+2);
			        				list.set(2*index+2,list.get(index));
			        				list.set(index,temp);
			        				index=2*index+2;
		        				}
		        				else {
		        					return tempo.value;
		        				}
		        			}
	        			}
	        		}
    			}
    			else {
    				if(list.get(2*index+1).compareTo(list.get(index))>0) {
    					Node<T> temp=list.get(2*index+1);
        				list.set(2*index+1,list.get(index));
        				list.set(index,temp);
        				index=2*index+1;
        			}
        			else if(list.get(2*index+1).compareTo(list.get(index))<0){
        				return tempo.value;
        			}
        			else {
        				if(list.get(2*index+1).time-list.get(index).time<0) {
        					Node<T> temp=list.get(2*index+1);
            				list.set(2*index+1,list.get(index));
            				list.set(index,temp);
            				index=2*index+1;        				}
        				else {
        					return tempo.value;
        				}
        			}
    			}
        	}
    	}
    	else if(list.size()==1) {
    		return list.remove(0).value;
    	}
    	else {
    		return null;
    	}
    }

}