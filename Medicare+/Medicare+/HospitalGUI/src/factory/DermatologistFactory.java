package factory;
public class DermatologistFactory extends DoctorFactory {
    @Override public Doctor createDoctor(String name) { return new Dermatologist(name); }
}
