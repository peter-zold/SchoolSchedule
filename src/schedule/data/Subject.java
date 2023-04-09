package schedule.data;

public class Subject {
    String className;
    String subjectName;
    int hoursPerWeek;
    String teacherName;
    String teachersMonogram;


    Subject(String className, String subjectName, int hoursPerWeek, String teacherName, String teachersMonogram){
        this.className = className;
        this.subjectName = subjectName;
        this.hoursPerWeek = hoursPerWeek;
        this.teacherName = teacherName;
        this.teachersMonogram = teachersMonogram;
    }
}
