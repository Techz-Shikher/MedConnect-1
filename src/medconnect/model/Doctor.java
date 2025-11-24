package medconnect.model;

public class Doctor extends Person {
    private String specialization;
    private int experience;

    public Doctor() {}

    public Doctor(int id, String name, String gender, String specialization, int experience) {
        super(id, name, gender);
        this.specialization = specialization;
        this.experience = experience;
    }

    @Override
    public String getDetails() {
        return super.getDetails() + ", Specialization: " + specialization + ", Experience: " + experience + " yrs";
    }

    // Getters and Setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
