package schedule;

import schedule.data.Classes;
import schedule.data.DataScan;
import schedule.roomarrangement.CustomHashMapForDisplayingRoomArrangement;
import schedule.roomarrangement.RoomArrangementDataInput;
import schedule.roomarrangement.RoomArrangementTxtMaker;

import java.util.List;

//FELADATOK

//Simon:
// population.getFitness(0) a nyertes timetable, amivel tovább kell dolgozni a teremrendező algoritmusnál
// Tudni kell az eredményt elmenteni (egyenlőre txt-be)
// - megvan, egy kicsit kellett módosítanom a maint hozzá,
// mert mindent static-be akart tenni és nem működött az én részem így, Roland tudja miről van szó, hozzáadtam egy kis run metódust, hogy ne legyen static minden

// Tudni kell az elmentett órarendet visszaalakítani és Lesson[][] tipusú timeTable-t létrehozni belőle.
// Lesson[][] értelmezése: Lesson[sor: osztályok 9A-12b][oszlop: idősávok (naponta 9 időpont * 5 nap = 45 idősáv)]
// - megvan, de helyette egy custom hashmap-et csináltam, mert nem úgy jöttek be az adatok, hogy lesson-t tudjak belőle csinálni,
// ahhoz a ti részeiteket is át kelett volna írni

// Ha ez megvan akkor a terembeosztás készítés lesz a feladat, de arról még beszélünk előtte
// (ehhez kell, hogy egy más kész órarenden lehessen tesztelni, amit bármikor be lehet tölteni)
//- megbeszéltük Rolanddal, de még dolgozom rajta

//+ extra feladat ami nincs itt, adatbázis kapcsolat lekutatása és prezentálása

// Péter - Roland
// Csoportbontás megvalósításán való elmélkedés, technikai megvalósítás kigondolása, majd megvalósítása
// Annak felosztása, hogy ki mit azt majd akkor, ha már jobban látjuk, hogy mit kell.

/*******************************************************************
* TOVÁBBI MEGOLDANDÓ FELADATOK MÉG A PROJEKT KAPCSÁN

*  - Óraadó tanár csak bizonyos napokon dolgozhasson, amit ráadásul előre meg lehet adni (hogy mikor szeretne)
*  - Lehessen egy tanár órarendjét csak 4 napra elosztani és így az egyik napja üres legyen.
*        (valamelyik nap továbbképzére jár egész évben, mestertanár stb)
*  - Lehessen beállítani, hogy lehessen-e nulladik óra, vagy nem.
*  - Mutációt módosítani úgy, hogy lehessen egy órá valamelyik nap végére tenni és helyére valamelyik nap végéről órát betenni
*  - Fitness függvény módosítása speciális terem ütközések figyelembevételére (2 infó terem van, de 3 infó óra egyszerre)


*  - FRONTENDET ÉPÍTENI
*  - Ha megvan a frontend akkor egy olyan felület létrehozása (is) ahol az adat bevitel megtörténik
*       // és ezek elmentése egy adatbázisba, majd a DataScan mdosítása, hogy a beolvasás az adatbázisból történjen.
*  - Jó lenne megvalósítani, hogy a kész órarendet pdf-be lehessen konvertálni a frontend felületen valamilyen táblázatos nézetben.
*  - Tesztelni igazi iskolákkal a kész verziót. (párral jó lenne kipróbálni)
*/

public class Main {
    DataScan dataScan = new DataScan();
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        // Create genetic algorithm object
        GeneticAlgorithm ga = new GeneticAlgorithm(1000, 0.5, 0.5, 0, 20);

        // Scan input data, creating teachers, lessons and classes instances
        dataScan.scanData();

        // Initialize population using scanned data
        // Evaluating fitness of individuals during construction
        Population population = ga.initPopulation(dataScan.getAllClasses());


        // Keep track of current generation
        int generation = 1;

        /**
         * Start the evolution loop
         *
         * Every genetic algorithm problem has different criteria for finishing.
         * In this case, we know what a perfect solution looks like (we don't
         * always!), so our isTerminationConditionMet method is very
         * straightforward: if there's a member of the population whose
         * chromosome is all ones, we're done!
         */
        while (ga.isTerminationConditionMet(population) == false) {
            // Print fitness of fittest individual of each generation
            System.out.println();
            System.out.println("Best solution in " + generation + " generations: ");
            System.out.println(population.getFittest(0).getFitness());

            // Apply crossover to the population
            population = ga.crossoverPopulation(population);

            // Apply mutation to the population
            population = ga.mutatePopulation(population);


            // Increment the current generation
            generation++;

            // break
            if(generation > 2000) break;
        }

        /**
         * We're out of the loop now, which means we have a perfect solution on
         * our hands. Let's print it out to confirm that it is actually all
         * ones, as promised.
         */
        System.out.println();
        System.out.println("__________________________________________________");
        System.out.println("__________________________________________________");
        System.out.println("Found solution in " + generation + " generations");
        createsTimeTableAndDisplaysItToConsole(dataScan.getAllClasses(),population.getFittest(0));
        System.out.println();
        System.out.println(population.getFittest(0).getFitness());
        printTimeTableTxt();
    }

    private static void createsTimeTableAndDisplaysItToConsole(List<Classes> allClasses, Individual individual) {
        RoomArrangementDataInput roomArrangementDataInput = RoomArrangementDataInput.getInstance();
        CustomHashMapForDisplayingRoomArrangement<Integer, String, String, Integer, String, String> timeTableCustomDisplayMap = CustomHashMapForDisplayingRoomArrangement.getInstance();
        String[] daysOfTheWeekArray = roomArrangementDataInput.getDaysOfTheWeekArray();
        int timeTableCustomDisplayMapCounter = -1;
        int daysOfTheWeekArrayCounter = -1;
        int timeRangeI = 0;

        //puts the values calculated in the for cycles below into Strings and ints so the calculation does not need to be performed twice
        // (once for roomarrangement data input, and secondly for displaying the results)
        String nameOfTheClass = null;
        int timeRange = 0;
        String subjectName = null;
        String teacherName = null;

        //timetable to console but also passes values to custom hashmap
        for (int i = 0; i < allClasses.size(); i++) {
            nameOfTheClass = allClasses.get(i).getClassName();
            roomArrangementDataInput.fillUpClassNamesStringArrayList(nameOfTheClass);

            System.out.println("\n\nA " + nameOfTheClass + " osztály órarendje:");

            for (int j = 0; j < 45; j++) {
                if(j % 9 == 9) {
                    System.out.println();
                }

                timeRange = j % 9;
                roomArrangementDataInput.fillUpTimeRangeIntegerArrayList(timeRange);
                subjectName = individual.getClassTimetable(i)[j].getNameOfLesson();
                roomArrangementDataInput.fillUpSubjectNamesStringArrayList(subjectName);
                teacherName = individual.getClassTimetable(i)[j].getTeacher().getName();
                roomArrangementDataInput.fillUpTeacherNamesStringArrayList(teacherName);

                System.out.print(timeRange + ". óra: " + subjectName + " -" + teacherName + " ,   ");

                if (i % 9 == 8) {
                    System.out.println();
                }

                //progress day of the week
                timeRangeI = i;
                daysOfTheWeekArrayCounter = roomArrangementDataInput.ChooseDayOfTheWeek(daysOfTheWeekArrayCounter, timeRangeI, timeRange);
                //make sure ++ is before the variable, not after it
                timeTableCustomDisplayMap.put(++timeTableCustomDisplayMapCounter, daysOfTheWeekArray[daysOfTheWeekArrayCounter], nameOfTheClass, timeRange, subjectName, teacherName);
            }
        }
    }

    private static void printTimeTableTxt() {
        CustomHashMapForDisplayingRoomArrangement<Integer, String, Integer, String, String, Integer> timeTableCustomDisplayMap = CustomHashMapForDisplayingRoomArrangement.getInstance();

        //you cna find the timetable in the timetable.txt
        //left it here for testing, you can use it to manually write the elements of the custom hashmap to the console
       /*
        for (int i = 0; i < timeTableCustomDisplayMap.size(); i++) {
            if (timeTableCustomDisplayMap.getAllElementsAtOnce(i) != null) {
                System.out.println(timeTableCustomDisplayMap.getAllElementsAtOnce(i));
            }
        }
         */

        RoomArrangementTxtMaker roomArrangementTxtMaker = new RoomArrangementTxtMaker();
        roomArrangementTxtMaker.txtMaker(timeTableCustomDisplayMap);
    }
}