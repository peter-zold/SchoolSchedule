package schedule.version3.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relationships {


    public static Map<String, ArrayList<Integer>> classesOfGradeLessons = new HashMap<>();
    public static Map<String, Integer> gradeLessonPerWeek = new HashMap<>();
    private static List<Integer> codes = new ArrayList<>();


    private Map<String, ArrayList<Integer>> instanceClassesOfGradeLessons;
    private Map<String, Integer> instanceGradeLessonPerWeek;
    private List<Integer> instanceCodes;

    private Map<String, ArrayList<Integer>> placeOfGradeLessons = new HashMap<>();

    Relationships() {
        this.instanceCodes = new ArrayList<>(codes);
        this.instanceGradeLessonPerWeek = new HashMap<>(gradeLessonPerWeek);
        this.instanceClassesOfGradeLessons = new HashMap<>(classesOfGradeLessons);
    }

    public Map<String, ArrayList<Integer>> getPlaceOfGradeLessons() {
        return placeOfGradeLessons;
    }

    public Map<String, Integer> getGradeLessonPerWeek() {
        return instanceGradeLessonPerWeek;
    }

    public List<Integer> getCodes() {
        return instanceCodes;
    }

    public void addInstanceCodes(int code) {
        if (!instanceCodes.contains(code)) {
            instanceCodes.add(code);
        }
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
        return instanceClassesOfGradeLessons;
    }

    public static void putClassesOfGradeLessons(String gradeCode, int classIndex) {
        if (classesOfGradeLessons.containsKey(gradeCode)) {
            ArrayList<Integer> tmpList = classesOfGradeLessons.get(gradeCode);
            if (!tmpList.contains(classIndex)) {
                classesOfGradeLessons.remove(gradeCode);
                tmpList.add(classIndex);
                classesOfGradeLessons.put(gradeCode, tmpList);
            }
        } else {
            classesOfGradeLessons.put(gradeCode, new ArrayList<>(List.of(classIndex)));
            codes.add(Integer.parseInt(gradeCode));
        }
    }

    public void putInstanceClassesOfGradeLessons(String gradeCode, List<Integer> classIndex) {
        instanceClassesOfGradeLessons.put(gradeCode, new ArrayList<>(classIndex));
    }
}
