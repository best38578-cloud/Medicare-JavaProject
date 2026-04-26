package factory;

public class Cardiologist extends Doctor {
    public Cardiologist(String name) {
        super(name, "Cardiology", "Room 204, Cardiology Wing", 4.9, 12, true);
    }
    @Override public String getInfo() {
        return "Dr. " + name + " | " + specialty + " | Rating: " + rating + " | " + yearsExp + " yrs exp";
    }
}
