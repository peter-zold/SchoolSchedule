package schedule.version2;

import schedule.version1.data.Classes;
import schedule.version2.data.DataScan2;
import schedule.version1.Individual;

import java.util.List;

//import schedule.version1.displayandtxtanddatabase.RoomArrangementTxtMaker;
//import schedule.version1.displayandtxtanddatabase.TimetableDataInput;

//próba
//FELADATOK

//Simon ----------------------------------------------
// Task1: population.getFitness(0) is the fittest timetable create a method the saves it as a txt file
// - done, had to slightly modify the Main class, because everything was unnecessarily set to static and it didn't work,
// Roland knows about this, I madea tiny run() method to circumvent the static tag introduced by the main method - Simon
//
// Task2: display the fittest timetable (see above) as a 2 dimensional Lesson[][] array, and make the creation of the lesson array dynamic
// and make it availabe through a getter method
// - done, Simon
//
//Task3: Create a database connection, implement it in the current state of the Genetic algorithm and make
// a presentation for the other team members illustrating how it works
// - doing it currently - Simon
//
// Other: I have translated all my comments into English, including this list of tasks. Additionally, I will send some optimization tips, fixes and OOP recommendation
//to Roland. Recommendations, fixes, tips I have come up with while working with the other team members' code (I won't directly change their code, will only send it to the project manager for consideration)
// as per our email discussion with the project manager.

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
 *    ötlet: talán külön classben és ez a class lenne példányosítva a Teacher calss-en belül, ugyanugy, mint a Techer a Lessonban jelenleg
 *    csak a competencinek megfelelő órákat tarthasson egy tanár


 *  - FRONTENDET ÉPÍTENI
 *  - Ha megvan a frontend akkor egy olyan felület létrehozása (is) ahol az adat bevitel megtörténik
 *       // és ezek elmentése egy adatbázisba, majd a DataScan mdosítása, hogy a beolvasás az adatbázisból történjen.
 *  - Jó lenne megvalósítani, hogy a kész órarendet pdf-be lehessen konvertálni a frontend felületen valamilyen táblázatos nézetben.
 *  - Tesztelni igazi iskolákkal a kész verziót. (párral jó lenne kipróbálni)
 */

public class Main2 {
    DataScan2 dataScan = new DataScan2();
    public static void main(String[] args) {
        new Main2().run();
    }

    private void run() {
        // Create genetic algorithm object
        GeneticAlgorithm2 ga = new GeneticAlgorithm2(2000, 0.5, 0.5, 0, 20);

        // Scan input data, creating teachers, lessons and classes instances
        dataScan.scanData();

        // Initialize population using scanned data
        // Evaluating fitness of individuals during construction
        Population2 population = ga.initPopulation(dataScan.getAllClasses());
        //printTimeTable(dataScan.getAllClasses(), population.getFittest(0));

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

            // print timetable
             printTimeTable(dataScan.getAllClasses(), population.getFittest(0));

            // break
            if (generation%50 ==0){
                printTimeTable(dataScan.getAllClasses() ,population.getFittest(0));
            }
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
        printTimeTable(dataScan.getAllClasses() ,population.getFittest(0));

        /*
        //Timetable display, database connection and txt maker - Simon
        TimetableDataInputAndOutput timetableDataInputAndOutput = TimetableDataInputAndOutput.getInstance();
        //transmits the necessary data for the display of the timetable, to the databases and the txtmakers - Simon
        timetableDataInputAndOutput.getTimetableData(dataScan.getAllClasses(), population.getFittest(0));
        //displays the timetable - Simon
        TimetableDisplay timetableDisplay = new TimetableDisplay();
        timetableDisplay.createAndDisplayTimeTable();
        //creates txt files, timetable.txt and the other txt files can be found in the displayandtxtanddatabase package - Simon
        TxtMaker txtMaker = new TxtMaker();
        txtMaker.timetableTxtMaker();
        //I created these additional txtmaker methods too, we might need them later, not currently in use
        txtMaker.subjectNamesTxtMaker();
        txtMaker.teacherNamesTxtMaker();
        txtMaker.valueOfFreenessTxtMaker();
        System.out.println();
        //This creates a new Lesson array based on the timetable created, use this to turn any timetable.txt into a Lesson array
        TimetableTxtToLessonArray timetableTxtToLessonArray = new TimetableTxtToLessonArray();
        Lesson[][] lessonArrayFromTimetable = timetableTxtToLessonArray.getLessonFromTxtFile();
        //System.out.println(lessonArrayFromTimetable[0][0]);
        //quick timetable for testing, will leave it here if you need a way to quickly display the results - Simon
        //printTimetable(dataScan.getAllClasses(),population.getFittest(0));
        //System.out.println(population.getFittest(0).getFitness());

         */
    }


    public static void printTimeTable(List<Classes> allClasses, Individual2 individual) {
        // This fast display remains here for testing.
        // The txtreader, txtmaker, database connection and proper full display can be found in
        // the displayandtxtanddatabase package. - Simon

        for (int i = 0; i < allClasses.size(); i++) {
            System.out.println("\n\nA " + allClasses.get(i).getClassName() + " osztály órarendje:");
            for (int k = 0; k < individual.getTimetable()[i].length; k++) {
                if (k % 9 == 0) {
                    System.out.println();
                }
                System.out.print(k % 9 + ". óra: ");
                for (int j = 0; j < individual.getTimetable()[i][k].size(); j++) {
                    System.out.print(individual.getTimetable()[i][k].get(j).getGroupID() + " " + individual.getTimetable()[i][k].get(j).getNameOfLesson() + "    ");
                }
                System.out.println();


            }
        }
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
