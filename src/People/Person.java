package People;
import Others.Simulation;
import States.*;
import Others.Vector2D;

import java.util.Random;


public class Person {
    private double x;
    private double y;
    private State state;
    private int timeToSick100; // 75 = 3 sekundy = 3*25 ticków // 100 - zakażany przez osobników z objawami 100% na chorobę // jak chory to czas do resista
    private int timeToSick50; // 75 = 3 sekundy = 3*25 ticków // 50 - zakażany przez osobników bez objawów 50% na chorobę

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
        int sick100 = 75 - this.timeToSick100;
        double probability = sick100/75. * 50.;
        return (probability + 50.); // 50% - 100% na zakażenie
    }


    public Person(double x, double y, State state) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.timeToSick50 = 75;
        this.timeToSick100 = 75;
        if(state instanceof SickSymptomsState || state instanceof SickNoSymptomsState)
        {
            Random random = new Random();
            int randomSicknessTime = random.nextInt(11);
            this.setTimeToSick100((20+randomSicknessTime)*25);
        }
    }

    public Person(PersonMemento personMemento) {
        this.x = personMemento.getX();
        this.y = personMemento.getY();
        State personState;
        if (personMemento.getState() instanceof ResistantState) {
            personState = new ResistantState();
        } else if (personMemento.getState() instanceof HealthyState) {
            personState = new HealthyState();
        } else if (personMemento.getState() instanceof SickNoSymptomsState) {
            personState = new SickNoSymptomsState();
        } else {
            personState = new SickSymptomsState();
        }
        this.state = personState;
        this.timeToSick50 = personMemento.getTimeToSick50();
        this.timeToSick100 = personMemento.getTimeToSick100();
    }

    public PersonMemento memento()
    {
        State personState;
        if (this.state instanceof ResistantState) {
            personState = new ResistantState();
        } else if (state instanceof HealthyState) {
            personState = new HealthyState();
        } else if (state instanceof SickNoSymptomsState) {
            personState = new SickNoSymptomsState();
        } else {
            personState = new SickSymptomsState();
        }
        return new PersonMemento(this.x, this.y, personState, this.timeToSick50, this.timeToSick100);
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
