package schedule.version3.data;

public class Teacher {
    String nameOfTeacher;
    public Teacher(String nameOfTeacher){
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

    //I had to insert a toString method because I could not test or display it otherwise. - Simon
    @Override
    public String toString() {
        return nameOfTeacher + " ";
    }
}
