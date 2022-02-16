/**
 * The Being superclass is the main class that simulates the living beings 
 * such as the plants and animals.
 *
 * Abbas BinVakas and Mohamed Shazeen Shaheen Nazeer
 * K21086651 and K21013731
 * @version 12/02/2022
 */
public class Being
{
    // The probability that the animal will become infected each step.
    protected static final double INFECTION_PROBABILITY = 0.03;
    
    // Whether the animal is alive or not.
    protected boolean alive;
    // The being's age.
    protected int age;
    // The being's field.
    protected Field field;
    // The being's position in the field.
    protected Location location;
    // Whether the being is infected with disease or not.
    protected boolean infected;
    
    /**
     * @param field The field whose status is to be displayed.
     * @param location The locations on the field.
     */
    public Being(Field field, Location location)
    {
        this.field = field;
        setLocation(location);
        alive = true;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    /**
     * Check whether the animal is alive or not.
     * @return true If the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    
    /**
     * Remove infection if the animal has recovered.
     */
    protected void recovered()
    {
        infected = false;
    }
    
    /**
     * Check whether the animal is infected or not.
     * @return true If the animal is infected.
     */
    public boolean isInfected()
    {
        return infected;
    }
    
    /**
     * Make the being infected.
     */
    public void becomeInfected()
    {
        infected = true;
    }
}
