package People;
import Others.Simulation;
import States.State;
import Others.Vector2D;

import java.util.Random;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class Person {
    private double x;
    private double y;
    private State state;
    private int timeToSick100; // 75 = 3 sekundy = 3*25 ticków // 100 - zakażany przez osobników z objawami 100% na chorobę
    private int timeToSick50; // 75 = 3 sekundy = 3*25 ticków // 50 - zakażany przez osobników bez objawów 50% na chorobę

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setState(State state) {
        this.state = state;
    }
    public void setTimeToSick100(int timeToSick100) { this.timeToSick100 = timeToSick100; }
    public void setTimeToSick50(int timeToSick50) { this.timeToSick50 = timeToSick50; }
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public State getState() { return this.state; }
    public int getTimeToSick100() { return this.timeToSick100; }
    public int getTimeToSick50() { return this.timeToSick50; }
    public int getTimeToSick() { return (this.timeToSick50 + this.timeToSick100 - 75); }
    public double getSicknessProbability() { // zwraca 0-50 punktów procentowych
        int sick50 = 75 - this.timeToSick50;
        int sick100 = 75 - this.timeToSick100;
        double probability = sick100/75. * 50;
        return probability + 50; // 50% - 100% na zakażenie
    }


    public Person(double x, double y, State state) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.timeToSick50 = 75;
        this.timeToSick100 = 75;
    }
    public PersonMemento memento()
    {
        return new PersonMemento(this.x, this.y, this.state, this.timeToSick50, this.timeToSick100);
    }

    public boolean move(Vector2D vector) //True if alive
    {
        this.x = this.x + vector.getComponents()[0];
        this.y = this.y + vector.getComponents()[1];
        if(x<0. || x> Simulation.n || y<0. || y> Simulation.m)
        {
            Random random = new Random();
            int randomExit = random.nextInt(2 - 1 + 1) + 1;
            if (randomExit == 1)
            {
                this.x = this.x - vector.getComponents()[0];
                this.y = this.y - vector.getComponents()[1];
                return true;
            }
            else
            {
                return false;
            }
        }
        return true;
    }
}
