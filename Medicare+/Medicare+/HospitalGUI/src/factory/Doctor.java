package factory;

public abstract class Doctor {
    protected String name;
    protected String specialty;
    protected String room;
    protected double rating;
    protected int yearsExp;
    protected boolean availableToday;

    public Doctor(String name, String specialty, String room, double rating, int yearsExp, boolean availableToday) {
        this.name = name;
        this.specialty = specialty;
        this.room = room;
        this.rating = rating;
        this.yearsExp = yearsExp;
        this.availableToday = availableToday;
    }

    public abstract String getInfo();
    public String getName()           { return name; }
    public String getSpecialty()      { return specialty; }
    public String getRoom()           { return room; }
    public double getRating()         { return rating; }
    public int    getYearsExp()       { return yearsExp; }
    public boolean isAvailableToday() { return availableToday; }

    @Override public String toString() { return "Dr. " + name + " — " + specialty; }
}
