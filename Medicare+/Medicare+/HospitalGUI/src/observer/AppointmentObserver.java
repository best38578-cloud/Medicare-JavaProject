package observer;
import builder.Appointment;

public interface AppointmentObserver {
    void update(Appointment appointment, String status);
}
