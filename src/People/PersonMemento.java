package People;
import States.*;

public class PersonMemento {
    public double x;
    public double y;
    public State state;
    public int timeToSick100;
    public int timeToSick50;
    public PersonMemento(double x, double y, State state, int timeToSick100, int timeToSick50 ) {
        this.x = x;
        this.y = y;

        if(state instanceof HealthyState) {
            this.state = new HealthyState();
        } else if (state instanceof ResistantState) {
            this.state = new ResistantState();
        } else if (state instanceof SickNoSymptomsState) {
            this.state = new SickNoSymptomsState();
        } else if (state instanceof SickSymptomsState) {
            this.state = new SickSymptomsState();
        }
        this.timeToSick100 = timeToSick100;
        this.timeToSick50 = timeToSick50;
    }
}
