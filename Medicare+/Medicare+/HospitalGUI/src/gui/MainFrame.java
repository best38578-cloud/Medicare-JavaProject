package gui;

import builder.Appointment;
import builder.Patient;
import factory.*;
import observer.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    private final Patient            patient;
    private final List<Doctor>       doctors;
    private final AppointmentManager manager;
    private       JPanel             contentArea;
    private       JButton[]          navBtns;

    private static final String[] NAV_LABELS = {"Home", "Find Doctor", "Book", "Appointments", "Schedule"};

    public MainFrame(Patient patient) {
        this.patient = patient;
        this.manager = new AppointmentManager();
        this.doctors = Arrays.asList(
            new CardiologistFactory().createDoctor("Ramon Ramos"),
            new DermatologistFactory().createDoctor("Sara Santos"),
            new PediatricianFactory().createDoctor("Carlo Cruz")
        );

        setTitle("MediCare+ — " + patient.getName());
        setSize(920, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(AppTheme.CREAM);
        setContentPane(root);

        root.add(buildTopBar(),  BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(AppTheme.CREAM);
        root.add(contentArea, BorderLayout.CENTER);

        showHome();
    }

    // ── Top bar ───────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(AppTheme.NAVY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bar.setPreferredSize(new Dimension(920, 54));
        bar.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 22));

        JLabel logo = new JLabel("MediCare+");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        bar.add(logo, BorderLayout.WEST);

        JLabel user = new JLabel(patient.getName() + "  |  " + patient.getPatientId());
        user.setFont(AppTheme.FONT_BODY);
        user.setForeground(new Color(143, 168, 192));
        bar.add(user, BorderLayout.EAST);
        return bar;
    }

    // ── Sidebar ───────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel side = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(15, 35, 65));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        side.setPreferredSize(new Dimension(195, 586));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));

        navBtns = new JButton[NAV_LABELS.length];

        for (int i = 0; i < NAV_LABELS.length; i++) {
            final int idx = i;
            JButton btn = new JButton(NAV_LABELS[i]) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getBackground() == AppTheme.TEAL) {
                        g2.setColor(AppTheme.TEAL);
                        g2.fill(new RoundRectangle2D.Float(10, 3, getWidth() - 20, getHeight() - 6, 10, 10));
                    } else if (getModel().isRollover()) {
                        g2.setColor(new Color(255, 255, 255, 20));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                    }
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setFont(AppTheme.FONT_BODY);
            btn.setForeground(new Color(190, 210, 230));
            btn.setBackground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setMaximumSize(new Dimension(195, 44));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                setActiveNav(idx);
                if      (idx == 0) showHome();
                else if (idx == 1) showRecommend();
                else if (idx == 2) showBook(null);
                else if (idx == 3) showAppointments();
                else if (idx == 4) showSchedule();
            });
            navBtns[i] = btn;
            side.add(btn);
            side.add(Box.createVerticalStrut(2));
        }

        side.add(Box.createVerticalGlue());

        JButton logout = new JButton("Logout");
        logout.setFont(AppTheme.FONT_BODY);
        logout.setForeground(new Color(192, 57, 43));
        logout.setFocusPainted(false);
        logout.setBorderPainted(false);
        logout.setContentAreaFilled(false);
        logout.setOpaque(false);
        logout.setMaximumSize(new Dimension(195, 40));
        logout.setHorizontalAlignment(SwingConstants.LEFT);
        logout.setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        side.add(logout);

        setActiveNav(0);
        return side;
    }

    private void setActiveNav(int idx) {
        for (int i = 0; i < navBtns.length; i++) {
            navBtns[i].setBackground(i == idx ? AppTheme.TEAL : Color.BLACK);
            navBtns[i].setForeground(i == idx ? Color.WHITE : new Color(190, 210, 230));
            navBtns[i].setFont(i == idx
                ? new Font("Segoe UI", Font.BOLD, 13)
                : AppTheme.FONT_BODY);
        }
    }

    private void loadPanel(JPanel p) {
        contentArea.removeAll();
        contentArea.add(p, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ══════════════════════════════════════════════════════════════════
    // HOME SCREEN
    // ══════════════════════════════════════════════════════════════════
    private void showHome() {
        setActiveNav(0);

        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);
        col.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));

        long upcoming = manager.getAppointments().stream()
            .filter(a -> a.getStatus().equals("CONFIRMED")).count();

        // ── Hero ──────────────────────────────────────────────────────
        JPanel hero = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.NAVY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(new Color(10, 123, 108, 55));
                g2.fillOval(getWidth() - 110, -35, 170, 170);
                g2.setColor(new Color(10, 123, 108, 30));
                g2.fillOval(getWidth() - 60, 75, 110, 110);
                g2.dispose();
            }
        };
        hero.setOpaque(false);
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        hero.setPreferredSize(new Dimension(600, 110));
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel greet = new JLabel("Good Morning");
        greet.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        greet.setForeground(AppTheme.TEAL_MID);
        greet.setBounds(22, 16, 300, 16);

        JLabel nameLabel = new JLabel(patient.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(22, 34, 460, 30);

        JLabel subLabel = new JLabel("You have " + upcoming + " upcoming appointment(s) this week.");
        subLabel.setFont(AppTheme.FONT_SMALL);
        subLabel.setForeground(new Color(143, 168, 192));
        subLabel.setBounds(22, 70, 500, 18);

        hero.add(greet); hero.add(nameLabel); hero.add(subLabel);
        col.add(hero);
        col.add(Box.createVerticalStrut(14));

        // ── Stats ─────────────────────────────────────────────────────
        JPanel stats = new JPanel(new GridLayout(1, 3, 12, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        stats.add(statBox(String.valueOf(upcoming), "Upcoming"));
        stats.add(statBox("3", "Doctors"));
        stats.add(statBox("5", "Completed"));
        col.add(stats);
        col.add(Box.createVerticalStrut(14));

        // ── Quick actions ─────────────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(2, 2, 12, 12));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.add(quickCard("Find Doctor",      "Get matched by specialty", AppTheme.TEAL_LIGHT,      AppTheme.TEAL,  () -> { setActiveNav(1); showRecommend(); }));
        grid.add(quickCard("Book Appointment", "Schedule a new visit",     new Color(232, 238, 245), AppTheme.NAVY,  () -> { setActiveNav(2); showBook(null); }));
        grid.add(quickCard("My Appointments",  "Manage and cancel",        AppTheme.RED_LIGHT,       AppTheme.RED,   () -> { setActiveNav(3); showAppointments(); }));
        grid.add(quickCard("Schedule",         "View weekly timeline",     AppTheme.AMBER_LIGHT,     AppTheme.AMBER, () -> { setActiveNav(4); showSchedule(); }));
        col.add(grid);
        col.add(Box.createVerticalStrut(22));

        // ── Next Appointment ──────────────────────────────────────────
        JLabel nextTitle = UIComponents.sectionTitle("Next Appointment");
        nextTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        col.add(nextTitle);
        col.add(Box.createVerticalStrut(10));

        Optional<Appointment> nextOpt = manager.getAppointments().stream()
            .filter(a -> a.getStatus().equals("CONFIRMED"))
            .findFirst();

        if (nextOpt.isPresent()) {
            Appointment a = nextOpt.get();

            UIComponents.CardPanel card = new UIComponents.CardPanel(12);
            card.setLayout(new BorderLayout(20, 0));
            card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

            JPanel left = new JPanel();
            left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
            left.setOpaque(false);

            JLabel dName = new JLabel("Dr. " + a.getDoctor().getName());
            dName.setFont(new Font("Segoe UI", Font.BOLD, 15));
            dName.setForeground(AppTheme.TEXT_PRI);

            JLabel dSpec = new JLabel(a.getDoctor().getSpecialty());
            dSpec.setFont(AppTheme.FONT_BODY);
            dSpec.setForeground(AppTheme.TEXT_SEC);

            JLabel dDate = new JLabel("Date: " + a.getDate() + "   Time: " + a.getTime());
            dDate.setFont(AppTheme.FONT_BODY);
            dDate.setForeground(AppTheme.TEXT_SEC);

            JLabel dRoom = new JLabel(a.getDoctor().getRoom());
            dRoom.setFont(AppTheme.FONT_BODY);
            dRoom.setForeground(AppTheme.TEXT_MUTED);

            left.add(dName);
            left.add(Box.createVerticalStrut(4));
            left.add(dSpec);
            left.add(Box.createVerticalStrut(4));
            left.add(dDate);
            left.add(Box.createVerticalStrut(4));
            left.add(dRoom);
            card.add(left, BorderLayout.CENTER);

            // Right — CONFIRMED badge + Cancel button, same size, side by side
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 16));
            right.setOpaque(false);
            right.setPreferredSize(new Dimension(268, 68));

            Dimension btnSize = new Dimension(115, 36);

            // CONFIRMED — custom painted label so color is never grayed out
            JLabel confirmedBtn = new JLabel("CONFIRMED") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(AppTheme.TEAL_LIGHT);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                    g2.setColor(AppTheme.TEAL);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 12, 12));
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            confirmedBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
            confirmedBtn.setForeground(AppTheme.TEAL);
            confirmedBtn.setHorizontalAlignment(SwingConstants.CENTER);
            confirmedBtn.setOpaque(false);
            confirmedBtn.setPreferredSize(btnSize);

            UIComponents.DangerButton cancelBtn = new UIComponents.DangerButton("Cancel");
            cancelBtn.setPreferredSize(btnSize);
            cancelBtn.setMaximumSize(btnSize);
            cancelBtn.addActionListener(e -> {
                manager.addObserver(new PatientNotifier(patient.getName()));
                manager.addObserver(new DoctorNotifier(a.getDoctor().getName()));
                manager.cancel(a);
                JOptionPane.showMessageDialog(this,
                    "Appointment cancelled.\nNotifications sent to patient and doctor.",
                    "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                showHome();
            });

            right.add(confirmedBtn);
            right.add(cancelBtn);
            card.add(right, BorderLayout.EAST);
            col.add(card);

        } else {
            JLabel none = UIComponents.bodyLabel("No upcoming appointments. Book one now!", AppTheme.TEXT_MUTED);
            none.setAlignmentX(Component.LEFT_ALIGNMENT);
            col.add(none);
        }

        col.add(Box.createVerticalStrut(20));

        JScrollPane scroll = new JScrollPane(col);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(AppTheme.CREAM);
        scroll.getViewport().setBackground(AppTheme.CREAM);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.CREAM);
        p.add(scroll, BorderLayout.CENTER);
        loadPanel(p);
    }

        // ══════════════════════════════════════════════════════════════════
    // FIND DOCTOR
    // ══════════════════════════════════════════════════════════════════
    private void showRecommend() {
        setActiveNav(1);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.CREAM);
        p.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));
        p.add(screenHeader("Find a Doctor", "Choose a specialist and book directly"), BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        Color[] avatarBg = {AppTheme.TEAL_LIGHT, new Color(238, 237, 254), AppTheme.AMBER_LIGHT};
        Color[] avatarFg = {AppTheme.TEAL,        new Color(60, 52, 137),   AppTheme.AMBER};

        for (int di = 0; di < doctors.size(); di++) {
            final Doctor d    = doctors.get(di);
            final Color  aBg  = avatarBg[di];
            final Color  aFg  = avatarFg[di];

            UIComponents.CardPanel card = new UIComponents.CardPanel(12);
            card.setLayout(new BorderLayout(14, 0));
            card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel avatar = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(aBg);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                    g2.setColor(aFg);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                    String init = String.valueOf(d.getName().charAt(0))
                        + String.valueOf(d.getName().split(" ")[1].charAt(0));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(init, (getWidth() - fm.stringWidth(init)) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 2);
                    g2.dispose();
                }
            };
            avatar.setPreferredSize(new Dimension(50, 50));
            avatar.setOpaque(false);

            JPanel info = new JPanel(new GridLayout(3, 1, 0, 3));
            info.setOpaque(false);
            info.add(boldLabel("Dr. " + d.getName(), AppTheme.TEXT_PRI));
            info.add(UIComponents.bodyLabel(
                d.getSpecialty() + "   |   " + d.getYearsExp() + " yrs exp   |   Rating: " + d.getRating(),
                AppTheme.TEXT_SEC));
            String avail     = d.isAvailableToday() ? "Available today" : "Available tomorrow";
            Color  availColor = d.isAvailableToday() ? AppTheme.TEAL : AppTheme.AMBER;
            info.add(UIComponents.bodyLabel(avail, availColor));

            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
            right.setOpaque(false);
            UIComponents.PrimaryButton bookBtn = new UIComponents.PrimaryButton("Book Now");
            bookBtn.setPreferredSize(new Dimension(96, 34));
            bookBtn.addActionListener(e -> { setActiveNav(2); showBook(d); });
            right.add(bookBtn);

            card.add(avatar, BorderLayout.WEST);
            card.add(info,   BorderLayout.CENTER);
            card.add(right,  BorderLayout.EAST);
            list.add(card);
            list.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        wrap.add(scroll, BorderLayout.CENTER);
        p.add(wrap, BorderLayout.CENTER);
        loadPanel(p);
    }

    // ══════════════════════════════════════════════════════════════════
    // BOOK SCREEN
    // ══════════════════════════════════════════════════════════════════
    private void showBook(Doctor preselected) {
        setActiveNav(2);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.CREAM);
        p.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));
        p.add(screenHeader("Book Appointment", "Fill in the details below"), BorderLayout.NORTH);

        UIComponents.CardPanel form = new UIComponents.CardPanel(14);
        form.setLayout(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(4, 0, 4, 0);

        // Doctor dropdown
        addFormLabel(form, c, "SELECT DOCTOR");
        c.gridy++;
        String[] docNames = new String[doctors.size()];
        for (int i = 0; i < doctors.size(); i++) {
            docNames[i] = "Dr. " + doctors.get(i).getName() + " — " + doctors.get(i).getSpecialty();
        }
        JComboBox<String> docCombo = UIComponents.styledCombo(docNames);
        if (preselected != null) {
            for (int i = 0; i < doctors.size(); i++) {
                if (doctors.get(i) == preselected) { docCombo.setSelectedIndex(i); break; }
            }
        }
        form.add(docCombo, c);

        // Date
        c.gridy++; c.insets = new Insets(12, 0, 4, 0);
        addFormLabel(form, c, "DATE (YYYY-MM-DD)");
        c.gridy++; c.insets = new Insets(4, 0, 4, 0);
        JTextField dateField = UIComponents.styledField("e.g. 2026-05-10");
        dateField.setText(LocalDate.now().plusDays(1).toString());
        form.add(dateField, c);

        // Time
        c.gridy++; c.insets = new Insets(12, 0, 4, 0);
        addFormLabel(form, c, "TIME SLOT");
        c.gridy++; c.insets = new Insets(4, 0, 4, 0);
        JComboBox<String> timeCombo = UIComponents.styledCombo(
            new String[]{"09:00", "10:00", "11:00", "14:00", "15:00"});
        form.add(timeCombo, c);

        // Notes
        c.gridy++; c.insets = new Insets(12, 0, 4, 0);
        addFormLabel(form, c, "REASON / NOTES");
        c.gridy++; c.insets = new Insets(4, 0, 4, 0);
        JTextArea notes = UIComponents.styledArea();
        JScrollPane noteScroll = new JScrollPane(notes);
        noteScroll.setPreferredSize(new Dimension(0, 80));
        noteScroll.setBorder(new UIComponents.RoundedBorder(10, AppTheme.BORDER));
        form.add(noteScroll, c);

        // Confirm button
        c.gridy++; c.insets = new Insets(18, 0, 0, 0);
        UIComponents.PrimaryButton confirmBtn = new UIComponents.PrimaryButton("CONFIRM BOOKING");
        confirmBtn.setPreferredSize(new Dimension(0, 44));
        confirmBtn.addActionListener(e -> {
            try {
                Doctor selected = doctors.get(docCombo.getSelectedIndex());
                LocalDate date  = LocalDate.parse(dateField.getText().trim());
                LocalTime time  = LocalTime.parse((String) timeCombo.getSelectedItem());
                String note     = notes.getText().trim().isEmpty()
                    ? "General consultation" : notes.getText().trim();

                Appointment appt = new Appointment.AppointmentBuilder()
                    .setPatient(patient)
                    .setDoctor(selected)
                    .setDate(date)
                    .setTime(time)
                    .setNotes(note)
                    .build();

                manager.addObserver(new PatientNotifier(patient.getName()));
                manager.addObserver(new DoctorNotifier(selected.getName()));
                manager.book(appt);

                JOptionPane.showMessageDialog(this,
                    "Appointment CONFIRMED!\n\n"
                    + "Patient   : " + patient.getName() + "\n"
                    + "Doctor    : Dr. " + selected.getName() + "\n"
                    + "Specialty : " + selected.getSpecialty() + "\n"
                    + "Date      : " + date + "\n"
                    + "Time      : " + time + "\n"
                    + "Room      : " + selected.getRoom() + "\n"
                    + "Notes     : " + note + "\n\n"
                    + "Notifications sent to patient and doctor.",
                    "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

                setActiveNav(3);
                showAppointments();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid input.\nPlease check the date format: YYYY-MM-DD\nExample: 2026-05-15",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(confirmBtn, c);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        wrap.add(form, BorderLayout.NORTH);
        p.add(wrap, BorderLayout.CENTER);
        loadPanel(p);
    }

    // ══════════════════════════════════════════════════════════════════
    // APPOINTMENTS SCREEN
    // ══════════════════════════════════════════════════════════════════
    private void showAppointments() {
        setActiveNav(3);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.CREAM);
        p.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));
        p.add(screenHeader("My Appointments", "Manage and cancel your bookings"), BorderLayout.NORTH);

        List<Appointment> appts = manager.getAppointments();

        if (appts.isEmpty()) {
            JPanel empty = new JPanel(new BorderLayout());
            empty.setOpaque(false);
            empty.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
            JLabel msg = UIComponents.bodyLabel(
                "No appointments found. Book one from Find Doctor or Book.", AppTheme.TEXT_MUTED);
            msg.setHorizontalAlignment(SwingConstants.CENTER);
            empty.add(msg, BorderLayout.NORTH);
            JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnWrap.setOpaque(false);
            UIComponents.PrimaryButton bookNow = new UIComponents.PrimaryButton("Book Now");
            bookNow.setPreferredSize(new Dimension(160, 40));
            bookNow.addActionListener(e -> { setActiveNav(2); showBook(null); });
            btnWrap.add(bookNow);
            empty.add(btnWrap, BorderLayout.CENTER);
            p.add(empty, BorderLayout.CENTER);
            loadPanel(p);
            return;
        }

        // Build table model
        String[] cols = {"#", "Doctor", "Specialty", "Date", "Time", "Notes", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (int i = 0; i < appts.size(); i++) {
            Appointment a = appts.get(i);
            model.addRow(new Object[]{
                i + 1,
                "Dr. " + a.getDoctor().getName(),
                a.getDoctor().getSpecialty(),
                a.getDate().toString(),
                a.getTime().toString(),
                a.getNotes(),
                a.getStatus()
            });
        }

        JTable table = new JTable(model);
        table.setFont(AppTheme.FONT_BODY);
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(AppTheme.WHITE);
        table.setForeground(AppTheme.TEXT_PRI);
        table.setSelectionBackground(AppTheme.TEAL_LIGHT);
        table.setSelectionForeground(AppTheme.TEXT_PRI);
        table.getTableHeader().setFont(AppTheme.FONT_LABEL);
        table.getTableHeader().setBackground(AppTheme.SURFACE);
        table.getTableHeader().setForeground(AppTheme.TEXT_SEC);
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());

        // Status column coloring
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                String s = v == null ? "" : v.toString();
                setForeground("CONFIRMED".equals(s) ? AppTheme.TEAL : AppTheme.RED);
                setFont(AppTheme.FONT_BOLD);
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new UIComponents.RoundedBorder(12, AppTheme.BORDER));
        scroll.getViewport().setBackground(AppTheme.WHITE);

        // Cancel button below table
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 12));
        bottom.setOpaque(false);
        JLabel hint = UIComponents.bodyLabel("Select a row, then click Cancel", AppTheme.TEXT_MUTED);
        bottom.add(hint);
        bottom.add(Box.createHorizontalStrut(16));

        UIComponents.DangerButton cancelBtn = new UIComponents.DangerButton("Cancel Selected");
        cancelBtn.setPreferredSize(new Dimension(160, 36));
        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this,
                    "Click on an appointment row first, then press Cancel.",
                    "No Row Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Appointment target = manager.getAppointments().get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel appointment with Dr. " + target.getDoctor().getName()
                    + "\nDate: " + target.getDate() + "   Time: " + target.getTime() + " ?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.addObserver(new PatientNotifier(patient.getName()));
                manager.addObserver(new DoctorNotifier(target.getDoctor().getName()));
                manager.cancel(target);
                JOptionPane.showMessageDialog(this,
                    "Appointment cancelled.\nNotifications sent to patient and doctor.",
                    "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                showAppointments();
            }
        });
        bottom.add(cancelBtn);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        wrap.add(scroll, BorderLayout.CENTER);
        wrap.add(bottom, BorderLayout.SOUTH);
        p.add(wrap, BorderLayout.CENTER);
        loadPanel(p);
    }

    // ══════════════════════════════════════════════════════════════════
    // SCHEDULE SCREEN
    // ══════════════════════════════════════════════════════════════════
    private void showSchedule() {
        setActiveNav(4);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.CREAM);
        p.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));
        p.add(screenHeader("My Schedule", "All booked appointments by time slot"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        String[] slots = {"09:00", "10:00", "11:00", "14:00", "15:00"};

        for (String slot : slots) {
            LocalTime slotTime = LocalTime.parse(slot);
            List<Appointment> atSlot = manager.getAppointments().stream()
                .filter(a -> a.getStatus().equals("CONFIRMED") && a.getTime().equals(slotTime))
                .collect(Collectors.toList());

            JPanel row = new JPanel(new BorderLayout(14, 0));
            row.setOpaque(false);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, atSlot.isEmpty() ? 56 : 58 * atSlot.size()));
            row.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

            JLabel timeLabel = new JLabel(slot);
            timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            timeLabel.setForeground(atSlot.isEmpty() ? AppTheme.TEXT_MUTED : AppTheme.TEAL);
            timeLabel.setPreferredSize(new Dimension(54, 0));
            timeLabel.setVerticalAlignment(SwingConstants.TOP);
            row.add(timeLabel, BorderLayout.WEST);

            if (atSlot.isEmpty()) {
                UIComponents.CardPanel emptyCard = new UIComponents.CardPanel(10);
                emptyCard.setLayout(new BorderLayout());
                emptyCard.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
                emptyCard.add(UIComponents.bodyLabel("Available", AppTheme.TEXT_MUTED), BorderLayout.CENTER);
                emptyCard.add(UIComponents.badge("FREE", AppTheme.SURFACE, AppTheme.TEXT_MUTED), BorderLayout.EAST);
                row.add(emptyCard, BorderLayout.CENTER);
            } else {
                JPanel slotStack = new JPanel();
                slotStack.setLayout(new BoxLayout(slotStack, BoxLayout.Y_AXIS));
                slotStack.setOpaque(false);

                for (Appointment a : atSlot) {
                    JPanel bookedCard = new JPanel(new BorderLayout()) {
                        @Override protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(AppTheme.TEAL_LIGHT);
                            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                            g2.setColor(AppTheme.TEAL);
                            g2.setStroke(new BasicStroke(1.5f));
                            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 10, 10));
                            g2.dispose();
                            super.paintComponent(g);
                        }
                    };
                    bookedCard.setOpaque(false);
                    bookedCard.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
                    bookedCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
                    bookedCard.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
                    info.setOpaque(false);
                    info.add(boldLabel("Dr. " + a.getDoctor().getName() + "   —   " + a.getDoctor().getSpecialty(), AppTheme.TEAL));
                    info.add(UIComponents.bodyLabel("Date: " + a.getDate() + "   " + a.getDoctor().getRoom(), new Color(13, 100, 80)));
                    bookedCard.add(info, BorderLayout.CENTER);
                    bookedCard.add(UIComponents.badge("CONFIRMED", AppTheme.TEAL, AppTheme.WHITE), BorderLayout.EAST);
                    slotStack.add(bookedCard);
                    slotStack.add(Box.createVerticalStrut(4));
                }
                row.add(slotStack, BorderLayout.CENTER);
            }
            content.add(row);
            content.add(Box.createVerticalStrut(6));
        }

        // Summary list of all confirmed
        content.add(Box.createVerticalStrut(20));
        JLabel sumTitle = boldLabel("All Confirmed Appointments", AppTheme.TEXT_PRI);
        sumTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(sumTitle);
        content.add(Box.createVerticalStrut(10));

        List<Appointment> confirmed = manager.getAppointments().stream()
            .filter(a -> a.getStatus().equals("CONFIRMED"))
            .collect(Collectors.toList());

        if (confirmed.isEmpty()) {
            JLabel none = UIComponents.bodyLabel("No confirmed appointments yet. Book one!", AppTheme.TEXT_MUTED);
            none.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(none);
        } else {
            UIComponents.CardPanel sumCard = new UIComponents.CardPanel(12);
            sumCard.setLayout(new BoxLayout(sumCard, BoxLayout.Y_AXIS));
            sumCard.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
            sumCard.setAlignmentX(Component.LEFT_ALIGNMENT);
            sumCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40 + confirmed.size() * 30));
            for (int i = 0; i < confirmed.size(); i++) {
                Appointment a = confirmed.get(i);
                JLabel lbl = UIComponents.bodyLabel(
                    (i + 1) + ".   " + a.getDate() + "   " + a.getTime()
                    + "      Dr. " + a.getDoctor().getName()
                    + "   —   " + a.getDoctor().getSpecialty(),
                    AppTheme.TEXT_SEC);
                lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                sumCard.add(lbl);
                if (i < confirmed.size() - 1) sumCard.add(Box.createVerticalStrut(8));
            }
            content.add(sumCard);
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        wrap.add(scroll, BorderLayout.CENTER);
        p.add(wrap, BorderLayout.CENTER);
        loadPanel(p);
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private JPanel screenHeader(String title, String sub) {
        JPanel h = new JPanel(new GridLayout(2, 1, 0, 4));
        h.setOpaque(false);
        h.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        JLabel t = new JLabel(title); t.setFont(AppTheme.FONT_TITLE); t.setForeground(AppTheme.TEXT_PRI);
        JLabel s = new JLabel(sub);   s.setFont(AppTheme.FONT_BODY);  s.setForeground(AppTheme.TEXT_SEC);
        h.add(t); h.add(s);
        return h;
    }

    private JPanel statBox(String num, String label) {
        JPanel box = new JPanel(new GridLayout(2, 1, 0, 4));
        box.setBackground(AppTheme.SURFACE);
        box.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        JLabel n = new JLabel(num);           n.setFont(new Font("Segoe UI", Font.BOLD, 26)); n.setForeground(AppTheme.NAVY);
        JLabel l = new JLabel(label.toUpperCase()); l.setFont(AppTheme.FONT_LABEL); l.setForeground(AppTheme.TEXT_MUTED);
        box.add(n); box.add(l);
        return box;
    }

    private JPanel quickCard(String title, String sub, Color bg, Color fg, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(0, 5)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel t = new JLabel(title); t.setFont(AppTheme.FONT_BOLD); t.setForeground(fg);
        JLabel s = new JLabel(sub);   s.setFont(AppTheme.FONT_SMALL); s.setForeground(AppTheme.TEXT_SEC);
        card.add(t, BorderLayout.CENTER);
        card.add(s, BorderLayout.SOUTH);
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { action.run(); }
        });
        return card;
    }

    private JLabel boldLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_BOLD);
        l.setForeground(color);
        return l;
    }

    private void addFormLabel(JPanel p, GridBagConstraints c, String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_LABEL);
        l.setForeground(AppTheme.TEXT_SEC);
        p.add(l, c);
    }
}
