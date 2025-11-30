// MatrixGUI.java
package src;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class MatrixGUI {

    private JFrame frame;
    private JTextField filePathField;
    private JButton loadButton;
    private JButton computeButton;
    private JTable matrixTable;
    private JTable resultTable;
    private JLabel statusLabel;

    private static final double CUSTOM_EXCEPTION_THRESHOLD = 1000.0;

    private double[][] matrix = null;

    public MatrixGUI() {
        initUI();
    }

    private void initUI() {
        frame = new JFrame("Lab6 — Matrix X Calculator (Swing)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new BorderLayout(6,6));
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        inputPanel.add(new JLabel("Matrix file path:"));
        filePathField = new JTextField(40);
        inputPanel.add(filePathField);

        loadButton = new JButton("Load");
        inputPanel.add(loadButton);

        computeButton = new JButton("Compute X");
        computeButton.setEnabled(false);
        inputPanel.add(computeButton);

        topPanel.add(inputPanel, BorderLayout.NORTH);

        statusLabel = new JLabel("Ready");
        topPanel.add(statusLabel, BorderLayout.SOUTH);

        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        matrixTable = new JTable();
        resultTable = new JTable();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(matrixTable), new JScrollPane(resultTable));
        split.setResizeWeight(0.75);
        frame.getContentPane().add(split, BorderLayout.CENTER);

        loadButton.addActionListener(e -> onLoad());
        computeButton.addActionListener(e -> onCompute());

        filePathField.addActionListener(e -> onLoad());

        frame.setVisible(true);
    }

    private void onLoad() {
        String path = filePathField.getText().trim();
        if (path.isEmpty()) {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(frame);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                filePathField.setText(f.getAbsolutePath());
                path = f.getAbsolutePath();
            } else {
                return;
            }
        }

        File f = new File(path);
        try {
            matrix = MatrixProcessor.loadMatrixFromFile(f);
            showMatrixInTable(matrix);
            statusLabel.setText("Loaded matrix from: " + f.getName());
            computeButton.setEnabled(true);
        } catch (java.io.FileNotFoundException ex) {
            computeButton.setEnabled(false);
            JOptionPane.showMessageDialog(frame, "File not found:\n" + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("File not found");
        } catch (NumberFormatException ex) {
            computeButton.setEnabled(false);
            JOptionPane.showMessageDialog(frame, "Invalid number format:\n" + ex.getMessage(),
                    "Format Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Invalid format");
        } catch (java.io.IOException ex) {
            computeButton.setEnabled(false);
            JOptionPane.showMessageDialog(frame, "IO Error while reading file:\n" + ex.getMessage(),
                    "IO Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("IO error");
        } catch (Exception ex) {
            computeButton.setEnabled(false);
            JOptionPane.showMessageDialog(frame, "Unexpected error:\n" + ex.toString(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error");
        }
    }

    private void showMatrixInTable(double[][] A) {
        int n = A.length;
        String[] cols = new String[n];
        for (int j = 0; j < n; j++) cols[j] = "C" + (j+1);

        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (int i = 0; i < n; i++) {
            Object[] row = new Object[n];
            for (int j = 0; j < n; j++) row[j] = A[i][j];
            model.addRow(row);
        }
        matrixTable.setModel(model);

        resultTable.setModel(new DefaultTableModel());
    }

    private void onCompute() {
        if (matrix == null) {
            JOptionPane.showMessageDialog(frame, "Matrix not loaded. Load a file first.",
                    "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double[] X = MatrixProcessor.computeX(matrix, CUSTOM_EXCEPTION_THRESHOLD);
            showResultInTable(X);
            statusLabel.setText("Computed X successfully");
        } catch (MyArithmeticException ex) {
            // власне виключення
            JOptionPane.showMessageDialog(frame, "Custom arithmetic error:\n" + ex.getMessage(),
                    "Custom Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Custom error");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Unexpected error during compute:\n" + ex.toString(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error");
        }
    }

    private void showResultInTable(double[] X) {
        String[] cols = new String[] { "Row", "X(i)" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (int i = 0; i < X.length; i++) {
            Object[] row = new Object[] { i+1, X[i] };
            model.addRow(row);
        }
        resultTable.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MatrixGUI::new);
    }
}
