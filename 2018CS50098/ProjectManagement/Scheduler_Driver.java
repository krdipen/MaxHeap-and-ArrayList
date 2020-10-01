package ProjectManagement;

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

import Trie.*;
import RedBlack.*;
import PriorityQueue.*;
import java.util.List;

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class Scheduler_Driver extends Thread implements SchedulerInterface {


    public static void main(String[] args) throws IOException {
//

        Scheduler_Driver scheduler_driver = new Scheduler_Driver();
        File file;
        if (args.length == 0) {
            URL url = Scheduler_Driver.class.getResource("INP");
            file = new File(url.getPath());
        } else {
            file = new File(args[0]);
        }

        scheduler_driver.execute(file);
    }

    public void execute(File commandFile) throws IOException {


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(commandFile));

            String st;
            while ((st = br.readLine()) != null) {
                String[] cmd = st.split(" ");
                if (cmd.length == 0) {
                    System.err.println("Error parsing: " + st);
                    return;
                }
                String project_name, user_name;
                Integer start_time, end_time;

                long qstart_time, qend_time;

                switch (cmd[0]) {
                    case "PROJECT":
                        handle_project(cmd);
                        break;
                    case "JOB":
                        handle_job(cmd);
                        break;
                    case "USER":
                        handle_user(cmd[1]);
                        break;
                    case "QUERY":
                        handle_query(cmd[1]);
                        break;
                    case "": // HANDLE EMPTY LINE
                        handle_empty_line();
                        break;
                    case "ADD":
                        handle_add(cmd);
                        break;
                    //--------- New Queries
                    case "NEW_PROJECT":
                    case "NEW_USER":
                    case "NEW_PROJECTUSER":
                    case "NEW_PRIORITY":
                        timed_report(cmd);
                        break;
                    case "NEW_TOP":
                        qstart_time = System.nanoTime();
                        timed_top_consumer(Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                        //System.out.println("Top query");
                        //System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                    case "NEW_FLUSH":
                        qstart_time = System.nanoTime();
                        timed_flush( Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                        //System.out.println("Flush query");
                        //System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                    default:
                        System.err.println("Unknown command: " + cmd[0]);
                }

            }


            run_to_completion();
            print_stats();

        } catch (FileNotFoundException e) {
            System.err.println("Input file Not found. " + commandFile.getAbsolutePath());
        } catch (NullPointerException ne) {
            ne.printStackTrace();

        }
    }
    
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    
    Trie<Project> trie=new Trie<Project>();
    RBTree<Project,Job> rbtree=new RBTree<Project,Job>();
    Trie<User> trie2=new Trie<User>();
    Trie<Job> trie3=new Trie<Job>();
    MaxHeap<Job> maxheap=new MaxHeap<Job>();
    List<Job> printc=new ArrayList<Job>();
    MaxHeap<Project> printr=new MaxHeap<Project>();
    MaxHeap<User> maxheap2=new MaxHeap<User>();
    int time=0;
    int job_remaining=0;
    
    @Override
    public ArrayList<JobReport_> timed_report(String[] cmd) {
        long qstart_time, qend_time;
        ArrayList<JobReport_> res = null;
        switch (cmd[0]) {
            case "NEW_PROJECT":
                qstart_time = System.nanoTime();
                res = handle_new_project(cmd);
                qend_time = System.nanoTime();
                //System.out.println("Project query");
                //System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_USER":
                qstart_time = System.nanoTime();
                res = handle_new_user(cmd);
                qend_time = System.nanoTime();
                //System.out.println("User query");
                //System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_PROJECTUSER":
                qstart_time = System.nanoTime();
                res = handle_new_projectuser(cmd);
                qend_time = System.nanoTime();
                //System.out.println("Project User query");
                //System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_PRIORITY":
                qstart_time = System.nanoTime();
                res = handle_new_priority(cmd[1]);
                qend_time = System.nanoTime();
                //System.out.println("Priority query");
                //System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
        }
        return res;
    }

    @Override
    public ArrayList<UserReport_> timed_top_consumer(int top) {
        ArrayList<UserReport_> top_list=new ArrayList<UserReport_>();
        MaxHeap<User> temp_user_heap=new MaxHeap<User>();
        int size=maxheap2.list.size();
        for(int j=0;j<size;j++) {
        	temp_user_heap.list.add(maxheap2.list.get(j));
        }
        if(top<=size) {
	        for(int j=0;j<top;j++) {
	        	User temp_user=temp_user_heap.extractMax();
	        	if(temp_user.consumed==0) {
	        		break;
	        	}
	        	top_list.add(temp_user);
	        }
        }
        else {
        	for(int j=0;j<size;j++) {
        		User temp_user=temp_user_heap.extractMax();
	        	if(temp_user.consumed==0) {
	        		break;
	        	}
	        	top_list.add(temp_user);
	        }
        }
        return top_list;
    }

    @Override
    public void timed_flush(int waittime) {
    	int count=0;
    	int size=maxheap.list.size();
    	for(int i=0;i<size;i++) {
    		if(time-maxheap.list.get(i).value.arrival_time()>=waittime) {
    			maxheap.list.get(i).value.cache=maxheap.list.get(i).value.priority;
    			maxheap.list.get(i).value.priority=99999999+maxheap.list.get(i).value.priority;
    			count++;
    			int index=i;
    			while(true) {
    		    	if(index==0) {
    		        	break;
    		        }
    		    	if(index%2==0) {
    		    		if(maxheap.list.get(index/2-1).compareTo(maxheap.list.get(index))<0) {
    		    			Node<Job> tempo=maxheap.list.get(index/2-1);
    		    			maxheap.list.set(index/2-1,maxheap.list.get(index));
    		    			maxheap.list.set(index,tempo);
    		    			index=index/2-1;
    		    		}
    		    		else if(maxheap.list.get(index/2-1).compareTo(maxheap.list.get(index))==0) {
    		    			if(maxheap.list.get(index/2-1).time-maxheap.list.get(index).time>0) {
    		    				Node<Job> tempo=maxheap.list.get(index/2-1);
        		    			maxheap.list.set(index/2-1,maxheap.list.get(index));
        		    			maxheap.list.set(index,tempo);
        		    			index=index/2-1;
    		    			}
    		    			else {
    		    				break;
    		    			}
    		    		}
    		    		else {
    		    			break;
    		    		}
    		    	}
    		    	else {
    		    		if(maxheap.list.get((index-1)/2).compareTo(maxheap.list.get(index))<0) {
    						Node<Job> tempo=maxheap.list.get((index-1)/2);
    						maxheap.list.set((index-1)/2,maxheap.list.get(index));
    						maxheap.list.set(index,tempo);
    						index=(index-1)/2;
    					}
    		    		else if(maxheap.list.get((index-1)/2).compareTo(maxheap.list.get(index))==0) {
    		    			if(maxheap.list.get((index-1)/2).time-maxheap.list.get(index).time>0) {
    		    				Node<Job> tempo=maxheap.list.get((index-1)/2);
        						maxheap.list.set((index-1)/2,maxheap.list.get(index));
        						maxheap.list.set(index,tempo);
        						index=(index-1)/2;
    		    			}
    		    			else {
    		    				break;
    		    			}
    		    		}
    					else {
    						break;
   						}
   					}
   		    	}
   			}
    	}
    	for(int i=0;i<count;i++) {
    		execute_a_job();
    	}
    }
    
    private ArrayList<JobReport_> handle_new_priority(String s) {
        ArrayList<JobReport_> priority_list=new ArrayList<JobReport_>();
        MaxHeap<Project> temp_project_heap=new MaxHeap<Project>();
        int size=printr.list.size();
        for(int j=0;j<size;j++) {
        	temp_project_heap.list.add(printr.list.get(j));
        }
    	for(int j=0;j<size;j++) {
    		Project temp_project=temp_project_heap.extractMax();
    		if(temp_project.priority<Integer.parseInt(s)) {
    			break;
    		}
	    	List<Job> job_list=temp_project.job_list;
	    	int size_job_list=job_list.size();
	    	for(int i=0;i<size_job_list;i++) {
	    		if(job_list.get(i).jobstatus!="COMPLETED") {
	    			priority_list.add(job_list.get(i));
	    		}
	    		else {
	    			temp_project.job_list.remove(i);
	    			i--;
	    			size_job_list--;
	    		}
	    	}
    	}
        return priority_list;
    }

    private ArrayList<JobReport_> handle_new_projectuser(String[] cmd) {
    	ArrayList<JobReport_> projectuser_list=new ArrayList<JobReport_>();
    	int size=printc.size();
    	for(int i=0;i<size;i++) {
    		if((printc.get(i).project.name==cmd[1])&&(printc.get(i).user.name==cmd[2])&&(printc.get(i).arrival_time>=Integer.parseInt(cmd[3]))&&(printc.get(i).arrival_time<=Integer.parseInt(cmd[4]))) {
    			projectuser_list.add(printc.get(i));
    		}
    	}
    	int sizep,sizeu;
    	Project temp_project;
    	User temp_user;
    	if(trie.search(cmd[1])!=null){
    		temp_project=trie.search(cmd[1]).obj;
    		sizep=temp_project.job_list.size();
    	}
    	else {
    		return projectuser_list;
    	}
    	if(trie2.search(cmd[2])!=null) {
    		temp_user=trie2.search(cmd[2]).obj;
    		sizeu=temp_user.job_list.size();
    	}
    	else {
    		return projectuser_list;
    	}
    	if(sizep<=sizeu) {
    		for(int i=0;i<sizep;i++) {
    			if((temp_project.job_list.get(i).arrival_time>=Integer.parseInt(cmd[3]))&&(temp_project.job_list.get(i).arrival_time<=Integer.parseInt(cmd[4]))&&(temp_project.job_list.get(i).user.name==cmd[2])&&(temp_project.job_list.get(i).jobstatus!="COMPLETED")) {
	    			projectuser_list.add(temp_project.job_list.get(i));
	    		}
    			if(temp_project.job_list.get(i).arrival_time>Integer.parseInt(cmd[4])) {
    				if(temp_project.job_list.get(i).jobstatus=="COMPLETED") {
        				temp_project.job_list.remove(i);
        				i--;
        				sizep--;
        			}
    				break;
    			}
    			if(temp_project.job_list.get(i).jobstatus=="COMPLETED") {
    				temp_project.job_list.remove(i);
    				i--;
    				sizep--;
    			}
    		}
    	}
    	else {
    		for(int i=0;i<sizeu;i++) {
    			if((temp_user.job_list.get(i).arrival_time>=Integer.parseInt(cmd[3]))&&(temp_user.job_list.get(i).arrival_time<=Integer.parseInt(cmd[4]))&&(temp_user.job_list.get(i).project.name==cmd[1])&&(temp_user.job_list.get(i).jobstatus!="COMPLETED")) {
	    			projectuser_list.add(temp_user.job_list.get(i));
	    		}
    			if(temp_user.job_list.get(i).arrival_time>Integer.parseInt(cmd[4])) {
    				if(temp_user.job_list.get(i).jobstatus=="COMPLETED") {
        				temp_user.job_list.remove(i);
        				i--;
        				sizep--;
        			}
        			break;
        		}
    			if(temp_user.job_list.get(i).jobstatus=="COMPLETED") {
    				temp_user.job_list.remove(i);
    				i--;
    				sizep--;
    			}
    		}
    	}
    	return projectuser_list;
    }

    private ArrayList<JobReport_> handle_new_user(String[] cmd) {
    	ArrayList<JobReport_> user_list=new ArrayList<JobReport_>();
    	int size=printc.size();
    	for(int i=0;i<size;i++) {
    		if((printc.get(i).user.name==cmd[1])&&(printc.get(i).arrival_time>=Integer.parseInt(cmd[2]))&&(printc.get(i).arrival_time<=Integer.parseInt(cmd[3]))) {
    			user_list.add(printc.get(i));
    		}
    	}
    	if(trie2.search(cmd[1])!=null) {
	    	User temp_user=trie2.search(cmd[1]).obj;
	    	size=temp_user.job_list.size();
	    	for(int i=0;i<size;i++) {
	    		if((temp_user.job_list.get(i).arrival_time>=Integer.parseInt(cmd[2]))&&(temp_user.job_list.get(i).arrival_time<=Integer.parseInt(cmd[3]))&&(temp_user.job_list.get(i).jobstatus!="COMPLETED")) {
	    			user_list.add(temp_user.job_list.get(i));
	    		}
	    		if(temp_user.job_list.get(i).arrival_time>Integer.parseInt(cmd[3])) {
	    			if(temp_user.job_list.get(i).jobstatus=="COMPLETED") {
	    				temp_user.job_list.remove(i);
	    				i--;
	    				size--;
	    			}
	    			break;
	    		}
	    		if(temp_user.job_list.get(i).jobstatus=="COMPLETED") {
    				temp_user.job_list.remove(i);
    				i--;
    				size--;
    			}
	    	}
    	}
    	return user_list;
    }

    private ArrayList<JobReport_> handle_new_project(String[] cmd) {
    	ArrayList<JobReport_> project_list=new ArrayList<JobReport_>();
    	int size=printc.size();
    	for(int i=0;i<size;i++) {
    		if((printc.get(i).project.name==cmd[1])&&(printc.get(i).arrival_time>=Integer.parseInt(cmd[2]))&&(printc.get(i).arrival_time<=Integer.parseInt(cmd[3]))) {
    			project_list.add(printc.get(i));
    		}
    	}
    	if(trie.search(cmd[1])!=null){
    		Project temp_project=trie.search(cmd[1]).obj;
    		size=temp_project.job_list.size();
    		for(int i=0;i<size;i++) {
    			if((temp_project.job_list.get(i).arrival_time>=Integer.parseInt(cmd[2]))&&(temp_project.job_list.get(i).arrival_time<=Integer.parseInt(cmd[3]))&&(temp_project.job_list.get(i).jobstatus!="COMPLETED")) {
	    			project_list.add(temp_project.job_list.get(i));
	    		}
    			if(temp_project.job_list.get(i).arrival_time>Integer.parseInt(cmd[3])) {
    				if(temp_project.job_list.get(i).jobstatus=="COMPLETED") {
        				temp_project.job_list.remove(i);
        				i--;
        				size--;
        			}
    				break;
    			}
    			if(temp_project.job_list.get(i).jobstatus=="COMPLETED") {
    				temp_project.job_list.remove(i);
    				i--;
    				size--;
    			}
    		}
    	}
    	return project_list;
    }

    @Override
    public void run_to_completion() {
    	while(job_remaining!=0) {
    		schedule();
    	}
    }

    @Override
    public void handle_project(String[] cmd) {
    	System.out.println("Creating project");
    	Project project=new Project(cmd[1],Integer.parseInt(cmd[2]),Integer.parseInt(cmd[3]));
    	if(trie.insert(project.getName(),project)) {
    		printr.insert(project);
    	}
    }

    @Override
    public void handle_job(String[] cmd) {
    	System.out.println("Creating job");
    	if((trie.search(cmd[2])==null)&&(trie2.search(cmd[3])==null)) {
    		System.out.println("No such project exists. "+cmd[2]);
    		System.out.println("No such user exists: "+cmd[3]);
    		return;
    	}
    	if(trie.search(cmd[2])==null) {
    		System.out.println("No such project exists. "+cmd[2]);
    		return;
    	}
    	Project temp_project=trie.search(cmd[2]).obj;
    	if(trie2.search(cmd[3])==null) {
    		System.out.println("No such user exists: "+cmd[3]);
    		return;
    	}
    	User temp_user=trie2.search(cmd[3]).obj;
    	Job job=new Job(cmd[1],temp_project,temp_user,Integer.parseInt(cmd[4]),time);
    	//if(trie3.insert(job.getName(),job))
    	//{
    		trie3.insert(job.getName(),job);
    		maxheap.insert(job);
    		job_remaining++;
    	//}
    }

    @Override
    public void handle_user(String name) {
    	System.out.println("Creating user");
    	User user=new User(name);
    	if(trie2.insert(user.getName(),user)) {
    		maxheap2.insert(user);
    	}
    }

    @Override
    public void handle_query(String key) {
    	
    	System.out.println("Querying");
    	if(trie3.search(key)!=null) {
    		Job temp_job=trie3.search(key).obj;
    		System.out.println(temp_job.name+": "+temp_job.jobstatus);
    	}
    	else {
    		System.out.println(key+": NO SUCH JOB");
    	}
    }

    @Override
    public void handle_empty_line() {
    	System.out.println("Running code");
		System.out.println("Remaining jobs: "+job_remaining);
    	while(true) {
	    	Job temp_job=maxheap.extractMax();
	    	if(temp_job==null) {
	    		System.out.println("Execution cycle completed");
	    		return;
	    	}
	    	job_remaining--;
			System.out.println("Executing: "+temp_job.name+" from: "+temp_job.project.name);
	    	if(temp_job.runtime<=temp_job.project.budget) {
	    		printc.add(temp_job);
	    		temp_job.project.budget=temp_job.project.budget-temp_job.runtime;
	    		System.out.println("Project: "+temp_job.project.name+" budget remaining: "+temp_job.project.budget);
	    		temp_job.jobstatus="COMPLETED";
	    		temp_job.user.consumed=temp_job.user.consumed+temp_job.runtime;
	    		bubbleup(maxheap2,temp_job.user);
	    		time=time+temp_job.runtime;
	    		temp_job.endtime=time;
	    		System.out.println("Execution cycle completed");
	    		return;
	    	}
	    	else {
		    	rbtree.insert(temp_job.project,temp_job);
		    	System.out.println("Un-sufficient budget.");
		    	temp_job.jobstatus="REQUESTED";
	    	}
    	}
    }

    @Override
    public void handle_add(String[] cmd) {
    	System.out.println("ADDING Budget");
    	if(trie.search(cmd[1])==null) {
    		System.out.println("No such project exists. "+cmd[1]);
    		return;
    	}
    	Project temp_project=trie.search(cmd[1]).obj;
    	temp_project.budget=temp_project.budget+Integer.parseInt(cmd[2]);
    	List<Job> list=rbtree.search(temp_project).getValues();
    	if(list!=null) {
	    	int size=list.size();
	    	for(int i=0;i<size;i++) {
	    		maxheap.insert(list.remove(0));
	    		job_remaining++;
	    	}
    	}
    }

    @Override
    public void print_stats() {
    	System.out.println("--------------STATS---------------");
    	System.out.println("Total jobs done: "+printc.size());
    	for(int i=0;i<printc.size();i++) {
    		Job temp=printc.get(i);
    		System.out.println("Job{user='"+temp.user.name+"', project='"+temp.project.name+"', jobstatus="+temp.jobstatus+", execution_time="+temp.runtime+", end_time="+temp.endtime+", name='"+temp.name+"'}");
    	}
    	System.out.println("------------------------");
    	System.out.println("Unfinished jobs: ");
    	int uf=0;
    	Project temp=printr.extractMax();
    	while(temp!=null) {
    		if(rbtree.search(temp).getValues()!=null) {
	    		List<Job> list=rbtree.search(temp).getValues();
	    		int size=list.size();
	    		for(int i=0;i<size;i++) {
	    			Job temp2=list.remove(0);
	    			System.out.println("Job{user='"+temp2.user.name+"', project='"+temp2.project.name+"', jobstatus="+temp2.jobstatus+", execution_time="+temp2.runtime+", end_time=null, name='"+temp2.name+"'}");
	    			uf++;
	    		}
    		}
    		temp=printr.extractMax();
    	}
    	System.out.println("Total unfinished jobs: "+uf);
    	System.out.println("--------------STATS DONE---------------");
    }

    public void schedule() {
    	System.out.println("Running code");
    	System.out.println("Remaining jobs: "+job_remaining);
    	while(true) {
	    	Job temp_job=maxheap.extractMax();
	    	if(temp_job==null) {
	    		System.out.println("System execution completed");
	    		return;
	    	}
	    	job_remaining--;
			System.out.println("Executing: "+temp_job.name+" from: "+temp_job.project.name);
	    	if(temp_job.runtime<=temp_job.project.budget) {
	    		printc.add(temp_job);
	    		temp_job.project.budget=temp_job.project.budget-temp_job.runtime;
	    		System.out.println("Project: "+temp_job.project.name+" budget remaining: "+temp_job.project.budget);
	    		temp_job.jobstatus="COMPLETED";
	    		temp_job.user.consumed=temp_job.user.consumed+temp_job.runtime;
	    		bubbleup(maxheap2,temp_job.user);
	    		time=time+temp_job.runtime;
	    		temp_job.endtime=time;
	    		System.out.println("System execution completed");
	    		return;
	    	}
	    	else {
	    		rbtree.insert(temp_job.project,temp_job);
	    		System.out.println("Un-sufficient budget.");
	    		temp_job.jobstatus="REQUESTED";
	    	}
    	}
    }
    
    public void execute_a_job() {
	    	Job temp_job=maxheap.extractMax();
	    	if(temp_job==null) {
	    		return;
	    	}
	    	job_remaining--;
	    	temp_job.priority=temp_job.cache;
	    	if(temp_job.runtime<=temp_job.project.budget) {
	    		printc.add(temp_job);
	    		temp_job.project.budget=temp_job.project.budget-temp_job.runtime;
	    		temp_job.jobstatus="COMPLETED";
	    		temp_job.user.consumed=temp_job.user.consumed+temp_job.runtime;
	    		bubbleup(maxheap2,temp_job.user);
	    		time=time+temp_job.runtime;
	    		temp_job.endtime=time;
	    	}
	    	else {
		    	rbtree.insert(temp_job.project,temp_job);
	    		temp_job.jobstatus="REQUESTED";
	    	}
    }
    
    public void bubbleup(MaxHeap<User> maxheap2,User user) {
    	int size=maxheap2.list.size();
    	int index=0;
    	for(int i=0;i<size;i++) {
    		if(maxheap2.list.get(i).value==user) {
    			maxheap2.time++;
    			maxheap2.list.get(i).time=maxheap2.time;
    			index=i;
    			break;
    		}
    	}
    	while(true) {
    		if(index==0) {
        		return;
        	}
    		if(index%2==0) {
    			if(maxheap2.list.get(index/2-1).compareTo(maxheap2.list.get(index))<0) {
    				Node<User> tempo=maxheap2.list.get(index/2-1);
    				maxheap2.list.set(index/2-1,maxheap2.list.get(index));
    				maxheap2.list.set(index,tempo);
    				index=index/2-1;
    			}
    			else if(maxheap2.list.get(index/2-1).compareTo(maxheap2.list.get(index))==0) {
    				if(maxheap2.list.get(index/2-1).time-maxheap2.list.get(index).time>0) {
    					Node<User> tempo=maxheap2.list.get(index/2-1);
        				maxheap2.list.set(index/2-1,maxheap2.list.get(index));
        				maxheap2.list.set(index,tempo);
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
				if(maxheap2.list.get((index-1)/2).compareTo(maxheap2.list.get(index))<0) {
					Node<User> tempo=maxheap2.list.get((index-1)/2);
					maxheap2.list.set((index-1)/2,maxheap2.list.get(index));
					maxheap2.list.set(index,tempo);
					index=(index-1)/2;
				}
				else if(maxheap2.list.get((index-1)/2).compareTo(maxheap2.list.get(index))==0) {
					if(maxheap2.list.get((index-1)/2).time-maxheap2.list.get(index).time>0) {
						Node<User> tempo=maxheap2.list.get((index-1)/2);
						maxheap2.list.set((index-1)/2,maxheap2.list.get(index));
						maxheap2.list.set(index,tempo);
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
    
    public void timed_handle_user(String name){
    	User user=new User(name);
    	if(trie2.insert(user.getName(),user)) {
    		maxheap2.insert(user);
    	}
    }
    
    public void timed_handle_job(String[] cmd){
    	if((trie.search(cmd[2])==null)&&(trie2.search(cmd[3])==null)) {
    		return;
    	}
    	if(trie.search(cmd[2])==null) {
    		return;
    	}
    	Project temp_project=trie.search(cmd[2]).obj;
    	if(trie2.search(cmd[3])==null) {
    		return;
    	}
    	User temp_user=trie2.search(cmd[3]).obj;
    	Job job=new Job(cmd[1],temp_project,temp_user,Integer.parseInt(cmd[4]),time);
    	//if(trie3.insert(job.getName(),job))
    	//{
    		trie3.insert(job.getName(),job);
    		maxheap.insert(job);
    		job_remaining++;
    	//}
    }
    
    public void timed_handle_project(String[] cmd){
    	Project project=new Project(cmd[1],Integer.parseInt(cmd[2]),Integer.parseInt(cmd[3]));
    	if(trie.insert(project.getName(),project)) {
    		printr.insert(project);
    	}
    }
    
    public void timed_run_to_completion(){
    	while(job_remaining!=0) {
    		timed_schedule();
    	}
    }
    
    public void timed_schedule() {
    	while(true) {
	    	Job temp_job=maxheap.extractMax();
	    	if(temp_job==null) {
	    		return;
	    	}
	    	job_remaining--;
	    	if(temp_job.runtime<=temp_job.project.budget) {
	    		printc.add(temp_job);
	    		temp_job.project.budget=temp_job.project.budget-temp_job.runtime;
	    		temp_job.jobstatus="COMPLETED";
	    		temp_job.user.consumed=temp_job.user.consumed+temp_job.runtime;
	    		bubbleup(maxheap2,temp_job.user);
	    		time=time+temp_job.runtime;
	    		temp_job.endtime=time;
	    		return;
	    	}
	    	else {
	    		rbtree.insert(temp_job.project,temp_job);
	    		temp_job.jobstatus="REQUESTED";
	    	}
    	}
    }
    
    
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    
}