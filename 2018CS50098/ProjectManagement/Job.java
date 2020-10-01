package ProjectManagement;

public class Job implements Comparable<Job> , JobReport_ {
	public String name;
	public Project project;
	public User user;
	public Integer runtime;
	public String jobstatus;
	public Integer endtime;
	public Integer arrival_time;
	public Integer priority;
	public Integer cache;
	public Job(String name,Project project,User user,int runtime,int arrival_time) {
		this.name=name;
		this.project=project;
		this.user=user;
		this.runtime=runtime;
		this.jobstatus="NOT FINISHED";
		this.endtime=99999999;
		this.arrival_time=arrival_time;
		this.project.job_list.add(this);
		this.user.job_list.add(this);
		this.priority=this.project.priority;
	}
	@Override
	public String user() {
		return this.user.name;
	}
	@Override
	public String project_name() {
		return this.project.name;
	}
	@Override
	public int budget() {
		return this.project.budget;
	}
	@Override
	public int arrival_time() {
		return this.arrival_time;
	}
	@Override
	public int completion_time() {
		return endtime;
	}
	public String getName() {
		return name;
	}
    @Override
    public int compareTo(Job job) {
        return (this.priority-job.priority);
    }
}