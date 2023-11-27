package States;
import States.State;
public class HealthyState implements State {
    @Override
    public String getColor() {
        return "green";
    }
}
