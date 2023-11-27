package Others;

import People.Person;
import States.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Simulation {
    public static double n;
    public static double m;
    List<Person> personList;

    public Simulation(double n, double m) {
        Simulation.n = n;
        Simulation.m = m;
        this.personList = new ArrayList<Person>();
    }

    public void checkBorders()
    {
        Iterator<Person> iterator = personList.iterator();
        while (iterator.hasNext())
        {
            Person person = iterator.next();
            Random random = new Random();
            double xCord = 0 + (2.5 - 0) * random.nextDouble();
            double yCord = 0 + (2.5 - 0) * random.nextDouble();
            Vector2D vector = new Vector2D(xCord, yCord);
            if(!person.move(vector))
            {
                iterator.remove();
            }
        }
    }

    void spawnWeakPerson() //TODO Procenty spawnowania chorych
    {
        Random random = new Random();
        int randomState = random.nextInt(20 - 1 + 1) + 1;
        State state = switch (randomState) {
            case 1 -> new SickNoSymptomsState();
            case 2 -> new SickSymptomsState();
            default -> new HealthyState();
        };

        spawnPerson(state);
    }

    void spawnStrongPerson() //TODO Procenty spawnowania chorych
    {
        Random random = new Random();
        int randomState = random.nextInt(4 - 1 + 1) + 1;
        State state = switch (randomState) {
            case 1 -> new HealthyState();
            case 2 -> new SickNoSymptomsState();
            case 3 -> new SickSymptomsState();
            case 4 -> new ResistantState();
            default -> null;
        };
        spawnPerson(state);
    }

    private void spawnPerson(State state) {
        Random random = new Random();
        double randomLocation = n + (m - n) * random.nextDouble();
        int randomLocationLine = random.nextInt(4 - 1 + 1) + 1;
        double x;
        double y;
        Person person = null;
        switch (randomLocationLine) {
            case 1 -> {
                x = randomLocation;
                y = 0.0;
                person = new Person(x, y, state);
            }
            case 2 -> {
                x = n;
                y = randomLocation;
                person = new Person(x, y, state);
            }
            case 3 -> {
                x = randomLocation;
                y = m;
                person = new Person(x, y, state);
            }
            case 4 -> {
                x = 0.0;
                y = randomLocation;
                person = new Person(x, y, state);
            }
        }
        personList.add(person);
    }

    public void spreadDisease()
    {
        for (Person person : personList)
        {
            if (person.getState() instanceof HealthyState)
            {
                int timeToSick = 0; // 0 - brak, 1 - 50%, 2 - 100%
                for (Person other : personList)
                {
                    if (canInfect(person, other) && other.getState() instanceof SickSymptomsState)
                    {
                        timeToSick = 2;
                        break;
                    }
                    else if (canInfect(person, other) && other.getState() instanceof SickNoSymptomsState)
                    {
                        timeToSick = 1;
                    }
                }
                if(timeToSick == 0)
                {
                    person.setTimeToSick50(75);
                    person.setTimeToSick100(75);
                }
                if(timeToSick == 1)
                {
                    person.setTimeToSick50(person.getTimeToSick50() - 1);
                }
                if(person.getTimeToSick100() == 2)
                {
                    person.setTimeToSick100(person.getTimeToSick100() - 1);
                }
                if(person.getTimeToSick() == 0)
                {
                    Random random = new Random();
                    double randomSickness = 100 * random.nextDouble();
                    if (randomSickness < person.getSicknessProbability())
                    {
                        int randomSymptoms = random.nextInt(2) + 1;
                        if(randomSymptoms == 1)
                        {
                            State state = new SickNoSymptomsState();
                            person.setState(state);
                        }
                        else
                        {
                            State state = new SickSymptomsState();
                            person.setState(state);
                        }
                    }
                    else
                    {
                        person.setTimeToSick50(75);
                        person.setTimeToSick100(75);
                    }
                }
            }
        }
    }

    private boolean canInfect(Person person1, Person person2)
    {
        double distance = Math.sqrt(Math.pow(person2.getX() - person1.getX(), 2) + Math.pow(person2.getY() - person1.getY(), 2));
        if (distance < 2.)
        {
            return true;
        }
        return false;
    }
}
