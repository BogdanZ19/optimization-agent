package ro.tuiasi.ac.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiEventListener implements ActionListener {
	JTextField txtFolder;
	JButton btnBrowse;
	JButton btnStart;
	JButton btnAccept;
	JButton btnReject;
	JTextArea txtOriginalCode;
	JTextArea txtOptimizedCode;

	public GuiEventListener(
			JTextField txtFolder, JButton btnBrowse, JButton btnStart,
			JButton btnAccept, JButton btnReject, JTextArea txtOriginalCode,
			JTextArea txtOptimizedCode) {
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
		// TODO Auto-generated method stub

	}

}
