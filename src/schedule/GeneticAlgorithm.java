package schedule;

import schedule.data.Classes;
import schedule.data.Lesson;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The GeneticAlgorithm class is our main abstraction for managing the
 * operations of the genetic algorithm. This class is meant to be
 * problem-specific, meaning that (for instance) the "calcFitness" method may
 * need to change from problem to problem.
 * <p>
 * This class concerns itself mostly with population-level operations, but also
 * problem-specific operations such as calculating fitness, testing for
 * termination criteria, and managing mutation and crossover operations (which
 * generally need to be problem-specific as well).
 * <p>
 * Generally, GeneticAlgorithm might be better suited as an abstract class or an
 * interface, rather than a concrete class as below. A GeneticAlgorithm
 * interface would require implementation of methods such as
 * "isTerminationConditionMet", "calcFitness", "mutatePopulation", etc, and a
 * concrete class would be defined to solve a particular problem domain. For
 * instance, the concrete class "TravelingSalesmanGeneticAlgorithm" would
 * implement the "GeneticAlgorithm" interface. This is not the approach we've
 * chosen, however, so that we can keep each chapter's examples as simple and
 * concrete as possible.
 *
 * @author bkanber
 */

public class GeneticAlgorithm {

    protected int tournamentSize;
    private int populationSize;
    /**
     * Mutation rate is the fractional probability than an individual gene will
     * mutate randomly in a given generation. The range is 0.0-1.0, but is
     * generally small (on the order of 0.1 or less).
     */
    private double mutationRate;
    /**
     * Crossover rate is the fractional probability that two individuals will
     * "mate" with each other, sharing genetic information, and creating
     * offspring with traits of each of the parents. Like mutation rate the
     * rance is 0.0-1.0 but small.
     */
    private double crossoverRate;
    /**
     * Elitism is the concept that the strongest members of the population
     * should be preserved from generation to generation. If an individual is
     * one of the elite, it will not be mutated or crossover.
     */
    private int elitismCount;


    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;

    }

    /**
     * Initialize population
     *
     * @param allTheClasses The classes arrayList from input data
     * @return population The initial population generated
     */
    public Population initPopulation(List<Classes> allTheClasses) {
        // Initialize population
        Population population = new Population(this.populationSize, allTheClasses);
        return population;
    }


    /**
     * Check if population has met termination condition
     *
     * Hoping for perfect result, termination condition is a fitness value of 1.0
     *
     * @param population
     * @return boolean True if termination condition met, otherwise, false
     */
    public boolean isTerminationConditionMet(Population population) {
        for (Individual individual : population.getIndividuals()) {
            if (individual.getFitness() == 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Selects parent for crossover using tournament selection
     * <p>
     * Tournament selection works by choosing N random individuals, and then
     * choosing the best of those.
     *
     * @param population
     * @return The individual selected as a parent
     */
    public Individual selectParent(Population population) {
        // Create tournament
        Population tournament = new Population(this.tournamentSize);

        // Add random individuals to the tournament
        population.shuffle();
        for (int i = 0; i < this.tournamentSize; i++) {
            Individual tournamentIndividual = population.getIndividual(i);
            tournament.setIndividual(i, tournamentIndividual);
        }

        // Return the best
        return tournament.getFittest(0);
    }

    /**
     * Apply crossover to population
     *
     * Crossover, more colloquially considered "mating", takes the population
     * and blends individuals to create new offspring. Offspring may have better fittnes than its parents.
     * It's up to testing to decide wether random crossover or guided crossover is better
     *
     * This method considers both the GeneticAlgorithm instance's crossoverRate
     * and the elitismCount.
     *
     * @param population The population to apply crossover to
     * @return The new population
     */
    public Population crossoverPopulation(Population population) {
        // Create new population
        Population newPopulation = new Population(population.size());

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getFittest(populationIndex);

           // Apply crossover to this individual?
            if (this.crossoverRate > Math.random() && populationIndex > this.elitismCount) {

                // Find second parent
                Individual parent2 = selectParent(population);

                // Initialize 10 offsprings. Validity of number 10 is up to more testing
                Population offspringsPop = new Population(10);
                for (int i = 0; i < 10; i++) {
                    Individual offspring = Individual.breedOffspring(parent1, parent2);
                    offspring.setFitness(offspring.calcFitness());
                    offspringsPop.setIndividual(i, offspring);
                }
                // get fittest offspring
               Individual fittestOffspring = offspringsPop.getFittest(0);
                // if offspring is fitter than parent
                if(fittestOffspring.getFitness() > parent1.getFitness()) {
                    // Add offspring to new population
                    newPopulation.setIndividual(populationIndex, fittestOffspring);
                }
                // if not fitter, parent remains
                else {
                    newPopulation.setIndividual(populationIndex, parent1);
                }

            } else {
                // Add individual to new population without applying crossover
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }

        return newPopulation;
    }

    /**
     * Apply mutation to population
     * <p>
     * Mutation affects individuals rather than the population. We look at each
     * individual in the population, and if they're lucky enough,
     * apply some randomness to their chromosome by swapping classes.
     * This method will consider the GeneticAlgorithm instance's mutationRate
     * and elitismCount
     *
     * @param population The population to apply mutation to
     * @return The mutated population
     */
    public Population mutatePopulation(Population population) {
        // Initialize new population
        Population newPopulation = new Population(this.populationSize);
        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            // choose and individual and clone it
            Individual individualOriginal = population.getFittest(populationIndex);
            Individual individual = new Individual(individualOriginal);

            // apply mutation to the clone
            if (populationIndex > this.elitismCount) {
            for (int classIndex = 0; classIndex < individual.getNumOfClasses(); classIndex++) {
                    // Does this class timetable need mutation?
                    if (this.mutationRate > Math.random()) {
                        // Get two colliding classes swap
                        individual.mutateTwoCollisions(classIndex);
                    }
                    if (this.mutationRate > Math.random()) {
                        // Get one colliding class swap with a random
                        individual.mutateOneCollision(classIndex);
                    }
                    if (this.mutationRate > Math.random()*2) {
                        // Get two random classes swap
                        individual.mutateRandom(classIndex);
                    }
                }
            // if mutated clone is fitter, swap it with the original
                if (individual.getFitness() >= individualOriginal.getFitness()) {
                    // Add individual to population
                    newPopulation.setIndividual(populationIndex, individual);
                } else {
                    // if not fitter, original remains
                    newPopulation.setIndividual(populationIndex, individualOriginal);
                }
            }
             else {
                 // if decided to not mutate, original remains
                newPopulation.setIndividual(populationIndex, individualOriginal);
            }
        }
        // Return population after mutation process
        return newPopulation;
    }
}