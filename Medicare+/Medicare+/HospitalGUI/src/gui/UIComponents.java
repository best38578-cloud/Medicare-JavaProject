package gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class UIComponents {

    // ── Rounded panel ─────────────────────────────────────────────────
    public static class RoundedPanel extends JPanel {
        private final int radius;
        private Color bg;
        public RoundedPanel(int radius, Color bg) {
            this.radius = radius; this.bg = bg;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Rounded border panel (card) ───────────────────────────────────
    public static class CardPanel extends JPanel {
        private final int radius;
        public CardPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
            setBackground(AppTheme.WHITE);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(AppTheme.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.setColor(AppTheme.BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Primary button ────────────────────────────────────────────────
    public static class PrimaryButton extends JButton {
        public PrimaryButton(String text) {
            super(text);
            setFont(AppTheme.FONT_BOLD);
            setForeground(Color.WHITE);
            setBackground(AppTheme.TEAL);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setBackground(AppTheme.TEAL_MID); repaint(); }
                public void mouseExited(MouseEvent e)  { setBackground(AppTheme.TEAL);     repaint(); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Danger button ─────────────────────────────────────────────────
    public static class DangerButton extends JButton {
        public DangerButton(String text) {
            super(text);
            setFont(AppTheme.FONT_BOLD);
            setForeground(AppTheme.RED);
            setBackground(AppTheme.RED_LIGHT);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            g2.setColor(new Color(192, 57, 43, 80));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 10, 10));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Outline button ────────────────────────────────────────────────
    public static class OutlineButton extends JButton {
        public OutlineButton(String text) {
            super(text);
            setFont(AppTheme.FONT_BOLD);
            setForeground(AppTheme.TEAL);
            setBackground(AppTheme.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            g2.setColor(AppTheme.TEAL);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 10, 10));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Styled text field ─────────────────────────────────────────────
    public static JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(AppTheme.FONT_BODY);
        f.setForeground(AppTheme.TEXT_PRI);
        f.setBackground(AppTheme.WHITE);
        f.setBorder(new RoundedBorder(10, AppTheme.BORDER));
        f.setPreferredSize(new Dimension(0, 38));
        return f;
    }

    // ── Styled combo box ──────────────────────────────────────────────
    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(AppTheme.FONT_BODY);
        c.setBackground(AppTheme.WHITE);
        c.setPreferredSize(new Dimension(0, 38));
        return c;
    }

    // ── Styled text area ──────────────────────────────────────────────
    public static JTextArea styledArea() {
        JTextArea a = new JTextArea(3, 20);
        a.setFont(AppTheme.FONT_BODY);
        a.setForeground(AppTheme.TEXT_PRI);
        a.setBackground(AppTheme.WHITE);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return a;
    }

    // ── Badge label ───────────────────────────────────────────────────
    public static JLabel badge(String text, Color bg, Color fg) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(fg);
        l.setOpaque(false);
        l.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        return l;
    }

    // ── Rounded border ────────────────────────────────────────────────
    public static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        public RoundedBorder(int radius, Color color) { this.radius = radius; this.color = color; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(x, y, w-1, h-1, radius, radius));
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(6, 10, 6, 10); }
    }

    // ── Section label ─────────────────────────────────────────────────
    public static JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_HEAD);
        l.setForeground(AppTheme.TEXT_PRI);
        return l;
    }

    public static JLabel bodyLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_BODY);
        l.setForeground(color);
        return l;
    }

    public static JSeparator separator() {
        JSeparator s = new JSeparator();
        s.setForeground(AppTheme.BORDER);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }
}
