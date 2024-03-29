import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A predator-prey simulator, based on a rectangular field
 * containing mice, goats, owls, snakes, lions,tigers and plants.
 * 
 * @author David J. Barnes and Michael Kölling and 
 * Abbas BinVakas and Mohamed Shazeen Shaheen Nazeer
 * K21086651 and K21013731
 * @version 12/02/2022
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 180;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 120;
    // The probability that a snake will be created in any given grid position.
    private static final double SNAKE_CREATION_PROBABILITY = 0.07;
    // The probability that a mouse will be created in any given grid position.
    private static final double MOUSE_CREATION_PROBABILITY = 0.15;
    // The probability that an owl will be created in any given grid position.
    private static final double OWL_CREATION_PROBABILITY = 0.12;
    // The probability that a lion will be created in any given grid position.
    private static final double LION_CREATION_PROBABILITY = 0.06;
    // The probability that a goat will be created in any given grid position.
    private static final double GOAT_CREATION_PROBABILITY = 0.12;
    // The probability that a tiger will be created in any given grid position.
    private static final double TIGER_CREATION_PROBABILITY = 0.05;
    //The probability that a plant will be created in any given grid position 
    private static final double PLANT_CREATION_PROBABILITY = 0.07;
    // How many steps daytime/not daytime takes;
    private static final int DAYTIME_LENGTH = 2;
    // How long a weather effect should last;
    private static final int WEATHER_LENGTH = 10;

    // List of animals in the field.
    private List<Animal> animals;
    // List of plants in the field.
    private List<Plant> plants;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    // Whether it is daytime or not.
    private boolean daytime;
    // Dictates what the current weather conditions are
    private Weather currentWeather;
    // Counts the total number of infected animals
    private int numberOfInfectedAnimals;
    // Counts the total number of male animals
    private int maleAnimals;
    // Counts the total number of female animals
    private int femaleAnimals;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        plants = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Mouse.class, Color.BLACK);
        view.setColor(Snake.class, Color.PINK);
        view.setColor(Owl.class, Color.GRAY);
        view.setColor(Lion.class, Color.RED);
        view.setColor(Goat.class, Color.YELLOW);
        view.setColor(Tiger.class, Color.ORANGE);
        view.setColor(Plant.class, Color.GREEN);

        currentWeather = currentWeather.chooseWeather();
        numberOfInfectedAnimals = 0;
        maleAnimals = 0;
        femaleAnimals = 0;

        // Setup a valid starting point.
        reset();
    }

    /**
     * Automatically starts the simulation and runs a set number of steps
     */
    public static void main(String[] args)
    {
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(500);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            // delay(60);   // uncomment this to run more slowly
            if (step % DAYTIME_LENGTH == 0) {
                daytime = !daytime;
            }
            if (step % WEATHER_LENGTH == 0) {
                currentWeather = Weather.chooseWeather();
            }
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * animal.
     */
    public void simulateOneStep()
    {
        step++;
        Random rand = Randomizer.getRandom();
        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();        
        // Counts the number of infected animals
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals, daytime, currentWeather);
            if(animal.isInfected()) {
                numberOfInfectedAnimals++;
            }
            if(animal.isMale()) {
                maleAnimals++;
            }
            else {
                femaleAnimals++;
            }
            if(!animal.isAlive()) {
                it.remove();
            }
        }
        for(Iterator<Plant> it = plants.iterator(); it.hasNext(); ) {
            Plant plant = it.next();
            plant.act(daytime, currentWeather);
        }

        // Add the newly born foxes and rabbits to the main lists.
        animals.addAll(newAnimals);
        
        view.showStatus(step, field, currentWeather, numberOfInfectedAnimals, maleAnimals, femaleAnimals);
        
        numberOfInfectedAnimals = 0;
        maleAnimals = 0;
        femaleAnimals = 0;
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        plants.clear();
        numberOfInfectedAnimals = 0;
        maleAnimals = 0;
        femaleAnimals = 0;
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field, currentWeather, numberOfInfectedAnimals, maleAnimals, femaleAnimals);
    }

    /**
     * Randomly populate the field with the animals.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        currentWeather = Weather.chooseWeather();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= SNAKE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Snake snake = new Snake(true, field, location);
                    animals.add(snake);
                    if(snake.isMale()) {
                        maleAnimals++;
                    }
                    else {
                        femaleAnimals++;
                    }
                }
                else if(rand.nextDouble() <= MOUSE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Mouse mouse = new Mouse(true, field, location);
                    animals.add(mouse);
                    if(mouse.isMale()) {
                        maleAnimals++;
                    }
                    else {
                        femaleAnimals++;
                    }
                }
                else if(rand.nextDouble() <= OWL_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Owl owl = new Owl(true, field, location);
                    animals.add(owl);
                    if(owl.isMale()) {
                        maleAnimals++;
                    }
                    else {
                        femaleAnimals++;
                    }
                }
                else if(rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Lion lion = new Lion(true, field, location);
                    animals.add(lion);
                    if(lion.isMale()) {
                        maleAnimals++;
                    }
                    else {
                        femaleAnimals++;
                    }
                }
                else if(rand.nextDouble() <= GOAT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Goat goat = new Goat(true, field, location);
                    animals.add(goat);
                    if(goat.isMale()) {
                        maleAnimals++;
                    }
                    else {
                        femaleAnimals++;
                    }
                }
                else if(rand.nextDouble() <= TIGER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Tiger tiger = new Tiger(true, field, location);
                    animals.add(tiger);
                    if(tiger.isMale()) {
                        maleAnimals++;
                    }
                    else {
                        femaleAnimals++;
                    }
                }
                else if(rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Plant plant = new Plant(true, field, location);
                    plants.add(plant);
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
