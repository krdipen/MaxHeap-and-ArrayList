package PriorityQueue;

public class Student implements Comparable<Student> {
    private String name;
    private Integer marks;

    public Student(String trim, int parseInt) {
    	this.name=trim;
    	this.marks=parseInt;
    }
    
    @Override
    public int compareTo(Student student) {
        return (this.marks-student.getMarks()); 
    }

    public String getName() {
        return name;
    }
    
    public Integer getMarks() {
        return marks;
    }
    
    public String toString() {
    	return "Student{name='"+  name   +"', marks="+ marks  +"}";
    }
}