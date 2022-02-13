import java.util.Random;
import java.util.List;

/**
 * The plant class is a subclass of the Being class.
 * Plants can grow and be eaten by some animals.
 *
 * Abbas BinVakas and Mohamed Shazeen Shaheen Nazeer
 * K21086651 and K21013731
 * @version 12/02/2022
 */
public class Plant extends Being
{
    // The height by which plant grows by each time.
    private static final int GROWTH_HEIGHT = 2; 
    // The maximum height that the plants can grow upto.
    private static final int MAX_GROWTH_HEIGHT = 12;
    // A random number generator.
    private static final Random rand = Randomizer.getRandom();
    // How tall the plant is, hence how much can be eaten.
    private int height;

    /**
     * Create a new plant. A plant may be created with height
     * zero or with a random height.
     * 
     * @param randomHeight If true, the plant will have a random height.
     * @param field The field currently occupied.
     * @param location The locations on the field.
     */
    public Plant(boolean randomHeight, Field field, Location location)
    {
        super(field, location);
        if(randomHeight) {
            height = rand.nextInt(MAX_GROWTH_HEIGHT);
        }
        else {
            height = 0;
        }
    }

    /**
     * Used to increase the height of the plants until it 
     * reaches max height.
     */
    private void incrementHeight()
    {
        if(height < MAX_GROWTH_HEIGHT) {
            if(height + GROWTH_HEIGHT <= MAX_GROWTH_HEIGHT) {
                height+= GROWTH_HEIGHT;
            }
            else{
                height = MAX_GROWTH_HEIGHT;
            }
        }
    }

    /**
     * A plant will increase it's height every action.
     * 
     * @param daytime The time of the day.
     * @param weather The weather condition.
     */
    public void act(boolean daytime, Weather weather)
    {
        if(daytime) {
            if(weather instanceof Raining) {
                incrementHeight();
                incrementHeight();
            }
            if(weather instanceof Sunny) {
                incrementHeight();
            }
            if(weather instanceof Snowing) {
                // Do nothing
            }
        }
    }

    /**
     * What happens when the plant is eaten by an animal
     */
    public void beEaten()
    {
        height--;
    }

    /**
     * @return The height of the plant.
     */
    protected int returnHeight()
    {
        return height;
    }
}