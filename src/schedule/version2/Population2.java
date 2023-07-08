package schedule.version2;

import schedule.version1.data.Classes;

import java.util.*;

/**
 * A population is an abstraction of a collection of individuals. The population
 * class is generally used to perform group-level operations on its individuals,
 * such as finding the strongest individuals, collecting stats on the population
 * as a whole, and selecting individuals to mutate or crossover.
 *
 * @author bkanber
 *
 */

public class Population2 {
    private Individual2[] population;
    private double populationFitness = -1;

    /**
     * Initializes blank population of individuals
     *
     * @param populationSize
     *            The number of individuals in the population
     */
    public Population2(int populationSize) {
        // Initial population
        this.population = new Individual2[populationSize];
    }

    /**
     * Initializes population of individuals
     *
     * @param populationSize
     *            The number of individuals in the population
     * @param allTheClasses
     *            classes from input data
     */
    public Population2(int populationSize, List<Classes> allTheClasses) {
        // Initialize the population as an array of individuals
        this.population = new Individual2[populationSize];

        // Create each individual in turn
        for (int individualCount = 0; individualCount < populationSize; individualCount++) {
            // Create an individual, initializing its chromosome accordint to input data
            Individual2 individual = new Individual2(allTheClasses);
            // Add individual to population
            this.population[individualCount] = individual;
        }
    }

    /**
     * Get individuals from the population
     *
     * @return individuals in population
     */
    public Individual2[] getIndividuals() {
        return this.population;
    }

    /**
     * Find an individual in the population by its fitness
     *
     * This method lets you select an individual in order of its fitness. This
     * can be used to find the single strongest individual (eg, if you're
     * testing for a solution), but it can also be used to find weak individuals
     * (if you're looking to cull the population) or some of the strongest
     * individuals (if you're using "elitism").
     *
     * @param offset
     *            The offset of the individual you want, sorted by fitness. 0 is
     *            the strongest, population.length - 1 is the weakest.
     * @return individual Individual at offset
     */
    public Individual2 getFittest(int offset) {
        // Order population by fitness
        Arrays.sort(this.population, new Comparator<Individual2>() {
            @Override
            public int compare(Individual2 o1, Individual2 o2) {
                if (o1.getFitness() > o2.getFitness()) {
                    return -1;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return 1;
                }
                return 0;
            }
        });

        // Return the fittest individual
        return this.population[offset];
    }

    /**
     * Get population's group fitness
     *
     * @return populationFitness The population's total fitness
     */
    public double getPopulationFitness() {
        return this.populationFitness;
    }

    /**
     * Calculate and set population's fitness
     * No use at the moment, reserving for future use
     *
     * @return size The population's size
     */
    public void calcPopulationFitness() {
        double popFitness = 0;
        for (Individual2 individual: this.population) {
            popFitness += individual.calcFitness();
        }
        this.populationFitness = popFitness;
    }

    /**
     * Get population's size
     *
     * @return size The population's size
     */

    public int size() {
        return this.population.length;
    }

    /**
     * Set individual at offset
     *
     * @param individual
     * @param offset
     * @return individual
     */
    public Individual2 setIndividual(int offset, Individual2 individual) {
        return population[offset] = individual;
    }

    /**
     * Get individual at offset
     *
     * @param offset
     * @return individual
     */
    public Individual2 getIndividual(int offset) {
        return population[offset];
    }

    /**
     * Shuffles the population in-place
     *
     * @param "void"
     * @return void
     */
    public void shuffle() {
        Random rnd = new Random();
        for (int i = population.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Individual2 a = population[index];
            population[index] = population[i];
            population[i] = a;
        }
    }
}
