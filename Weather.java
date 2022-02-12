import java.util.Random;
/**
 * The weather super class is used to simulate different weather conditions
 * of the field.
 *
 * @author Abbas BinVakas and Mohamed Shazeen Shaheen Nazeer
 * K21086651 and K21013731
 * @version 12/02/2022
 */
public abstract class Weather
{
    // The probability that that it will rain.
    private static final double RAIN_PROBABILITY = 0.3;
    // The probability that that it will snow.
    private static final double SNOW_PROBABILITY = 0.2;
    // The probability that that it will be sunny.
    private static final double SUNNY_PROBABILITY = 1 - SNOW_PROBABILITY + RAIN_PROBABILITY;
    // A random number generator.
    protected static final Random rand = Randomizer.getRandom();

    /**
     * @return The name of the weather.
     */
    abstract public String returnWeatherName();

    /**
     * Determines the weather using the probabilities.
     * @return The weather
     */
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