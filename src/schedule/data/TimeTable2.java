package schedule.data;

import java.util.*;

public class TimeTable2 {

    public static List<Lesson>[] createRandomTimeTable(Classes classes) {

        List<Lesson> clonedLessons = new ArrayList<>(classes.getAllLessons());
        Collections.shuffle(clonedLessons);

        int[] randomHoursPerDay = siteOfHoleInTimeTable(classes);
        int[] siteOfFreePeriod = calculatesiteOfFreePeriod(randomHoursPerDay, classes);
        List<Lesson>[] classLessons = lessonsInTimeTable(clonedLessons, siteOfFreePeriod, classes.getLessonsPerWeek());

        //Tesztelés
        printTimeTable(classLessons, siteOfFreePeriod, randomHoursPerDay, classes);
        return classLessons;
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
    private static int[] calculatesiteOfFreePeriod(int[] HoursPerDay, Classes classes) {
        int[] siteOfHole = new int[45 - classes.getLessonsPerWeek()];

        int indexOfHole = 0;
        for (int i = 0; i < 5; i++) {
            int actualHole = HoursPerDay[i] + 1;
            siteOfHole[indexOfHole] = i * 9;
            indexOfHole++;
            while (actualHole <= 8) {
                siteOfHole[indexOfHole] = i * 9 + actualHole;
                actualHole++;
                indexOfHole++;
            }
        }
        return siteOfHole;
    }

    private static List<Lesson>[] lessonsInTimeTable(List<Lesson> lessons, int[] siteOfFreePeriod, int lessonsPerWeek) {
        //Lesson[] classLessons = new Lesson[45];
        List<Lesson>[] classLessons = new ArrayList[45];
        // Lyukasórák behelyezése
        for (int i = 0; i < classLessons.length; i++) {
            for (int j = 0; j < siteOfFreePeriod.length; j++) {
                if (i == siteOfFreePeriod[j]) {
                    int valueOfFreeness = calculateValueOfFreeness(i);
                    List<Lesson> holes = new ArrayList<>();
                    holes.add(new Lesson("/000", "Free Period", "none", valueOfFreeness));
                    classLessons[i] = holes;
                }
            }
        }
        //Tantárgyak behelyezése
        int indexOflessons = 0;
        for (int i = 0; i < classLessons.length; i++) {
            if (classLessons[i] == null) {
                List<Lesson> subject = new ArrayList<>();
                Set<Integer> indexes = new HashSet<>();
                if (lessons.get(indexOflessons).getGroupID().equals("000")) {

                    subject.add(lessons.get(indexOflessons));

                } else if (lessons.get(indexOflessons).getGroupID().startsWith("0")) {
                    //subject.add(lessons.get(0));
                    if (indexes.add(indexOflessons)) {
                        outer:
                        for (int k = 1; k < 3; k++) {
                            inner:
                            for (int j = indexOflessons; j < lessons.size(); j++) {
                                if (lessons.get(indexOflessons).getGroupID().charAt(1) == lessons.get(j).getGroupID().charAt(1) && lessons.get(j).getGroupID().charAt(2) == Character.forDigit(k, 10)) {
                                    subject.add(lessons.get(j));
                                    indexes.add(indexOflessons);
                                    break inner;
                                }
                            }
                        }
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

    private static void printTimeTable(List<Lesson>[] classLessons, int[] siteOfFreePeriod, int[] randomHoursPerDay, Classes classes) {
        // Tesztelés
        System.out.println();
        System.out.println("Melyik nap hány órája van az osztálynak");
        System.out.print(Arrays.toString(randomHoursPerDay));
        System.out.println();
        System.out.println("Lyukas órák helye");
        System.out.println(Arrays.toString(siteOfFreePeriod));
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
            for (int j = 0; j < classLessons[i].size();j++){
                System.out.print(classLessons[i].get(j).getGroupID() + " " + classLessons[i].get(j).getNameOfLesson() + "    ");
            }
            System.out.println();


        }
    }
}
