package People;
import States.State;


public class PersonMemento {
    private double x;
    private double y;
    private State state;
    private int timeToSick100;
    private int timeToSick50;

    public PersonMemento(double x, double y, State state, int timeToSick50, int timeToSick100) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.timeToSick50 = timeToSick50;
        this.timeToSick100 = timeToSick100;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public State getState() {
        return state;
    }

    public int getTimeToSick100() {
        return timeToSick100;
    }

    public int getTimeToSick50() {
        return timeToSick50;
    }
}





