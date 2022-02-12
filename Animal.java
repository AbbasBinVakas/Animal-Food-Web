import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * The animal class is a subclass of the Being class.
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling and 
 * Abbas BinVakas and Mohamed Shazeen Shaheen Nazeer
 * K21086651 and K21013731
 * @version 12/02/2022
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
    protected static final double SNOWING_DEATH_PROBABILITY = 0.02;
    //The probabilty that the animal will spread the infection to other animals.
    private static final double INFECTION_SPREAD_PROBABILITY = 0.35;
    //The probabilty that the an animal will be male;
    private static final double MALE_PROBABILITY = 0.5;
    // A random number generator.
    protected static final Random rand = Randomizer.getRandom();
    
    // The being's food level, which is increased by eating plants.
    protected int foodLevel;
    protected boolean male;
    private boolean isAdjacentMale;
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
        BREEDING_AGE = breedingAge;
        MAX_AGE = maxAge;
        BREEDING_PROBABILITY = breedingProbability;
        MAX_LITTER_SIZE = maxLitterSize;
        if(rand.nextDouble() <= MALE_PROBABILITY) {
            male = true;
        }
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     * @param daytime The time of the day.
     * @param weather The weather condition.
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
        Double randomDouble = rand.nextDouble();
        if(randomDouble <= INFECTION_PROBABILITY) {
            infected = true;
        }
        if(infected) {  
            if(randomDouble <= INFECTION_DEATH_PROBABILITY) {
                setDead();
            }
            else if (randomDouble <= INFECTION_RECOVERY_PROBABILITY + INFECTION_DEATH_PROBABILITY && randomDouble > INFECTION_DEATH_PROBABILITY) {
                recovered();
            }
            // Otherwise the animal will stay alive but still infected
        }
    }
    
    /**
     * Things that can happen when an animal is infected and it comes 
     * in contact with other animals.
     * There is a chance of other animals becoming infected if it comes 
     * in contact.
     */
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

    /**
     * Checks is an animal is male.
     * @return true If the animal is a male.
     */
    public boolean isMale()
    {
        return male;
    }
}
