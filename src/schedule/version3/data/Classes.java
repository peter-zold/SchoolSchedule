package schedule.version3.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Classes {
    private String className;

    private List<Lesson> allLessons = new ArrayList<>();
    private List<Lesson> gradeLessons = new ArrayList<>();

    private int grade;

    public List<Lesson> getGradeLessons() {
        return gradeLessons;
    }

    public void addGradeLessons(Lesson gradeLesson) {
        this.gradeLessons.add(gradeLesson);
    }

    private int lessonsPerWeek;
    public Classes(String className){

        this.className = className;
        this.grade = calcGrade();
    }

    public List<Lesson> getAllLessons() {
        return allLessons;
    }

    public int getGrade() {
        return this.grade;
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

    public int calcGrade() {
        String grade = "";
        for (char c : className.toCharArray()) {
            if (Character.isDigit(c))
            {
            grade = grade + c;
            }
        }
        return Integer.parseInt(grade);
    }
}
