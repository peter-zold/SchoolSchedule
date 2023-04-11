package schedule;

import schedule.data.Classes;
import schedule.data.DataScan;
import schedule.data.Lesson;

import java.util.List;

//FELADATOK

//Simon:
// population.getFitness(0) a nyertes timetable, amivel tovább kell dolgozni a teremrendező algoritmusnál
// Tudni kell az eredményt elmenteni (egyenlőre txt-be)

// Tudni kell az elmentett órarendet visszaalakítani és Lesson[][] tipusú timeTable-t létrehozni belőle.
// Lesson[][] értelmezése: Lesson[sor: osztályok 9A-12b][oszlop: idősávok (naponta 9 időpont * 5 nap = 45 idősáv)]

// Ha ez megvan akkor a terembeosztás készítés lesz a feladat, de arról még beszélünk előtte
// (ehhez kell, hogy egy más kész órarenden lehessen tesztelni, amit bármikor be lehet tölteni)

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
    public static void main(String[] args) {
        // Create GA object
        GeneticAlgorithm ga = new GeneticAlgorithm(1000, 0.2, 0.1, 0, 20);
        // Scan data
        DataScan ds = new DataScan();
        ds.scanData();

        // Initialize population
        Population population = ga.initPopulation(ds.getAllClasses());


        // Evaluate population
        ga.evalPopulation(population);

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
            // Print fittest individual from population
            System.out.println();
            System.out.println("Best solution: ");
            //printTimeTable(ds.getAllClasses(),population.getFittest(0));
            System.out.println(population.getFittest(0).getFitness());

            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population);

            // Evaluate population
             ga.evalPopulation(population);

            // Increment the current generation
            generation++;

            // break
            if(generation > 1000) break;
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
        printTimeTable(ds.getAllClasses(),population.getFittest(0));
        System.out.println();
        System.out.println(population.getFittest(0).getFitness());
    }

    public static void printTimeTable(List<Classes> allClasses, Individual individual) {
        // Tesztelés
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