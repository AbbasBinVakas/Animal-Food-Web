import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a snake.
 * Snakes age, move, eat mice, and die.
 * 
 * Abbas BinVakas and Mohamed Shazeen Shaheen Nazeer
 * K21086651 and K21013731
 * @version 12/02/2022
 */
public class Snake extends Animal
{
    // Characteristics shared by all snakes (class variables).
 
    // The age at which a snake can start to breed.
    private static final int BREEDING_AGE = 7;
    // The age to which a snake can live.
    private static final int MAX_AGE = 60;
    // The likelihood of a snake breeding.
    private static final double BREEDING_PROBABILITY = 0.19;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single snake. In effect, this is the
    // number of steps a snake can go before it has to eat again.
    private static final int FOOD_VALUE = 47;

    /**
     * Create a snake. A snake can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the snake will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Snake(boolean randomAge, Field field, Location location)
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
     * This is what the snake does most of the time: it hunts for
     * mice. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newYoung A list to return newly born snakes.
     * @param daytime If true, time of day is daytime.
     * @param weather The weather condition.
     */
    public void act(List<Animal> newYoung, boolean daytime, Weather weather)
    {
        if(weather instanceof Sunny) { // what the animal does while it is sunny
            incrementAge();
            incrementHunger();
            if(isAlive() && daytime) {
                giveBirth(newYoung);            
                // Move towards a source of food if found.
                Location newLocation = findFood();
                if(newLocation == null) { 
                    // No food found - try to move to a free location.
                    newLocation = getField().freeAdjacentLocation(getLocation());
                }
                // See if it was possible to move.
                if(newLocation != null) {
                    setLocation(newLocation);
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
                // Move towards a source of food if found.
                Location newLocation = findFood();
                if(newLocation == null) { 
                    // No food found - try to move to a free location.
                    newLocation = getField().freeAdjacentLocation(getLocation());
                }
                // See if it was possible to move.
                if(newLocation != null) {
                    setLocation(newLocation);
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
     * Look for mice adjacent to the current location.
     * Only the first live mice is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Mouse) {
                Mouse mouse = (Mouse) animal;
                if(mouse.isAlive()) { 
                    mouse.setDead();
                    foodLevel = FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this snake is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newSnakes A list to return newly born snakes.
     */
    private void giveBirth(List<Animal> newSnakes)
    {
        // New snakes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Snake young = new Snake(false, field, loc);
            newSnakes.add(young);
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
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
                Location where = it.next();
                Object Being = field.getObjectAt(where);
                if(Being instanceof Snake) {
                    Snake snake = (Snake) Being;
                    if(male && !snake.isMale()) {
                        births = rand.nextInt(MAX_LITTER_SIZE) + 1;
                    }
                    else if(!male && snake.isMale()) {
                        births = rand.nextInt(MAX_LITTER_SIZE) + 1;
                    }
                }
            }
        }
        return births;
    }
}