package schedule;

import schedule.data.Classes;
import schedule.data.DataScan;
import schedule.displayandtxtanddatabase.RoomArrangementTxtMaker;
import schedule.displayandtxtanddatabase.TimetableDataInput;
import schedule.displayandtxtanddatabase.TimetableDisplay;

import java.util.List;

//FELADATOK

//Simon:
// population.getFitness(0) a nyertes timetable, amivel tovább kell dolgozni a teremrendező algoritmusnál
// Tudni kell az eredményt elmenteni (egyenlőre txt-be)
// - megvan, egy kicsit kellett módosítanom a maint hozzá
// mert mindent static-be akart tenni és nem működött az én részem így, Roland tudja miről van szó, hozzáadtam egy kis run metódust, hogy ne legyen static minden - Simon

// Tudni kell az elmentett órarendet visszaalakítani és Lesson[][] tipusú timeTable-t létrehozni belőle.
// Lesson[][] értelmezése: Lesson[sor: osztályok 9A-12b][oszlop: idősávok (naponta 9 időpont * 5 nap = 45 idősáv)]
// - megvan - Simon
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
 * - Simon ötlete: teacher competency, ének zene tanár ne tarthasson fizika órát, minden tanárnak legyen egy kompetencia array-e
 *   ötlet: talán külön classben és ez a class lenne pédányositva a Teacher calss-en belül, ugyanugy, mint a Techer a Lessonban jelenleg
 *   csak a competencinek megfeleő órákat tarthasson egy tanár


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
        System.out.println();
        //Timetable display, database connection and txt maker - Simon
        TimetableDataInput timetableDataInput = TimetableDataInput.getInstance();
        //transmits the necessary data for the display of the timetable, to the databases and the txtmakers - Simon
        timetableDataInput.getTimetableData(dataScan.getAllClasses(), population.getFittest(0));
        //displays the timetable - Simon
        TimetableDisplay timetableDisplay = new TimetableDisplay();
        timetableDisplay.createAndDisplayTimeTable();
        //creates txt files - Simon
        RoomArrangementTxtMaker roomArrangementTxtMaker = new RoomArrangementTxtMaker();
        roomArrangementTxtMaker.txtMaker();
        System.out.println();
        //quick timetable for testing, will leave it here if you need a way to quickly display the results - Simon
        //printTimeTable(dataScan.getAllClasses(),population.getFittest(0));
        //System.out.println(population.getFittest(0).getFitness());
    }

    public static void printTimeTable(List<Classes> allClasses, Individual individual) {
        // This fast display remains here for testing.
        // The txtreader, txtmaker, database connection and proper full display can be found in
        // the displayandtxtanddatabase package. - Simon

        for (int i = 0; i < allClasses.size(); i++) {
            System.out.println("\n\nA " + allClasses.get(i).getClassName() + " osztály órarendje:");
            for (int j = 0; j < 45; j++) {
                if(j % 9 == 0) {
                    System.out.println();
                }
                System.out.print(j % 9 + ". óra: " + individual.getClassTimetable(i)[j].getNameOfLesson() + " -" + individual.getClassTimetable(i)[j].getTeacher().getName() + " ,   ");
                if (i % 9 == 8) {
                    System.out.println();
                }
            }
        }
    }
}