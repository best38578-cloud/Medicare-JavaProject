package factory;
public class PediatricianFactory extends DoctorFactory {
    @Override public Doctor createDoctor(String name) { return new Pediatrician(name); }
}
