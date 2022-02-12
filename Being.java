/**
 * Write a description of class Beings here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Being
{
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
    protected int count;
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
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    
    protected void recovered()
    {
        infected = false;
    }
    
    public String numberInfected()
    {
        if(infected) {
            count++;
        }
        return Integer.toString(count);
    }
}
