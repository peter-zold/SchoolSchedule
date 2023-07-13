package schedule.version3.data;

public class Lesson {
    private String nameOfLesson;
    private Teacher teacher;
    private int valueOfFreeness;
    private String groupID;
    private int howManyPart;
    private int hoursPerWeek;


    public Lesson(String groupName, String nameOfLesson, String nameOfTeacher, int valueOfFreeness, int hoursPerWeek) {
        this.nameOfLesson = nameOfLesson;
        this.teacher = new Teacher(nameOfTeacher);
        this.valueOfFreeness = valueOfFreeness;
        this.hoursPerWeek = hoursPerWeek;
        if (groupName.equals("")) {
            this.groupID = "000";

            this.howManyPart = 1;
        } else if (groupName.length()==5){
            this.groupID = groupName.substring(1,4);
            this.howManyPart = Integer.parseInt(groupName.substring(groupName.length()-1));

        } else if (groupName.length()==4){
            this.groupID = groupName.substring(1);

        } else {
            this.groupID = groupName.substring(1,4);
            this.howManyPart = Integer.parseInt(groupName.substring(groupName.length()-1));
        }
    }
    void setGrouId(String groupID){
        this.groupID = groupID;
    }

    public int getHowManyPart() {
        return howManyPart;
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

    public String getGroupID() {
        return groupID;
    }


    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setValueOfFreeness(int valueOfFreeness) {
        this.valueOfFreeness = valueOfFreeness;
    }

    //I had to insert a toString method because I could not test or display it otherwise. - Simon
    @Override
    public String toString() {

        return " | " + nameOfLesson + " | " + teacher + "| " + valueOfFreeness + " |" + groupID;
    }
}
