package gui;

import builder.Patient;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("MediCare+ Hospital System");
        setSize(480, 560);
        setLocationRelativeTo(null);
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.CREAM);
        setContentPane(root);

        // ── Top hero banner ──────────────────────────────────────────
        JPanel hero = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // navy background
                g2.setColor(AppTheme.NAVY);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // decorative teal circle
                g2.setColor(new Color(10, 123, 108, 60));
                g2.fillOval(320, -40, 200, 200);
                g2.setColor(new Color(10, 123, 108, 40));
                g2.fillOval(360, 80, 140, 140);
                g2.dispose();
            }
        };
        hero.setPreferredSize(new Dimension(480, 180));

        // Cross icon
        JLabel icon = new JLabel("+") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.TEAL);
                g2.fill(new RoundRectangle2D.Float(0, 0, 56, 56, 14, 14));
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("+", (56 - fm.stringWidth("+")) / 2, (56 + fm.getAscent()) / 2 - 2);
                g2.dispose();
            }
        };
        icon.setBounds(40, 40, 56, 56);
        hero.add(icon);

        JLabel brand = new JLabel("MediCare+");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 28));
        brand.setForeground(Color.WHITE);
        brand.setBounds(110, 38, 220, 36);
        hero.add(brand);

        JLabel sub = new JLabel("Hospital Appointment System");
        sub.setFont(AppTheme.FONT_BODY);
        sub.setForeground(AppTheme.TEAL_MID);
        sub.setBounds(110, 76, 260, 20);
        hero.add(sub);

        JLabel tagline = new JLabel("Your health, our priority.");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        tagline.setForeground(new Color(143, 168, 192));
        tagline.setBounds(40, 130, 300, 20);
        hero.add(tagline);

        root.add(hero, BorderLayout.NORTH);

        // ── Login form card ──────────────────────────────────────────
        UIComponents.CardPanel card = new UIComponents.CardPanel(16);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel loginTitle = new JLabel("Sign In");
        loginTitle.setFont(AppTheme.FONT_TITLE);
        loginTitle.setForeground(AppTheme.TEXT_PRI);
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel loginSub = new JLabel("Name can be anything  |  ID must be P-<number>");
        loginSub.setFont(AppTheme.FONT_BODY);
        loginSub.setForeground(AppTheme.TEXT_SEC);
        loginSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Name field
        JLabel nameLabel = new JLabel("PATIENT NAME");
        nameLabel.setFont(AppTheme.FONT_LABEL);
        nameLabel.setForeground(AppTheme.TEXT_SEC);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField nameField = UIComponents.styledField("Full name");
        nameField.setText("Juan Dela Cruz");
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Patient ID
        JLabel idLabel = new JLabel("PATIENT ID");
        idLabel.setFont(AppTheme.FONT_LABEL);
        idLabel.setForeground(AppTheme.TEXT_SEC);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField idField = UIComponents.styledField("e.g. P-1001");
        idField.setText("P-1001");
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Sign In button
        UIComponents.PrimaryButton signInBtn = new UIComponents.PrimaryButton("SIGN IN");
        signInBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        signInBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        signInBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id   = idField.getText().trim();

            // Name must not be empty
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter your name.",
                    "Missing Name", JOptionPane.WARNING_MESSAGE);
                nameField.requestFocus();
                return;
            }

            // Patient ID must match P-<digits> format e.g. P-1001
            if (!id.matches("P-\\d+")) {
                JOptionPane.showMessageDialog(this,
                    "Invalid Patient ID.\n\n"
                    + "Patient ID must follow the format:  P-<number>\n"
                    + "Examples:  P-1001  |  P-2005  |  P-300",
                    "Invalid Patient ID", JOptionPane.ERROR_MESSAGE);
                idField.selectAll();
                idField.requestFocus();
                return;
            }

            Patient patient = new Patient(name, 34, id);
            dispose();
            new MainFrame(patient).setVisible(true);
        });

        // Sign Up button
        UIComponents.OutlineButton signUpBtn = new UIComponents.OutlineButton("CREATE ACCOUNT");
        signUpBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        signUpBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        signUpBtn.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Registration feature coming soon!\nPlease use the default credentials to log in.",
                "Sign Up", JOptionPane.INFORMATION_MESSAGE));

        card.add(loginTitle);
        card.add(Box.createVerticalStrut(4));
        card.add(loginSub);
        card.add(Box.createVerticalStrut(24));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(nameField);
        card.add(Box.createVerticalStrut(16));
        card.add(idLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(idField);
        card.add(Box.createVerticalStrut(24));
        card.add(signInBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(signUpBtn);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(AppTheme.CREAM);
        center.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        center.add(card, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
    }
}
