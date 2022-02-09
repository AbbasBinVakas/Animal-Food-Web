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
    private static final double SUNNY_PROBABILITY = 0.5;

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    abstract public void weatherEffect();
    
    abstract public String returnWeatherName();

    public static Weather chooseWeather() {
        Random rand = Randomizer.getRandom();
        if(rand.nextDouble() <= RAIN_PROBABILITY) {
            Raining rain = new Raining();
            return rain;
        }
        else if(rand.nextDouble() <= SNOW_PROBABILITY) {
            Snowing snow = new Snowing();
            return snow;
        }
        else {
            Sunny sunny = new Sunny();
            return sunny;
        }
    }
}