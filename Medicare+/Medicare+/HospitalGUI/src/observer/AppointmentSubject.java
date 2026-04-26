package observer;
import builder.Appointment;

public interface AppointmentSubject {
    void addObserver(AppointmentObserver o);
    void removeObserver(AppointmentObserver o);
    void notifyObservers(Appointment a, String status);
}
