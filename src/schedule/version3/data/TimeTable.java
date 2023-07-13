package schedule.version3.data;

import java.util.*;

public class TimeTable {


    public static List<Lesson>[][] createRandomTimeTable(List<Classes> classes) {
        List<Integer>[] siteOfHoles = new ArrayList[classes.size()];
        Set<Integer>[] siteOfReservedPlaces = new HashSet[classes.size()];
        List<Lesson>[][] randomTimeTable = new ArrayList[classes.size()][];
        int[][] randomHoursPerDay = new int[classes.size()][];
        //int[] randomHoursPerDay = null;

        Relationships relationships = new Relationships();
        // Összes osztály lyukasóráinak előállítása
        for (int i = 0; i < classes.size(); i++) {
            randomHoursPerDay[i] = siteOfHoleInTimeTable(classes.get(i));
            siteOfHoles[i] = calculatesiteOfFreePeriod(randomHoursPerDay[i]);
            siteOfReservedPlaces[i] = new HashSet<>(siteOfHoles[i]);
        }
        // Ha van több ugyanolyan kódú évfolyam csoportbontás, akkor annak meghatározása, hogy melyik órák lesznek párhuzamosan
        gradeLessonsInSomeTimeSlot(classes, relationships);
        System.out.println(classes.get(0).getGradeLessons());

        // a letárolt kapcsolatokon végigmegyünk és mindegyik évfolyam órának meghatározzuk a helyeit
        findPlacesOfGradeLessons(relationships, siteOfReservedPlaces);

        // előállítjuk az osztályok véletlen órarendjét
        for (int i = 0; i < classes.size(); i++) {
            List<Lesson> clonedLessons = new ArrayList<>(classes.get(i).getAllLessons());
            Collections.shuffle(clonedLessons);

            randomTimeTable[i] = lessonsInTimeTable(clonedLessons, classes.get(i).getGradeLessons(), siteOfHoles[i], relationships);
            //Tesztelés
            printTimeTable(randomTimeTable[i], siteOfHoles[i], randomHoursPerDay[i], classes.get(i));
        }

        return randomTimeTable;
    }

    private static void gradeLessonsInSomeTimeSlot(List<Classes> classes, Relationships relationships) {
        Set<String> mustremovingCodes = new HashSet<>();
        for (int i = 0; i < classes.size(); i += 2) {
            List<Lesson> more = areThereMore(classes.get(i), relationships);
            mustremovingCodes.addAll(more.stream().map(l -> l.getGroupID().substring(0, 2)).toList());
            makeLessonList(more, relationships, classes.get(i), classes);

        }
        Iterator iterator = mustremovingCodes.iterator();
        while (iterator.hasNext()) {
            String remove = (String) iterator.next();
            relationships.getClassesOfGradeLessons().remove(remove);
            relationships.getPlaceOfGradeLessons().remove(remove);
        }

    }

    private static void makeLessonList(List<Lesson> more, Relationships relationships, Classes oneClass, List<Classes> classes) {
        Collections.shuffle(more);
        Set<Integer> usedIndexes = new HashSet<>();
        List<Lesson> lessonsSameTimeslot = new ArrayList<>();
        int actualIndex = 0;
        while (actualIndex < more.size()) {
            Set<Character> chars = new HashSet<>();
            for (int j = 0; j < more.get(actualIndex).getHowManyPart() - 1; j++) {
                for (int i = actualIndex; i < more.size(); i++) {

                    if (!usedIndexes.contains(i) && !chars.contains(more.get(i).getGroupID().charAt(2))) {
                        chars.add(more.get(i).getGroupID().charAt(2));
                        lessonsSameTimeslot.add(more.get(i));
                        usedIndexes.add(i);
                    }

                }

            }
            lessonsSameTimeslot.sort((p1, p2) -> p1.getGroupID().compareTo(p2.getGroupID()));
            if (!lessonsSameTimeslot.isEmpty()) {
                setGradecodes(lessonsSameTimeslot, relationships, oneClass, classes);
                System.out.println(lessonsSameTimeslot.size());
                System.out.println(lessonsSameTimeslot);
            }
            lessonsSameTimeslot.clear();
            chars.clear();
            actualIndex++;
        }
    }

    private static void setGradecodes(List<Lesson> lessonsSameTimeslot, Relationships relationships, Classes oneClass, List<Classes> classes) {
        List<Integer> codes = relationships.getCodes();
        Collections.sort(codes);
        String actualcode = lessonsSameTimeslot.get(0).getGroupID().substring(0, 2);
        int nextCode = codes.get(codes.size() - 1) + 1;
        String nextCodeInString = String.valueOf(nextCode);

        List<Integer> tmpList = relationships.getClassesOfGradeLessons().get(lessonsSameTimeslot.get(0).getGroupID().substring(0, 2));
        relationships.putInstanceClassesOfGradeLessons(nextCodeInString, tmpList);

        relationships.getGradeLessonPerWeek().put(nextCodeInString, 1);

        relationships.addInstanceCodes(nextCode);

        for (int i = 0; i < lessonsSameTimeslot.size(); i++) {
            String lastCharacter = lessonsSameTimeslot.get(i).getGroupID().substring(2);

            String groupName = "/" + nextCodeInString + lastCharacter + lessonsSameTimeslot.get(i).getHoursPerWeek();
            String nameOfLesson = lessonsSameTimeslot.get(i).getNameOfLesson();
            String nameOfTeacher = lessonsSameTimeslot.get(i).getTeacher().nameOfTeacher;
            int valueOfFreeness = 0;
            int hoursPerWeek = 1;


            List<Integer> relationsList = relationships.getClassesOfGradeLessons().get(actualcode);

            for (int j = 0; j < relationsList.size(); j++) {
                classes.get(relationsList.get(j)).getGradeLessons().add(new Lesson(groupName, nameOfLesson, nameOfTeacher, valueOfFreeness, hoursPerWeek));
                // törölni ha megegyezik pár paraméter
                for (int k = 0; k < classes.get(relationsList.get(j)).getGradeLessons().size(); k++) {
                    if (classes.get(relationsList.get(j)).getGradeLessons().get(k).getGroupID() == lessonsSameTimeslot.get(i).getGroupID() && classes.get(relationsList.get(j)).getGradeLessons().get(k).getNameOfLesson() == lessonsSameTimeslot.get(i).getNameOfLesson()) {
                        classes.get(relationsList.get(j)).getGradeLessons().remove(k);
                    }

                }
            }
        }
    }

    private static List<Lesson> areThereMore(Classes classes, Relationships relationships) {
        List<Lesson> more = new ArrayList<>();
        Map<String, Lesson> filter = new HashMap<>();
        System.out.println(classes.getGradeLessons());
        for (int j = 0; j < classes.getGradeLessons().size(); j++) {
            if (filter.containsKey(classes.getGradeLessons().get(j).getGroupID())) {
                more.add(classes.getGradeLessons().get(j));
                more.add(filter.get(classes.getGradeLessons().get(j).getGroupID()));
            } else {
                filter.put(classes.getGradeLessons().get(j).getGroupID(), classes.getGradeLessons().get(j));
            }
        }
        List<Lesson> result = new ArrayList<>(more);
        for (int i = 0; i < more.size(); i++) {
            int hoursPerWeek = more.get(i).getHoursPerWeek();
            System.out.println(hoursPerWeek);
            for (int j = 0; j < hoursPerWeek - 1; j++) {
                result.add(more.get(i));
            }
        }
        return result;
    }

    private static void findPlacesOfGradeLessons(Relationships relationships, Set<Integer>[] siteOfReservedPlaces) {
        System.out.println(relationships.getClassesOfGradeLessons());
        Iterator iterator = relationships.getClassesOfGradeLessons().entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry dataOfGradeLesson = (Map.Entry) iterator.next();
            String code = (String) dataOfGradeLesson.getKey();
            dataOfGradeLesson.getValue();
            List<Integer> gradeClassList = (ArrayList) dataOfGradeLesson.getValue();
            int hourPerWeek = relationships.getGradeLessonPerWeek().get(code);
            Set<Integer> badTimeSlots = new HashSet<>();

            for (int j = 0; j < gradeClassList.size(); j++) {
                badTimeSlots.addAll(siteOfReservedPlaces[gradeClassList.get(j)]);
            }

            for (int k = 0; k < hourPerWeek; k++) {
                boolean put;
                do {
                    Random random = new Random();
                    int place = random.nextInt(Constants.MAXIMUM_HOURS_PER_WEEK);
                    put = badTimeSlots.add(place);
                    if (put) {
                        relationships.putPlaceOfGradeLessons(code, place);
                        badTimeSlots.add(place);
                        for (int j = 0; j < gradeClassList.size(); j++) {
                            siteOfReservedPlaces[j].add(place);
                        }
                    }

                } while (!put);
            }
        }
    }


    private static int[] siteOfHoleInTimeTable(Classes classes) {
        Random random = new Random();

        double hoursPerDay = (double) classes.getLessonsPerWeek() / 5;
        int hoursperDayRounded = roundingData(hoursPerDay);
        int[] randomHoursPerDay = new int[5];
        int sumOfHours = 0;

        // Napi óraszámok meghatározása
        do {
            for (int i = 0; i < randomHoursPerDay.length - 1; i++) {
                randomHoursPerDay[i] = random.nextInt(3) + hoursperDayRounded - 1;
                sumOfHours += randomHoursPerDay[i];
            }
            randomHoursPerDay[4] = classes.getLessonsPerWeek() - sumOfHours;
            sumOfHours = 0;

        } while (randomHoursPerDay[4] < hoursperDayRounded - 1 || hoursperDayRounded + 1 < randomHoursPerDay[4]);
        return randomHoursPerDay;
    }

    // A napi óraszámokból a lyukak helyének meghatározása
    private static List<Integer> calculatesiteOfFreePeriod(int[] HoursPerDay) {
        List<Integer> siteOfHole = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int actualHole = HoursPerDay[i] + 1;
            siteOfHole.add(i * 9);

            while (actualHole <= 8) {
                siteOfHole.add(i * 9 + actualHole);
                actualHole++;
            }
        }
        return siteOfHole;
    }

    private static List<Lesson>[] lessonsInTimeTable(List<Lesson> lessons, List<Lesson> gradeLessons, List<Integer> siteOfFreePeriod, Relationships relationships) {
        List<Lesson>[] classLessons = new ArrayList[Constants.MAXIMUM_HOURS_PER_WEEK];

        // Lyukasórák behelyezése
        Iterator iterator = siteOfFreePeriod.iterator();
        while (iterator.hasNext()) {
            int hole = (int) iterator.next();
            int valueOfFreeness = calculateValueOfFreeness(hole);
            List<Lesson> holes = new ArrayList<>();
            holes.add(new Lesson("/000", "Free Period", "none", valueOfFreeness, 0));
            classLessons[hole] = holes;
        }

        // évfolyam órák behelyezése
        for (int i = 0; i < gradeLessons.size(); i++) {

            String code = gradeLessons.get(i).getGroupID().substring(0, 2);
            System.out.println(gradeLessons.get(i).getNameOfLesson() + " " + code);
            System.out.println(relationships.getPlaceOfGradeLessons());
            for (int j = 0; j < relationships.getPlaceOfGradeLessons().get(code).size(); j++) {
                List<Lesson> subject = new ArrayList<>();
                int place = relationships.getPlaceOfGradeLessons().get(code).get(j);
                subject.add(gradeLessons.get(i));
                if (classLessons[place] == null) {
                    classLessons[place] = subject;
                } else {
                    classLessons[place].addAll(subject);
                }
            }
        }

        // Tantárgyak behelyezése
        int indexOflessons = 0;
        Set<Integer> indexes = new HashSet<>();

        for (int i = 0; i < classLessons.length; i++) {
            if (classLessons[i] == null) {
                List<Lesson> subject = new ArrayList<>();

                if (lessons.get(indexOflessons).getGroupID().equals("000") && !indexes.contains(indexOflessons)) {

                    subject.add(lessons.get(indexOflessons));
                    indexes.add(indexOflessons);


                } else if (lessons.get(indexOflessons).getGroupID().startsWith("0")) {

                    int tempIndexOfLessons = indexOflessons;
                    Set<Character> whichGroup = new HashSet<>();

                    while (tempIndexOfLessons < lessons.size()) {
                        if (!indexes.contains(tempIndexOfLessons)) {
                            subject.add(lessons.get(tempIndexOfLessons));
                            indexes.add(tempIndexOfLessons);
                            whichGroup.add(lessons.get(tempIndexOfLessons).getGroupID().charAt(2));

                            for (int k = 0; k < lessons.get(tempIndexOfLessons).getHowManyPart() - 1; k++) {
                                for (int j = indexOflessons; j < lessons.size(); j++) {

                                    if (!indexes.contains(j) && lessons.get(tempIndexOfLessons).getGroupID().charAt(1) == lessons.get(j).getGroupID().charAt(1) && lessons.get(j).getGroupID().charAt(2) != '0' && whichGroup.add(lessons.get(j).getGroupID().charAt(2))) {
                                        subject.add(lessons.get(j));
                                        indexes.add(j);
                                        whichGroup.add(lessons.get(j).getGroupID().charAt(2));
                                        subject.sort((p1, p2) -> p1.getGroupID().compareTo(p2.getGroupID()));
                                        break;

                                    }
                                }
                            }

                            break;
                        }
                        tempIndexOfLessons++;
                    }
                }

                classLessons[i] = subject;
                indexOflessons++;
            }
        }
        return classLessons;
    }

    //Comment: This calculation does not work properly, try it with 18, wrong value. - Simon
    private static int calculateValueOfFreeness(int serialNum) {
        int valueOfFreeness = 0;

        switch (serialNum % 9) {
            case 0:
                valueOfFreeness = 1;
                break;
            case 6:
                valueOfFreeness = 2;
                break;
            case 7:
                valueOfFreeness = 3;
                break;
            case 8:
                valueOfFreeness = 4;
                break;
            default:
                System.out.println("Valami baj van a lyukas óra előállításnál");
                break;
        }

        return valueOfFreeness;
    }

    // Kerekítés
    private static int roundingData(double hoursPerDay) {
        int roundedData = (int) hoursPerDay;
        if (hoursPerDay - roundedData < 0.5) {
            return roundedData;
        } else {
            return roundedData + 1;
        }
    }

    private static void printTimeTable(List<Lesson>[] classLessons, List<Integer> siteOfFreePeriod, int[] randomHoursPerDay, Classes classes) {
        // Tesztelés
        System.out.println();
        System.out.println("Melyik nap hány órája van az osztálynak");
        System.out.print(Arrays.toString(randomHoursPerDay));
        System.out.println();
        System.out.println("Lyukas órák helye");
        System.out.println(siteOfFreePeriod.toString());
        System.out.println();

        System.out.println("A " + classes.getClassName() + " osztály órarendje:");
        System.out.println("Soronként 1-1 nap órarendje hétfőtől péntekig: ");
        for (int i = 0; i < classLessons.length; i++) {

        }


        System.out.println();
        System.out.println();
        System.out.println("Az összes óra listázva sorban");
        for (int i = 0; i < classLessons.length; i++) {
            System.out.print(i + ". óra: ");
            for (int j = 0; j < classLessons[i].size(); j++) {
                System.out.print(classLessons[i].get(j).getGroupID() + " " + classLessons[i].get(j).getNameOfLesson() + "    ");
            }
            System.out.println();


        }
    }

    public static void main(String[] args) {
        DataScan data2 = new DataScan();
        data2.scanData();
        TimeTable.createRandomTimeTable(data2.getAllClasses());
    }
}
