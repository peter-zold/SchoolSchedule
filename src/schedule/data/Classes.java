package schedule.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Classes {
    List<Subject> subjects = new ArrayList<>();
    String className;
    Classes(List<Subject> subjects, String className){

        Iterator<Subject> iterator = subjects.iterator();
        while (iterator.hasNext()){
            Subject subject = iterator.next();
            this.subjects.add(subject);
        }
        this.className = className;
    }

}
