package schedule.data;

public class Lesson {
    String nameOfLesson;
    Teacher teacher;
    Lesson(String nameOfLesson, String nameOfTeacher ){
        this.nameOfLesson = nameOfLesson;
        this.teacher = new Teacher(nameOfTeacher);
    }
}
