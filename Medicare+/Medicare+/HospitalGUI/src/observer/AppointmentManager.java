package observer;

import builder.Appointment;
import java.util.ArrayList;
import java.util.List;

public class AppointmentManager implements AppointmentSubject {
    private final List<AppointmentObserver> observers    = new ArrayList<>();
    private final List<Appointment>         appointments = new ArrayList<>();

    @Override public void addObserver(AppointmentObserver o)    { observers.add(o); }
    @Override public void removeObserver(AppointmentObserver o) { observers.remove(o); }
    @Override public void notifyObservers(Appointment a, String status) {
        for (AppointmentObserver o : observers) o.update(a, status);
        observers.clear();
    }

    public void book(Appointment a) {
        appointments.add(a);
        notifyObservers(a, "CONFIRMED");
    }

    public void cancel(Appointment a) {
        a.setStatus("CANCELLED");
        appointments.remove(a);
        notifyObservers(a, "CANCELLED");
    }

    public List<Appointment> getAppointments() { return appointments; }
}
