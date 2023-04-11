package schedule;

import com.sun.jdi.connect.Connector;
import schedule.data.Classes;
import schedule.data.Lesson;
import schedule.data.Teacher;
import schedule.data.TimeTable;

import java.util.*;


public class Individual {
    private Lesson[][] timetable;
    private double fitness = -1;
    private int numOfClasses;
    private ArrayList<Integer>[] candidatesForMutation;  //int index is ClassID

    // constructor for creating individual from timetable during algorithm process
    public Individual(Lesson[][] timetable) {
        this.timetable = timetable;
        this.numOfClasses = timetable.length;
        this.candidatesForMutation = new ArrayList[this.numOfClasses];
        for (int i = 0; i < this.numOfClasses; i++) {
            this.candidatesForMutation[i] = new ArrayList<Integer>();
        }
        this.fitness = calcFitness();
    }

    public Individual(Individual individual) {
        this.timetable = individual.getTimetable();
        this.numOfClasses = individual.getNumOfClasses();
        this.candidatesForMutation = new ArrayList[this.numOfClasses];
        for (int i = 0; i < this.numOfClasses; i++) {
            this.candidatesForMutation[i] = new ArrayList<Integer>();
        }
        this.fitness = individual.calcFitness();
    }


    // constructor for initializing firts generation individuals
    public Individual(List<Classes> allClasses) {
        Lesson[][] tempTimeTable = new Lesson[allClasses.size()][];
        for (int i = 0; i < allClasses.size(); i++) {
            tempTimeTable[i] = TimeTable.createRandomTimeTable(allClasses.get(i));
        }
        this.timetable = tempTimeTable;
        this.numOfClasses = allClasses.size();
        this.candidatesForMutation = new ArrayList[numOfClasses];
        for (int i = 0; i < numOfClasses; i++) {
            this.candidatesForMutation[i] = new ArrayList<Integer>();
        }
        this.fitness = calcFitness();
    }

    public static Individual breedOffspring(Individual parent1, Individual parent2) {
        Lesson[][] offspringTimeTable = new Lesson[parent1.getNumOfClasses()][45];

        // Loop over genome for all the offspring candidates
        for (int classIndex = 0; classIndex < parent1.getNumOfClasses(); classIndex++) {
            // Use half of parent1's genes and half of parent2's genes
            if (0.5 > Math.random()) {
                offspringTimeTable[classIndex] = parent1.getClassTimetable(classIndex);
            } else {
                offspringTimeTable[classIndex] = parent2.getClassTimetable(classIndex);
            }
        }
        Individual offspring = new Individual(offspringTimeTable);
        return offspring;
    }

    // get number of classes
    public int getNumOfClasses() {
        return numOfClasses;
    }

    // get Timetable
    public Lesson[][] getTimetable() {
        Lesson[][] newTimeTable = new Lesson[this.numOfClasses][45];
        for (int i = 0; i < this.numOfClasses; i++) {
            for (int j = 0; j < 45; j++) {
                newTimeTable[i][j]=this.timetable[i][j];
            }
        }
        return newTimeTable;
    }

    // get fitness
    public double getFitness() {
        return this.fitness;
    }

    // set fitness manually
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    // calculate fitness and select candidates for mutation
    public double calcFitness() {
        for (ArrayList<Integer> studentClass: candidatesForMutation) {
            studentClass.clear();
        }
        int clashes = 0;
        // clashes for same teacher in same timeslot
        Set<String> set = new HashSet<>();
        for (int i = 0; i < timetable[0].length; i++) {
            for (int j = 0; j < timetable.length; j++) {
                if (timetable[j][i].getValueOfFreeness() == 0 && !set.add(timetable[j][i].getTeacher().getName())) {
                    candidatesForMutation[j].add(i);
                    clashes++;
                }
            }
            set.clear();
        }
      // clashes for same lesson in same day
        Set<String> set2 = new HashSet<>();
        for (int i = 0; i < timetable.length; i++) {
            for (int j = 0; j < timetable[i].length; j++) {
                if (j % 9 == 0) {
                    set2.clear();
                }
                if (timetable[i][j].getValueOfFreeness() == 0 && !set2.add(timetable[i][j].getNameOfLesson())) {
                    candidatesForMutation[i].add(j);
                    clashes++;
                }
            }
        }
        double calculatedFitness = (double) 1 / (double) (1 + clashes);
        setFitness(calculatedFitness);
        return calculatedFitness;
    }

    private void setLesson(Lesson lesson, int classID, int dayHour) {
        timetable[classID][dayHour] = lesson;
    }

    private Lesson getLesson(int classID, int dayHour) {
        return timetable[classID][dayHour];
    }

    public void mutateTwoCollisions(int classID) {
        // how many collisions in a class timetable
        int collNum = candidatesForMutation[classID].size();
        if (collNum >= 2) {
            // choose two random indexes, and get Lesson day and hour location indexes from them
            int i = (int) Math.floor(Math.random() * collNum);
            int j;
            do {
                j = (int) Math.floor(Math.random() * collNum);
            } while (i == j);
            int dayHour1 = candidatesForMutation[classID].get(i);
            int dayHour2 = candidatesForMutation[classID].get(j);

            // swap these lessons, if they are not the same
            if (!timetable[classID][dayHour1].equals(timetable[classID][dayHour2])) {
                Lesson temp = timetable[classID][dayHour2];
                timetable[classID][dayHour2] = timetable[classID][dayHour1];
                timetable[classID][dayHour1] = temp;
            }
        }
    }

    public void mutateOneCollision(int classID) {
        // How many collisions in a class timetable
        int collNum = candidatesForMutation[classID].size();
        if (collNum >= 1) {
            // choose two random indexes, and get Lesson day and hour location indexes from them
            int i = (int) Math.floor(Math.random() * collNum);
            int dayHour1 = candidatesForMutation[classID].get(i);
            int dayHour2 = (int) Math.floor(Math.random() * timetable[classID].length);

            // swap these lessons, if they are not the same
            if (!timetable[classID][dayHour1].equals(timetable[classID][dayHour2]) && timetable[classID][dayHour2].getValueOfFreeness() == 0) {
                Lesson temp = timetable[classID][dayHour2];
                timetable[classID][dayHour2] = timetable[classID][dayHour1];
                timetable[classID][dayHour1] = temp;
            }
        }
    }

    public void mutateRandom(int classID) {
        // choose two random indexes, and get Lesson day and hour location indexes from them
        int dayHour1 = (int) Math.floor(Math.random() * timetable[classID].length);
        int dayHour2 = (int) Math.floor(Math.random() * timetable[classID].length);

        // swap these lessons, if they are not the same
        if (!timetable[classID][dayHour1].equals(timetable[classID][dayHour2])
                && timetable[classID][dayHour1].getValueOfFreeness() == 0
                && timetable[classID][dayHour2].getValueOfFreeness() == 0) {
            Lesson temp = timetable[classID][dayHour2];
            timetable[classID][dayHour2] = timetable[classID][dayHour1];
            timetable[classID][dayHour1] = temp;
        }
    }

    public Lesson[] getClassTimetable(int classID) {
        return this.timetable[classID];
    }

    public void setClassTimetable(int classID, Lesson[] classTimeTable) {
        this.timetable[classID] = classTimeTable;
    }


}






