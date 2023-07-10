package schedule.version3.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relationships {
    private Map<String,ArrayList<Integer>> placeOfGradeLessons = new HashMap<>();
    public static Map<String, ArrayList<Integer>> classesOfGradeLessons = new HashMap<>();
    public static Map<String, Integer> gradeLessonPerWeek = new HashMap<>();

    public Map<String, ArrayList<Integer>> getPlaceOfGradeLessons() {
        return placeOfGradeLessons;
    }

    public void putPlaceOfGradeLessons(String gradeCode, int place) {

        if (placeOfGradeLessons.containsKey(gradeCode)) {
            ArrayList<Integer> tmpList = placeOfGradeLessons.get(gradeCode);
            placeOfGradeLessons.remove(gradeCode);
            tmpList.add(place);
            this.placeOfGradeLessons.put(gradeCode, tmpList);
        } else {
            this.placeOfGradeLessons.put(gradeCode, new ArrayList<>(List.of(place)));
        }
    }

    public Map<String, ArrayList<Integer>> getClassesOfGradeLessons() {
        return classesOfGradeLessons;
    }

    public static void putClassesOfGradeLessons(String gradeCode, int classIndex) {
        if (classesOfGradeLessons.containsKey(gradeCode)){
            ArrayList<Integer> tmpList = classesOfGradeLessons.get(gradeCode);
            if (!tmpList.contains(classIndex)) {
                classesOfGradeLessons.remove(gradeCode);
                tmpList.add(classIndex);
                classesOfGradeLessons.put(gradeCode, tmpList);
            }
        } else {
            classesOfGradeLessons.put(gradeCode, new ArrayList<>(List.of(classIndex)));
        }
    }
}
