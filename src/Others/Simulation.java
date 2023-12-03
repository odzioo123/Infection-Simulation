package Others;

import People.Person;
import People.PersonMemento;
import States.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Simulation {
    public static int n;
    public static int m;
    List<Person> personList;
    HashMap<Integer, List<PersonMemento>> saveMap = new HashMap<Integer, List<PersonMemento>>();
    private JFrame window;
    private Board board;
    private Bar bar;
    private int seconds;
    private boolean stop;

    public HashMap<Integer, List<PersonMemento>> getSaveMap() {
        return saveMap;
    }

    public Simulation(int n, int m) {
        Simulation.n = n;
        Simulation.m = m;
        this.personList = new ArrayList<Person>();
        this.window = new JFrame();
        this.board = new Board(this);
        this.seconds = 0;
        this.bar = new Bar(this);
        this.stop = false;
    }

    public void init() {
        window.setTitle("Simulation");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(this.board, BorderLayout.CENTER);
        window.add(this.bar, BorderLayout.PAGE_END);
        bar.createButtons();
        window.pack();
        window.setResizable(false);
        window.setVisible(true);
    }

    public void restore(Integer second) {
        personList.clear();
        for(PersonMemento memento : saveMap.get(second))
        {
            personList.add(new Person(memento));
        }
    }

    public void run(boolean immune) {
        while (true)
        {
            for (int i = 0; i < 25; i++)
            {
                sleep();
                checkBorders();
                spawnPerson(immune);
                spreadDisease();
                board.repaint();
            }

            seconds++;
            System.out.println(seconds);

            List<PersonMemento> savePersonList = new ArrayList<PersonMemento>();
            for(Person person : personList)
            {
                savePersonList.add(person.memento());
            }
            saveMap.put(seconds,savePersonList);

            System.out.println(Arrays.toString(getSaveMap().keySet().toArray(new Integer[0])));

            bar.refreshComboBox();

            if(this.stop)
            {
                stop = false;
                break;
            }

        }
    }

    public void stop()
    {
        this.stop = true;
    }
    public void sleep()
    {
        try
        {
            Thread.sleep(1000 / 25);
        } catch (InterruptedException exception)
        {
            exception.printStackTrace();
        }
    }
    public void createPops(boolean immune)
    {
        for (int i = 0; i< 400; i++)
        {
            double x = Math.random()*50;
            double y = Math.random()*50;
            double personState = Math.random();
            if(immune)
            {
                if(personState < 0.2)
                {
                    this.personList.add(new Person(x, y, new ResistantState()));
                }
                else if(personState < 0.8)
                {
                    this.personList.add(new Person(x, y, new HealthyState()));
                }
                else if(personState < 0.9)
                {
                    this.personList.add(new Person(x,y,new SickSymptomsState()));
                }
                else
                {
                    this.personList.add(new Person(x,y,new SickNoSymptomsState()));
                }
            }
            else
            {
                if(personState < 0.8)
                {
                    this.personList.add(new Person(x, y, new HealthyState()));
                }
                else if(personState < 0.9)
                {
                    this.personList.add(new Person(x,y,new SickSymptomsState()));
                }
                else
                {
                    this.personList.add(new Person(x,y,new SickNoSymptomsState()));
                }
            }
        }
    }
    public void checkBorders()
    {
        Iterator<Person> iterator = personList.iterator();
        while (iterator.hasNext())
        {
            Person person = iterator.next();
            Random random = new Random();
            double xCord = -0.1 + (0.2) * random.nextDouble();
            double yCord = -0.1 + (0.2) * random.nextDouble();
            Vector2D vector = new Vector2D(xCord, yCord);
            double velocity = random.nextDouble() * 0.1;
            xCord = velocity * xCord/vector.abs();
            yCord = velocity * xCord/vector.abs();
            Vector2D vectorReal = new Vector2D(xCord, yCord);
            if(!person.move(vectorReal))
            {
                iterator.remove();
            }
        }
    }

    public void spawnPerson(boolean immune)
    {
        if(immune)
            spawnWeakPerson();
        else
        {
            spawnStrongPerson();
        }
    }

    public void spawnWeakPerson() // 10% - Sick 90% - Healthy
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

    public void spawnStrongPerson() // 10% - resistant 10% - Sick 80% - Healthy
    {
        Random random = new Random();
        int randomState = random.nextInt(20 - 1 + 1) + 1;
        State state = switch (randomState) {
            case 1 -> new SickNoSymptomsState();
            case 2 -> new SickSymptomsState();
            case 3, 4 -> new ResistantState();
            default -> new HealthyState();
        };
        spawnPerson(state);
    }

    public void spawnPerson(State state) {
        Random random = new Random();
        double randomLocationX = (n) * random.nextDouble();
        double randomLocationY = (m) * random.nextDouble();
        int randomLocationLine = random.nextInt(4 - 1 + 1) + 1;
        double x;
        double y;
        Person person = null;
        switch (randomLocationLine) {
            case 1 -> {
                x = randomLocationX;
                y = 0.0;
                person = new Person(x, y, state);
            }
            case 2 -> {
                x = n;
                y = randomLocationY;
                person = new Person(x, y, state);
            }
            case 3 -> {
                x = randomLocationX;
                y = m;
                person = new Person(x, y, state);
            }
            case 4 -> {
                x = 0.0;
                y = randomLocationY;
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
                if(timeToSick == 2)
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
                        System.out.println("Zakażone " + person.getX() + " " + person.getY()); // Sprawdzenie
                        System.out.println("Szansa: " + person.getSicknessProbability());
                        if(randomSymptoms == 1)
                        {
                            person.setState(new SickNoSymptomsState());
                            int randomSicknessTime = random.nextInt(11);
                            person.setTimeToSick100((20+randomSicknessTime)*25);
                        }
                        else
                        {
                            person.setState(new SickSymptomsState());
                            int randomSicknessTime = random.nextInt(11);
                            person.setTimeToSick100((20+randomSicknessTime)*25);
                        }
                    }
                    else
                    {
                        person.setTimeToSick50(75);
                        person.setTimeToSick100(75);
                    }
                }
            }
            else if(person.getState() instanceof SickNoSymptomsState || person.getState() instanceof SickSymptomsState)
            {
                person.setTimeToSick100(person.getTimeToSick100() - 1);
                if(person.getTimeToSick100() == 0)
                {
                    person.setState(new ResistantState());
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

    public int getSeconds() {
        return seconds;
    }


}


