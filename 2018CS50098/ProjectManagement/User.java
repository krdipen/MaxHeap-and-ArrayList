package ProjectManagement;
import java.util.ArrayList;

public class User implements Comparable<User> , UserReport_ {
	public String name;
	public Integer consumed;
	public ArrayList<Job> job_list=new ArrayList<Job>();
	public User(String name) {
		this.name=name;
		this.consumed=0;
	}
	
	@Override
	public String user() {
		return this.name;
	}
	@Override
	public int consumed() {
		return consumed;
	}
	
	public String getName() {
		return name;
	}
    @Override
    public int compareTo(User user) {
        return this.consumed.compareTo(user.consumed);
    }
}