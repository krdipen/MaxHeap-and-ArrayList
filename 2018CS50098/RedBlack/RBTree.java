package RedBlack;


public class RBTree<T extends Comparable, E> implements RBTreeInterface<T, E>  {
	RedBlackNode<T,E> root;
	public RBTree() {
		root=null;
	}
	
	public void restructure(RedBlackNode<T,E> child,RedBlackNode<T,E> parent,RedBlackNode<T,E> grand) {
		int status=grand.status;
		if((parent.status==0)&&(child.status==0)) {
			if(status==0) {
				if(grand.parent!=null) {
					grand.parent.left=parent;
				}
				else {
					root=parent;
				}
				parent.parent=grand.parent;
				parent.color=1;
				grand.left=parent.right;
				if(parent.right!=null) {
					parent.right.parent=grand;
					parent.right.status=0;
				}
				grand.color=0;
				parent.right=grand;
				grand.parent=parent;
				parent.status=status;
				grand.status=1;
			}
			else {
				if(grand.parent!=null) {
					grand.parent.right=parent;
				}
				else {
					root=parent;
				}
				parent.parent=grand.parent;
				parent.color=1;
				grand.left=parent.right;
				if(parent.right!=null) {
					parent.right.parent=grand;
					parent.right.status=0;
				}
				grand.color=0;
				parent.right=grand;
				grand.parent=parent;
				parent.status=status;
				grand.status=1;
			}
		}
		else if((parent.status==0)&&(child.status==1)) {
			if(status==0) {
				if(grand.parent!=null) {
					grand.parent.left=child;
				}
				else {
					root=child;
				}
				child.parent=grand.parent;
				child.color=1;
				parent.right=child.left;
				if(child.left!=null) {
					child.left.parent=parent;
					child.left.status=1;
				}
				grand.left=child.right;
				if(child.right!=null) {
					child.right.parent=grand;
					child.right.status=0;
				}
				grand.color=0;
				child.left=parent;
				parent.parent=child;
				child.right=grand;
				grand.parent=child;
				child.status=status;
				parent.status=0;
				grand.status=1;
			}
			else {
				if(grand.parent!=null) {
					grand.parent.right=child;
				}
				else {
					root=child;
				}
				child.parent=grand.parent;
				child.color=1;
				parent.right=child.left;
				if(child.left!=null) {
					child.left.parent=parent;
					child.left.status=1;
				}
				grand.left=child.right;
				if(child.right!=null) {
					child.right.parent=grand;
					child.right.status=0;
				}
				grand.color=0;
				child.left=parent;
				parent.parent=child;
				child.right=grand;
				grand.parent=child;
				child.status=status;
				parent.status=0;
				grand.status=1;
			}
		}
		else if((parent.status==1)&&(child.status==0)) {
			if(status==0) {
				if(grand.parent!=null) {
					grand.parent.left=child;
				}
				else {
					root=child;
				}
				child.parent=grand.parent;
				child.color=1;
				parent.left=child.right;
				if(child.right!=null) {
					child.right.parent=parent;
					child.right.status=0;
				}
				grand.right=child.left;
				if(child.left!=null) {
					child.left.parent=grand;
					child.left.status=1;
				}
				grand.color=0;
				child.right=parent;
				parent.parent=child;
				child.left=grand;
				grand.parent=child;
				child.status=status;
				parent.status=1;
				grand.status=0;
			}
			else {
				if(grand.parent!=null) {
					grand.parent.right=child;
				}
				else {
					root=child;
				}
				child.parent=grand.parent;
				child.color=1;
				parent.left=child.right;
				if(child.right!=null) {
					child.right.parent=parent;
					child.right.status=0;
				}
				grand.right=child.left;
				if(child.left!=null) {
					child.left.parent=grand;
					child.left.status=1;
				}
				grand.color=0;
				child.right=parent;
				parent.parent=child;
				child.left=grand;
				grand.parent=child;
				child.status=status;
				parent.status=1;
				grand.status=0;
			}
		}
		else {
			if(status==0) {
				if(grand.parent!=null) {
					grand.parent.left=parent;
				}
				else {
					root=parent;
				}
				parent.parent=grand.parent;
				parent.color=1;
				grand.right=parent.left;
				if(parent.left!=null) {
					parent.left.parent=grand;
					parent.left.status=1;
				}
				grand.color=0;
				parent.left=grand;
				grand.parent=parent;
				parent.status=status;
				grand.status=0;
			}
			else {
				if(grand.parent!=null) {
					grand.parent.right=parent;
				}
				else {
					root=parent;
				}
				parent.parent=grand.parent;
				parent.color=1;
				grand.right=parent.left;
				if(parent.left!=null) {
					parent.left.parent=grand;
					parent.left.status=1;
				}
				grand.color=0;
				parent.left=grand;
				grand.parent=parent;
				parent.status=status;
				grand.status=0;
			}
		}
	}

    @Override
    public void insert(T key, E value) {
    	if(root==null) {
    		root=new RedBlackNode<T,E>(key,value);
    		root.color=1;
    		return;
    	}
    	RedBlackNode<T,E> current=root;
    	while(true) {
    		T temp;
    		temp=current.key;
    		if(temp.compareTo(key)>0) {
    			if(current.left!=null) {
    				current=current.left;
    			}
    			else {
    				current.left=new RedBlackNode<T,E>(key,value);
    				current.left.parent=current;
    				current.left.status=0;
    				while(true) {
	    				if(current.color==1) {
	    					return;
	    				}
	    				int status=current.status;
	    				if(status==0) {
	    					if(current.parent.right==null) {
	    						if(current.left.color==0) {
	    							restructure(current.left,current,current.parent);
	    						}
	    						else {
	    							restructure(current.right,current,current.parent);
	    						}
	    						return;
	    					}
	    					if(current.parent.right.color==1) {
	    						if(current.left.color==0) {
	    							restructure(current.left,current,current.parent);
	    						}
	    						else {
	    							restructure(current.right,current,current.parent);
	    						}
	    						return;
	    					}
	    					if(current.parent.right.color==0) {
		    					current.color=1;
		    					current.parent.right.color=1;
		    					current.parent.color=0;
		    					if(current.parent.parent==null) {
									current.parent.color=1;
									return;
								}
		    					current=current.parent.parent;
	    					}
	    				}
	    				else {
	    					if(current.parent.left==null){
	    						if(current.left.color==0) {
	    							restructure(current.left,current,current.parent);
	    						}
	    						else {
	    							restructure(current.right,current,current.parent);
	    						}
	    						return;
	    					}
	    					if(current.parent.left.color==1) {
	    						if(current.left.color==0) {
	    							restructure(current.left,current,current.parent);
	    						}
	    						else {
	    							restructure(current.right,current,current.parent);
	    						}
	    						return;
	    					}
							if(current.parent.left.color==0) {
								current.color=1;
								current.parent.left.color=1;
								current.parent.color=0;
								if(current.parent.parent==null) {
									current.parent.color=1;
									return;
								}
								current=current.parent.parent;
							}
	    				}
    				}	
    			}
    		}
    		else if(temp.compareTo(key)<0) {
    			if(current.right!=null) {
    				current=current.right;
    			}
    			else {
    				current.right=new RedBlackNode<T,E>(key,value);
    				current.right.parent=current;
    				current.right.status=1;
    				while(true) {
	    				if(current.color==1) {
	    					return;
	    				}
	    				int status=current.status;
	    				if(status==0) {
	    					if(current.parent.right==null) {
	    						if(current.right.color==0) {
	    							restructure(current.right,current,current.parent);
	    						}
	    						else {
	    							restructure(current.left,current,current.parent);
	    						}
	    						return;
	    					}
	    					if(current.parent.right.color==1) {
	    						if(current.right.color==0) {
	    							restructure(current.right,current,current.parent);
	    						}
	    						else {
	    							restructure(current.left,current,current.parent);
	    						}
	    						return;
	    					}
	    					if(current.parent.right.color==0) {
		    					current.color=1;
								current.parent.right.color=1;
								current.parent.color=0;
								if(current.parent.parent==null) {
									current.parent.color=1;
									return;
								}
								current=current.parent.parent;
	    					}
	    				}
	    				else {
	    					if(current.parent.left==null){
	    						if(current.right.color==0) {
	    							restructure(current.right,current,current.parent);
	    						}
	    						else {
	    							restructure(current.left,current,current.parent);
	    						}
	    						return;
	    					}
	    					if(current.parent.left.color==1) {
	    						if(current.right.color==0) {
	    							restructure(current.right,current,current.parent);
	    						}
	    						else {
	    							restructure(current.left,current,current.parent);
	    						}
	    						return;
	    					}
	    					if(current.parent.left.color==0) {
		    					current.color=1;
								current.parent.left.color=1;
								current.parent.color=0;
								if(current.parent.parent==null) {
									current.parent.color=1;
									return;
								}
								current=current.parent.parent;
	    					}
	    				}
    				}	
    			}
    		}
    		else {
    			current.list.add(value);
    			return;
    		}
    	}
    }

    @Override
    public RedBlackNode<T, E> search(T key) {
    	if(root==null) {
    		return new RedBlackNode<T,E>();
    	}
    	RedBlackNode<T,E> current=root;
    	while(true) {
    		T temp;
    		temp=current.key;
    		if(temp.compareTo(key)>0) {
    			if(current.left!=null) {
    				current=current.left;
    			}
    			else {
    				return new RedBlackNode<T,E>();
    			}
    		}
    		else if(temp.compareTo(key)<0) {
    			if(current.right!=null) {
    				current=current.right;
    			}
    			else {
    				return new RedBlackNode<T,E>();
    			}
    		}
    		else {
    			return current;
    		}
    	}
    }
}