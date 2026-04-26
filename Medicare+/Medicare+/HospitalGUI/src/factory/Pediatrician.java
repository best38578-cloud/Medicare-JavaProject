package factory;

public class Pediatrician extends Doctor {
    public Pediatrician(String name) {
        super(name, "Pediatrics", "Room 108, Pediatrics Wing", 4.8, 10, true);
    }
    @Override public String getInfo() {
        return "Dr. " + name + " | " + specialty + " | Rating: " + rating + " | " + yearsExp + " yrs exp";
    }
}
