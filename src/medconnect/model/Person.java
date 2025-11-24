package medconnect.model;

public class Person {
    protected int id;
    protected String name;
    protected String gender;

    public Person() {}

    public Person(int id, String name, String gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public String getDetails() {
        return "ID: " + id + ", Name: " + name + ", Gender: " + gender;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
