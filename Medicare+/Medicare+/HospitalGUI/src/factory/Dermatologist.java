package factory;

public class Dermatologist extends Doctor {
    public Dermatologist(String name) {
        super(name, "Dermatology", "Room 312, Dermatology Wing", 4.7, 8, false);
    }
    @Override public String getInfo() {
        return "Dr. " + name + " | " + specialty + " | Rating: " + rating + " | " + yearsExp + " yrs exp";
    }
}
