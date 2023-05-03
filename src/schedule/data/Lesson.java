package schedule.data;

public class Lesson {
    private String nameOfLesson;
    private Teacher teacher;
    private int valueOfFreeness;

    public Lesson(String nameOfLesson, String nameOfTeacher, int valueOfFreeness){
        this.nameOfLesson = nameOfLesson;
        this.teacher = new Teacher(nameOfTeacher);
        this.valueOfFreeness = valueOfFreeness;
    }

    public String getNameOfLesson() {
        return nameOfLesson;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public int getValueOfFreeness() {
        return valueOfFreeness;
    }

    public void setValueOfFreeness(int valueOfFreeness) {
        this.valueOfFreeness = valueOfFreeness;
    }

    //I had to insert a toString because I could not test or display it otherwise. - Simon

    @Override
    public String toString() {
        return " | " + nameOfLesson + " | " + teacher + "| " + valueOfFreeness + " |]";
    }
}
