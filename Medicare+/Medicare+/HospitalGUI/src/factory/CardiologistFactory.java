package factory;
public class CardiologistFactory extends DoctorFactory {
    @Override public Doctor createDoctor(String name) { return new Cardiologist(name); }
}
