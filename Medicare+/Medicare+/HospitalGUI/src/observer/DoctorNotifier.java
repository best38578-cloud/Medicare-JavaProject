package observer;
import builder.Appointment;

public class DoctorNotifier implements AppointmentObserver {
    private final String doctorName;
    public DoctorNotifier(String doctorName) { this.doctorName = doctorName; }

    @Override
    public void update(Appointment a, String status) {
        System.out.println("[DOCTOR] Dr. " + doctorName
            + ", appointment with " + a.getPatient().getName()
            + " on " + a.getDate() + " at " + a.getTime() + " is: " + status);
    }
}
