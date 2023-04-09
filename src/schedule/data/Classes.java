package schedule.data;
import java.util.ArrayList;
import java.util.List;


public class Classes {
    private String className;

    private List<Lesson> allLessons = new ArrayList<>();

    private int lessonsPerWeek;
    Classes(String className){
        this.className = className;
    }

    public List<Lesson> getAllLessons() {
        return allLessons;
    }

    public void addLessons(Lesson lesson) {
        this.allLessons.add(lesson);
    }

    public int getLessonsPerWeek() {
        return lessonsPerWeek;
    }

    public void setLessonsPerWeek(int lessonsPerWeek) {
        this.lessonsPerWeek = lessonsPerWeek;
    }

    public String getClassName() {
        return className;
    }
}
