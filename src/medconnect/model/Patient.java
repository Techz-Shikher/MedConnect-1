package medconnect.model;

public class Patient extends Person {
    private int age;
    private String bloodGroup;

    public Patient() {}

    public Patient(int id, String name, String gender, int age, String bloodGroup) {
        super(id, name, gender);
        this.age = age;
        this.bloodGroup = bloodGroup;
    }

    @Override
    public String getDetails() {
        return super.getDetails() + ", Age: " + age + ", Blood Group: " + bloodGroup;
    }

    // Getters and Setters
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
}
