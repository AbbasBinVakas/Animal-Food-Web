import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * A simple model of a mouse.
 * Mice age, move, breed, and die.
 * 
 * Abbas BinVakas and Mohamed Shazeen Shaheen Nazeer
 * K21086651 and K21013731
 * @version 12/02/2022
 */
public class Mouse extends Animal
{
    // Characteristics shared by all mice (class variables).
 
    // The age at which a mouse can start to breed.
    private static final int BREEDING_AGE = 3;
    // The age to which a mouse can live.
    private static final int MAX_AGE = 12;
    // The likelihood of a mouse breeding.
    private static final double BREEDING_PROBABILITY = 0.36;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 10;
    // The food value of a single moues. In effect, this is the
    // number of steps a mouse can go before it has to eat again.
    private static final int FOOD_VALUE = 7;

    /**
     * Create a new mouse. A mouse may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the mouse will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Mouse(boolean randomAge, Field field, Location location)
    {
        super(field, location, BREEDING_AGE, MAX_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = FOOD_VALUE;
        }
    }

    /**
     * This is what the mouse does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newYoung A list to return newly born mice.
     * @param daytime If true, time of day is daytime.
     * @param weather The weather condition.
     */
    public void act(List<Animal> newYoung, boolean daytime, Weather weather)
    {
        if(weather instanceof Sunny) { // what the animal does while it is sunny
            incrementAge();
            incrementHunger();
            if(isAlive() && !daytime) {
                giveBirth(newYoung);            
                // See if it was possible to move.
                Location newLocation = getField().freeAdjacentLocation(getLocation());
                if(newLocation != null) {
                    findFood();
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
            ifInfected();
        }
        
        if(weather instanceof Raining) { // what the animal does while it is raining
            incrementAge();
            incrementHunger();
            if(isAlive() && !daytime) {  
                giveBirth(newYoung);
                // See if it was possible to move.
                Location newLocation = getField().freeAdjacentLocation(getLocation());
                if(newLocation != null) {
                    findFood();
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
            ifInfected();
        }
        
        if(weather instanceof Snowing) { // what the animal does while it is snowing
            if(rand.nextDouble() <= SNOWING_DEATH_PROBABILITY) {
                setDead();
            }
        }
    }

    /**
     * Look for plants adjacent to the current location.
     */
    private void findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object Being = field.getObjectAt(where);
            if(Being instanceof Plant) {
                Plant plant = (Plant) Being;
                if(plant.returnHeight() > 0) {
                    plant.beEaten();
                    foodLevel = FOOD_VALUE;
                }
            }
        }
    }

    /**
     * Check whether or not this mouse is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newMice A list to return newly born mice.
     */
    private void giveBirth(List<Animal> newMice)
    {
        // New mice are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Mouse young = new Mouse(false, field, loc);
            newMice.add(young);
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (can be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
                Location where = it.next();
                Object Being = field.getObjectAt(where);
                if(Being instanceof Mouse) {
                    Mouse mouse = (Mouse) Being;
                    if(male && !mouse.isMale()) {
                        births = rand.nextInt(MAX_LITTER_SIZE) + 1;
                    }
                    else if(!male && mouse.isMale()) {
                        births = rand.nextInt(MAX_LITTER_SIZE) + 1;
                    }
                }
            }
        }
        return births;
    }
}
