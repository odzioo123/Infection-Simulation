package People;
import People.Person;

import java.util.HashMap;
import java.util.Map;

public class PersonCareTaker {
    private Person person;
    private Map<Integer, PersonMemento> mementoMap;
    PersonCareTaker(Person person) {
        mementoMap = new HashMap<>();
    }

    public void save(Integer t)
    {
        mementoMap.put(t, person.memento());
    }

    public boolean load(Integer second)
    {
        if (mementoMap.containsKey(second)) {
            PersonMemento snapshot = mementoMap.get(second);
            person.setX(snapshot.x);
            person.setY(snapshot.y);
            person.setState(snapshot.state);
            return true;
        }
        else{
            return false;
        }
    }

}
