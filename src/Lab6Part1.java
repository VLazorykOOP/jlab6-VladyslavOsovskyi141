package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class Lab6Part1 extends JPanel implements ActionListener {

    private double angle = 0;           
    private Color lineColor = Color.RED;
    private Timer timer;
    private int lineLength = 150;

    public Lab6Part1() {
        timer = new Timer(100, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int x0 = getWidth() / 2;
        int y0 = getHeight() / 2;

        AffineTransform old = g2.getTransform();
        g2.translate(x0, y0);
        g2.rotate(angle);

        g2.setStroke(new BasicStroke(4));
        g2.setColor(lineColor);
        g2.drawLine(0, 0, lineLength, 0);

        g2.setTransform(old);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        angle += Math.PI / 30;

        lineColor = new Color(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );

        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rotating Line");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.add(new Lab6Part1());
            frame.setVisible(true);
        });
    }
}