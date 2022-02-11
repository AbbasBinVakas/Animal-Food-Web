import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal extends Being
{
    // The age at which the animal can breed.
    private final int BREEDING_AGE;
    // The age to which an animal can live.
    private final int MAX_AGE;
    // The likelihood of an animal breeding.
    private final double BREEDING_PROBABILITY;
    // The maximum number of births of the animal.
    private final int MAX_LITTER_SIZE;
    // The probability that the animal will die when infected each step.
    protected static final double INFECTION_DEATH_PROBABILITY = 0.02;
    // The probability that the animal will recover from being infected each step.
    protected static final double INFECTION_RECOVERY_PROBABILITY = 0.02;
    // The probability that the animal will die due to the snow each step.
    protected static final double SNOWING_DEATH_PROBABILITY = 0.01;
    
    private static final double INFECTION_SPREAD_PROBABILITY = 0.35;
    // A random number generator.
    protected static final Random rand = Randomizer.getRandom();
    // The gender of the animal
    protected Gender gender;
    // The being's food level, which is increased by eating plants.
    protected int foodLevel;
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param breedingAge The age at which the animal can breed.
     * @param maxAge The age to which an animal can live.
     * @param breedingProbability The likelihood of an animal breeding.
     * @param maxLitterSize The maximum number of births of the animal.
     */
    public Animal(Field field, Location location, int breedingAge, int maxAge, double breedingProbability, int maxLitterSize)
    {
        super(field, location);
        this.gender = gender;
        BREEDING_AGE = breedingAge;
        MAX_AGE = maxAge;
        BREEDING_PROBABILITY = breedingProbability;
        MAX_LITTER_SIZE = maxLitterSize;
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals, boolean daytime, Weather weather);
    
    /**
     * Make this animal more hungry. This could result in the animal's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Increase the age of the animal.
     * This could result in the animal's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * An animal can breed if it has reached the breeding age.
     * @return true if the mouse can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Things that can happen to the animal while it is infected.
     * The animal has a chance to both die or recover, or it can
     * remain infected.
     */
    public void ifInfected()
    {
        if(infected) {
            Double randomDouble = rand.nextDouble();    
            if(randomDouble <= INFECTION_DEATH_PROBABILITY) {
                    setDead();
            }
            else if (randomDouble <= INFECTION_RECOVERY_PROBABILITY + INFECTION_DEATH_PROBABILITY && randomDouble > INFECTION_DEATH_PROBABILITY) {
                    recovered();
            }
            // Otherwise the animal will stay alive but still infected
        }
    }
    
    private void spreadInfection()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            Double randomDouble = rand.nextDouble(); 
            if(isAlive() && randomDouble <= INFECTION_SPREAD_PROBABILITY) {
                infected = true;
            }
        }
    }
}
