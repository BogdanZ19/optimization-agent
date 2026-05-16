package ro.tuiasi.ac.gui;

import java.awt.*;
import java.io.File;
import javax.swing.*;


public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtFolder;
    private JButton btnBrowse;
    private JButton btnStart;
    private JButton btnAccept;
    private JButton btnReject;
    private JTextArea txtOriginalCode;
    private JTextArea txtOptimizedCode;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
    
    public MainWindow() {
        initialize();
    }

    private void initialize() {
        setTitle("Java Code Optimizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 650));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        setContentPane(mainPanel);

        // ================= TOP PANEL =================
        JPanel topPanel = new JPanel(new GridBagLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);

        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.insets = new Insets(0, 0, 0, 15);
        gbcTop.gridy = 0;
        gbcTop.fill = GridBagConstraints.HORIZONTAL;

        txtFolder = new JTextField();
        txtFolder.setEditable(false);

        gbcTop.gridx = 0;
        gbcTop.weightx = 1.0;
        topPanel.add(txtFolder, gbcTop);

        btnBrowse = new JButton("Browse");
        gbcTop.gridx = 1;
        gbcTop.weightx = 0;
        topPanel.add(btnBrowse, gbcTop);

        // ================= CENTER PANEL =================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblOriginal = new JLabel("Cod original", SwingConstants.CENTER);
        JLabel lblOptimized = new JLabel("Cod optimizat", SwingConstants.CENTER);

        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        gbc.gridx = 0;
        gbc.weightx = 0.45;
        centerPanel.add(lblOriginal, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.45;
        centerPanel.add(lblOptimized, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.10;
        centerPanel.add(new JLabel(""), gbc);

        txtOriginalCode = new JTextArea();
        txtOriginalCode.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtOriginalCode.setEditable(false);

        txtOptimizedCode = new JTextArea();
        txtOptimizedCode.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtOptimizedCode.setEditable(false);

        JScrollPane scrollOriginal = new JScrollPane(txtOriginalCode);
        JScrollPane scrollOptimized = new JScrollPane(txtOptimizedCode);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.weightx = 0.45;
        centerPanel.add(scrollOriginal, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.45;
        centerPanel.add(scrollOptimized, gbc);

        // ================= RIGHT BUTTON PANEL =================
        JPanel rightPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 2;
        gbc.weightx = 0.10;
        centerPanel.add(rightPanel, gbc);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = new Insets(15, 10, 15, 10);
        gbcRight.fill = GridBagConstraints.BOTH;
        gbcRight.gridx = 0;

        btnStart = new JButton("START");
        btnStart.setFont(new Font("Tahoma", Font.BOLD, 26));
        btnStart.setEnabled(false);

        gbcRight.gridy = 0;
        gbcRight.weighty = 0.35;
        rightPanel.add(btnStart, gbcRight);

        JPanel spacer = new JPanel();
        gbcRight.gridy = 1;
        gbcRight.weighty = 0.30;
        rightPanel.add(spacer, gbcRight);

        btnAccept = new JButton("Accept");
        btnAccept.setEnabled(false);

        gbcRight.gridy = 2;
        gbcRight.weighty = 0.15;
        rightPanel.add(btnAccept, gbcRight);

        btnReject = new JButton("Reject");
        btnReject.setEnabled(false);

        gbcRight.gridy = 3;
        gbcRight.weighty = 0.15;
        rightPanel.add(btnReject, gbcRight);

        // ================= ACTIONS =================
        GuiEventListener eventListener = new GuiEventListener(txtFolder, btnBrowse, btnStart, btnAccept, btnReject, txtOriginalCode, txtOptimizedCode);
        btnBrowse.addActionListener(eventListener);
        btnStart.addActionListener(eventListener);
        btnAccept.addActionListener(eventListener);
        btnReject.addActionListener(eventListener);
    }

//    private void browseFolder() {
//        JFileChooser chooser = new JFileChooser();
//        chooser.setDialogTitle("Selectează folderul cu fișiere .java");
//        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//
//        int result = chooser.showOpenDialog(this);
//
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File folder = chooser.getSelectedFile();
//            txtFolder.setText(folder.getAbsolutePath());
//
//            // dupa selectarea folderului, deblocam START
//            btnStart.setEnabled(true);
//        }
//    }
}