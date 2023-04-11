package schedule.data;

public class Teacher {
    String nameOfTeacher;
    Teacher(String nameOfTeacher){
        this.nameOfTeacher = nameOfTeacher;
    }
    public String getName() {
        return this.nameOfTeacher;
    }
    @Override
    public boolean equals(Object object) {
        Teacher anotherPerson= (Teacher) object; //downcasting from object to Person
        if (!this.nameOfTeacher.equals(anotherPerson.getName())) {
            return false;
        }
        return true;
    }
}
