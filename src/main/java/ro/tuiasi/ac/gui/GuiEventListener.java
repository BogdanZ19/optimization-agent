package ro.tuiasi.ac.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ro.tuiasi.ac.model.OptimizationSuggestion;

import javax.swing.*;


import java.io.File;
import java.nio.file.Path;

public class GuiEventListener implements ActionListener {

    private final JTextField txtFolder;
    private final JButton btnBrowse;
    private final JButton btnStart;
    private final JButton btnAccept;
    private final JButton btnReject;
    private final JTextArea txtOriginalCode;
    private final JTextArea txtOptimizedCode;

    private OptimizationSuggestion currentSuggestion;

    public GuiEventListener(JTextField txtFolder, JButton btnBrowse, JButton btnStart, JButton btnAccept, JButton btnReject, JTextArea txtOriginalCode, JTextArea txtOptimizedCode) {
        this.txtFolder = txtFolder;
        this.btnBrowse = btnBrowse;
        this.btnStart = btnStart;
        this.btnAccept = btnAccept;
        this.btnReject = btnReject;
        this.txtOriginalCode = txtOriginalCode;
        this.txtOptimizedCode = txtOptimizedCode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnBrowse) {
            handleBrowse();
        } else if (source == btnStart) {
            handleStart();
        } else if (source == btnAccept) {
            handleAccept();
        } else if (source == btnReject) {
            handleReject();
        }
    }

    private void handleBrowse() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select file");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Java files (*.java)", "java"));

        int result = chooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            txtFolder.setText(selectedFile.getAbsolutePath());

            txtOriginalCode.setText("");
            txtOptimizedCode.setText("");

            currentSuggestion = null;

            btnStart.setEnabled(true);
            btnAccept.setEnabled(false);
            btnReject.setEnabled(false);
        }
    }

    private void handleStart() {
        String filePathText = txtFolder.getText();

        if (filePathText == null || filePathText.isBlank()) {
            JOptionPane.showMessageDialog(null, "Select a file", "Missing file", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Path filePath = Path.of(filePathText);

        btnStart.setEnabled(false);
        btnAccept.setEnabled(false);
        btnReject.setEnabled(false);

        txtOriginalCode.setText("Verifying backend...");
        txtOptimizedCode.setText("Preparing file...");

        SwingWorker<OptimizationSuggestion, Void> worker = new SwingWorker<>() {

            @Override
            protected OptimizationSuggestion doInBackground() throws Exception {
                BackendApiClient client = new BackendApiClient();

                if (!client.health()) {
                    throw new IllegalStateException("Backend not running. Restart spring application.");
                }

                return client.analyze(filePath);
            }

            @Override
            protected void done() {
                try {
                    currentSuggestion = get();

                    if (currentSuggestion == null) {
                        JOptionPane.showMessageDialog(null, "Backend not running. Restart spring application.", "Empty response", JOptionPane.WARNING_MESSAGE);

                        txtOriginalCode.setText("");
                        txtOptimizedCode.setText("");
                        return;
                    }

                    txtOriginalCode.setText(currentSuggestion.originalCode());
                    txtOptimizedCode.setText(currentSuggestion.optimizedCode());

                    btnAccept.setEnabled(true);
                    btnReject.setEnabled(true);

                } catch (Exception ex) {
                    //JOptionPane.showMessageDialog(null, "Error in analyze request: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error in analyze request: " + ex.getMessage());
                    txtOriginalCode.setText("");
                    txtOptimizedCode.setText("");

                } finally {
                    btnStart.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void handleAccept() {
        if (currentSuggestion == null) {
            JOptionPane.showMessageDialog(null, "No suggestion to accept", "Missing suggestion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnAccept.setEnabled(false);
        btnReject.setEnabled(false);

        SwingWorker<String, Void> worker = new SwingWorker<>() {

            @Override
            protected String doInBackground() throws Exception {
                BackendApiClient client = new BackendApiClient();

                if (!client.health()) {
                    throw new IllegalStateException("Backend not running. Restart spring application.");
                }

                return client.accept(currentSuggestion);
            }

            @Override
            protected void done() {
                try {
                    String response = get();

                    JOptionPane.showMessageDialog(null, response, "Accepted", JOptionPane.INFORMATION_MESSAGE);

                    currentSuggestion = null;

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error in accepting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

                    btnAccept.setEnabled(true);
                    btnReject.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void handleReject() {
        if (currentSuggestion == null) {
            JOptionPane.showMessageDialog(null, "No suggestion to reject.", "Missing suggestion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnAccept.setEnabled(false);
        btnReject.setEnabled(false);

        SwingWorker<String, Void> worker = new SwingWorker<>() {

            @Override
            protected String doInBackground() throws Exception {
                BackendApiClient client = new BackendApiClient();

                if (!client.health()) {
                    throw new IllegalStateException("Backend not running. Restart spring application.");
                }

                return client.reject(currentSuggestion);
            }

            @Override
            protected void done() {
                try {
                    String response = get();

                    JOptionPane.showMessageDialog(null, response, "Rejected", JOptionPane.INFORMATION_MESSAGE);

                    currentSuggestion = null;
                    txtOptimizedCode.setText("");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error while rejecting: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);

                    btnAccept.setEnabled(true);
                    btnReject.setEnabled(true);
                }
            }
        };

        worker.execute();
    }
}
