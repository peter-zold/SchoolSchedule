package schedule.version3.data;


import java.security.KeyStore;
import java.util.*;

public class TimeTable {


    public static List<Lesson>[][] createRandomTimeTable(List<Classes> classes) {
        Set<Integer>[] siteOfHoles = new HashSet[classes.size()];
        Set<Integer>[] siteOfReservedPlaces = new HashSet[classes.size()];
        List<Lesson>[][] randomTimeTable = new ArrayList[classes.size()][];
        int[] randomHoursPerDay = null;

        Relationships relationships = new Relationships();
        // Összes osztály lyukasóráinak előállítása
        for (int i = 0; i < classes.size(); i++) {
            randomHoursPerDay = siteOfHoleInTimeTable(classes.get(i));
            siteOfHoles[i] = calculatesiteOfFreePeriod(randomHoursPerDay, classes.get(i));

            siteOfReservedPlaces[i] = new HashSet<>(siteOfHoles[i]);
        }

        // a letárolt kapcsolatokon végigmegyünk és mindegyik évfolyam órának meghatározzuk a helyeit
        Iterator iterator = relationships.getClassesOfGradeLessons().entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry dataOfGradeLesson = (Map.Entry) iterator.next();
            String code = (String) dataOfGradeLesson.getKey();
            dataOfGradeLesson.getValue();
            List<Integer> gradeClassList =  (ArrayList) dataOfGradeLesson.getValue();
            int hourPerWeek = Relationships.gradeLessonPerWeek.get(code);
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
        // előállítjuk az osztályok véletlen órarendjét
        for (int i = 0; i < classes.size(); i++) {
            List<Lesson> clonedLessons = new ArrayList<>(classes.get(i).getAllLessons());
            Collections.shuffle(clonedLessons);

            randomTimeTable[i] = lessonsInTimeTable(clonedLessons, classes.get(i).getGradeLessons(), siteOfHoles[i], classes.get(i).getLessonsPerWeek(), relationships);

            //Tesztelés
            // printTimeTable(randomTimeTable[i], siteOfHoles[i], randomHoursPerDay, classes.get(i));
        }


        return randomTimeTable;
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
    private static Set<Integer> calculatesiteOfFreePeriod(int[] HoursPerDay, Classes classes) {
        //int[] siteOfHole = new int[45 - classes.getLessonsPerWeek()];
        Set<Integer> siteOfHole = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            int actualHole = HoursPerDay[i] + 1;
            siteOfHole.add(i * 9);

            while (actualHole <= 8) {
                siteOfHole.add(i * 9 + actualHole);

                System.out.println((i+1) + ". nap: " + HoursPerDay[i] + "db óra van -> " + (i * 9 + actualHole));
                actualHole++;
            }
        }
        return siteOfHole;
    }

    private static List<Lesson>[] lessonsInTimeTable(List<Lesson> lessons, List<Lesson> gradeLessons, Set siteOfFreePeriod, int lessonsPerWeek, Relationships relationships) {
        //Lesson[] classLessons = new Lesson[45];
        List<Lesson>[] classLessons = new ArrayList[Constants.MAXIMUM_HOURS_PER_WEEK];


        // Lyukasórák behelyezése

        Iterator iterator = siteOfFreePeriod.iterator();
        while (iterator.hasNext()) {
            int hole = (int) iterator.next();
            int valueOfFreeness = calculateValueOfFreeness(hole);
            List<Lesson> holes = new ArrayList<>();
            holes.add(new Lesson("/000", "Free Period", "none", valueOfFreeness));
            classLessons[hole] = holes;

        }

        // évfolyam órák behelyezése

        for (int i = 0; i < gradeLessons.size(); i++) {

            String code = gradeLessons.get(i).getGroupID().substring(0, 2);
            for (int j = 0; j < relationships.getPlaceOfGradeLessons().get(code).size(); j++) {
                List<Lesson> subject = new ArrayList<>();
                int place = relationships.getPlaceOfGradeLessons().get(code).get(j);
                subject.add(gradeLessons.get(i));
                if (classLessons[place] == null){
                    classLessons[place] = subject;
                } else {
                    classLessons[place].addAll(subject);
                }


            }


        }

        //Tantárgyak behelyezése
        int indexOflessons = 0;
        Set<Integer> indexes = new HashSet<>();

        for (int i = 0; i < classLessons.length; i++) {
            if (classLessons[i] == null) {
                List<Lesson> subject = new ArrayList<>();
                //Set<Integer> indexes = new HashSet<>();
                if (lessons.get(indexOflessons).getGroupID().equals("000") && !indexes.contains(indexOflessons)) {

                    subject.add(lessons.get(indexOflessons));
                    indexes.add(indexOflessons);


                } else if (lessons.get(indexOflessons).getGroupID().startsWith("0")) {
                    //subject.add(lessons.get(indexOflessons));
                    int tempIndexOfLessons = indexOflessons;
                    while (tempIndexOfLessons < lessons.size()) {
                        if (!indexes.contains(tempIndexOfLessons)) {
                            subject.add(lessons.get(tempIndexOfLessons));
                            indexes.add(tempIndexOfLessons);


                            for (int j = indexOflessons; j < lessons.size(); j++) {
                                if (!indexes.contains(j) && lessons.get(tempIndexOfLessons).getGroupID().charAt(1) == lessons.get(j).getGroupID().charAt(1) && lessons.get(j).getGroupID().charAt(2) != lessons.get(tempIndexOfLessons).getGroupID().charAt(2)) {
                                    subject.add(lessons.get(j));
                                    indexes.add(j);
                                    subject.sort((p1, p2) -> p1.getGroupID().compareTo(p2.getGroupID()));
                                    break;

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
        //System.out.println(indexes);
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

    private static void printTimeTable(List<Lesson>[] classLessons, Set<Integer> siteOfFreePeriod, int[] randomHoursPerDay, Classes classes) {
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
