package builder;

import factory.Doctor;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private Patient   patient;
    private Doctor    doctor;
    private LocalDate date;
    private LocalTime time;
    private String    notes;
    private String    status;

    private Appointment() {}

    public Patient   getPatient() { return patient; }
    public Doctor    getDoctor()  { return doctor; }
    public LocalDate getDate()    { return date; }
    public LocalTime getTime()    { return time; }
    public String    getNotes()   { return notes; }
    public String    getStatus()  { return status; }
    public void      setStatus(String s) { this.status = s; }

    @Override public String toString() {
        return "Dr. " + doctor.getName() + " | " + date + " " + time + " | " + status;
    }

    public static class AppointmentBuilder {
        private final Appointment a = new Appointment();
        public AppointmentBuilder setPatient(Patient p)  { a.patient = p; return this; }
        public AppointmentBuilder setDoctor(Doctor d)    { a.doctor  = d; return this; }
        public AppointmentBuilder setDate(LocalDate d)   { a.date    = d; return this; }
        public AppointmentBuilder setTime(LocalTime t)   { a.time    = t; return this; }
        public AppointmentBuilder setNotes(String n)     { a.notes   = n; return this; }
        public Appointment build() {
            if (a.patient == null || a.doctor == null || a.date == null || a.time == null)
                throw new IllegalStateException("Missing required fields.");
            a.status = "CONFIRMED";
            return a;
        }
    }
}
