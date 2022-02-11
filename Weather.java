import java.util.Random;
/**
 * Write a description of class Weather here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Weather
{
    private static final double RAIN_PROBABILITY = 0.3;
    private static final double SNOW_PROBABILITY = 0.2;
    private static final double SUNNY_PROBABILITY = 1 - SNOW_PROBABILITY + RAIN_PROBABILITY;
    // A random number generator.
    protected static final Random rand = Randomizer.getRandom();

    /**
     * Returns the name of the weather.
     */
    abstract public String returnWeatherName();

    public static Weather chooseWeather() {
        Double randomDouble = rand.nextDouble();
        //Weather newWeather = new Weather();
        if(randomDouble <= RAIN_PROBABILITY) {
            Weather newWeather = new Raining();
            return newWeather;
        }
        else if(randomDouble <= SNOW_PROBABILITY + RAIN_PROBABILITY && randomDouble > RAIN_PROBABILITY) {
            Weather newWeather = new Snowing();
            return newWeather;
        }
        // else if(randomDouble <= SNOW_PROBABILITY + RAIN_PROBABILITY + SUNNY_PROBABILITY && randomDouble > SNOW_PROBABILITY + RAIN_PROBABILITY) {
        else {
            Weather newWeather = new Sunny();
            return newWeather;
        }
        //return newWeather;
    }
}