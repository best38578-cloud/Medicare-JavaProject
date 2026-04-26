package observer;
import builder.Appointment;

public class PatientNotifier implements AppointmentObserver {
    private final String patientName;
    public PatientNotifier(String patientName) { this.patientName = patientName; }

    @Override
    public void update(Appointment a, String status) {
        System.out.println("[PATIENT] Dear " + patientName
            + ", your appointment with Dr. " + a.getDoctor().getName()
            + " on " + a.getDate() + " at " + a.getTime() + " is: " + status);
    }
}
