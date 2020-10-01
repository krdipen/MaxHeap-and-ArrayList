package Trie;

public class Trie<T> implements TrieInterface<T> {
	TrieNode<T>[] root=new TrieNode[95];

    @Override
    public boolean delete(String word) {
        int length=word.length();
        TrieNode<T>[] toDeleteArray;
        toDeleteArray=root;
        int toDeleteIndex=word.charAt(0)-' ';
        TrieNode<T>[] current;
        current=root;
        for(int i=0;i<length;i++) {
        	if(current[word.charAt(i)-' ']==null) {
        		return false;
        	}
        	else {
        		for(int j=0;j<95;j++) {
            		if((current[j]!=null)&&(j!=word.charAt(i)-' ')) {
            			toDeleteArray=current;
            			toDeleteIndex=word.charAt(i)-' ';
            			break;
            		}
            	}
        		if(i==length-1) {
        			if(current[word.charAt(i)-' '].obj==null) {
        				return false;
        			}
        			else {
            			current[word.charAt(i)-' '].obj=null;
            		}
        		}
        		current=current[word.charAt(i)-' '].next;
        	}
        }
        for(int j=0;j<95;j++) {
    		if(current[j]!=null) {
    	        return true;
    		}
    	}
        if(toDeleteArray!=null) {
        	toDeleteArray[toDeleteIndex]=null;
        }
        return true;
    }

    @Override
    public TrieNode<T> search(String word) {
    	int length=word.length();
        TrieNode<T>[] current;
        current=root;
        for(int i=0;i<length;i++) {
        	if(current[word.charAt(i)-' ']==null) {
        		return null;
        	}
        	else {
        		if(i==length-1) {
        			if(current[word.charAt(i)-' '].obj!=null) {
        				return current[word.charAt(i)-' '];
        			}
        			else {
        				return null;
        			}
        		}
        		current=current[word.charAt(i)-' '].next;
        	}
        }
        return null;
    }

    @Override
    public TrieNode<T> startsWith(String prefix) {
    	int length=prefix.length();
        TrieNode<T>[] current;
        current=root;
        for(int i=0;i<length;i++) {
        	if(current[prefix.charAt(i)-' ']==null) {
        		return null;
        	}
        	else {
        		if(i==length-1) {
        			return current[prefix.charAt(i)-' '];
        		}
        		current=current[prefix.charAt(i)-' '].next;
        	}
        }
        return null;
    }

    @Override
    public void printTrie(TrieNode trieNode) {
    	if(trieNode.obj!=null) {
    		System.out.println(trieNode.obj);
    	}
        for(int i=0;i<95;i++) {
        	if(trieNode.next[i]!=null) {
        		printTrie(trieNode.next[i]);
        	}
        }
    }

    @Override
    public boolean insert(String word, Object value) {
    	int length=word.length();
        TrieNode<T>[] current;
        current=root;
        for(int i=0;i<length;i++) {
        	if(current[word.charAt(i)-' ']==null) {
        		current[word.charAt(i)-' ']=new TrieNode<T>();
        	}
        	if(i==length-1) {
        		if(current[word.charAt(i)-' '].obj!=null) {
        			return false;
        		}
        		else {
        			current[word.charAt(i)-' '].obj=(T)value;
        			return true;
        		}
        	}
        	current=current[word.charAt(i)-' '].next;
        }
        return false;
    }

    boolean b=false;
    String seq="";
    
    public void printlvl(TrieNode<T> trieNode,int lvl,char c) {
		if(lvl==0) {
			if(c!=' ') {
				int length=seq.length();
				seq=seq+c;
				for(int i=length-1;i>=0;i--) {
					if(seq.charAt(i)-seq.charAt(i+1)>0) {
						char temp1=seq.charAt(i);
						char temp2=seq.charAt(i+1);
						seq=seq.substring(0,i)+temp2+temp1+seq.substring(i+2,length+1);
					}
					else {
						break;
					}
				}
				b=true;
			}
		}
		else {
			int tempo=lvl-1;
			for(int i=0;i<95;i++) {
	    		if(trieNode.next[i]!=null) {
	        		printlvl(trieNode.next[i],tempo,(char)(i+32));
	        	}
	    	}
		}
	}
    
    @Override
    public void printLevel(int level) {
    	b=false;
    	seq="";
    	System.out.print("Level "+level+": ");
    	int tempo=level-1;
    	for(int i=0;i<95;i++) {
    		if(root[i]!=null) {
        		printlvl(root[i],tempo,(char)(i+32));
        	}
    	}
    	int length=seq.length();
    	if(length>=1) {
	    	for(int i=0;i<length-1;i++) {
	    		System.out.print(seq.charAt(i)+",");
	    	}
	    	System.out.println(seq.charAt(length-1));
    	}
    	else {
    		System.out.println();
    	}
    	System.out.println("-------------");
    }
    
    public void printLevelL(int level) {
    	b=false;
    	seq="";
    	System.out.print("Level "+level+": ");
    	int tempo=level-1;
    	for(int i=0;i<95;i++) {
    		if(root[i]!=null) {
        		printlvl(root[i],tempo,(char)(i+32));
        	}
    	}
    	int length=seq.length();
    	if(length>=1) {
	    	for(int i=0;i<length-1;i++) {
	    		System.out.print(seq.charAt(i)+",");
	    	}
	    	System.out.println(seq.charAt(length-1));
    	}
    	else {
    		System.out.println();
    	}
    }
    
    @Override
    public void print() {
    	System.out.println("Printing Trie");
    	int i=1;
    	b=true;
    	while(b) {
    		b=false;
    		seq="";
    		printLevelL(i);
    		i++;
    	}
    	System.out.println("-------------");
    }
}