import java.util.Random;
import java.util.List;

/**
 * Write a description of class Plant here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Plant extends Being
{
    private static final int GROWTH_HEIGHT = 2; 

    private static final int MAX_GROWTH_HEIGHT = 10;

    private static final Random rand = Randomizer.getRandom();

    private int height;

    /**
     * Constructor for objects of class Plant
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

    public void act(boolean daytime, Weather weather)
    {
        if(daytime) {
            incrementHeight();
        }
    }

    public void setEaten()
    {
        height = 0;
    }
}