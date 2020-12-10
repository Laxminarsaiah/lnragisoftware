package com.lnragisoft.swings.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.text.WebTextField;
import com.alee.managers.style.StyleId;
import com.alee.utils.swing.DialogOptions;
import com.lnragisoft.utils.AlertsManager;
import com.lnragisoft.utils.FontFactory;
import com.lnragisoft.utils.Icons;

public class BuildAntProjectPanel extends WebPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	WebPanel northPanel;
	WebTextField target;
	WebButton submit, browse;
	WebFileChooser chooser;
	String choosertitle;
	JProgressBar progressBar;
	String line;
	WebTextArea textArea;
	WebLabel statusBarLabel, targetLabel;
	String mavenBuildTitleTxt = "<html><font>Select BUILD.XML Location:</font></html>";
	public boolean buildStatus = false;

	public BuildAntProjectPanel() {
		statusBarLabel = new WebLabel();
		textArea = new WebTextArea(30, 146);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		textArea.setForeground(new Color(0, 0, 0));
		textArea.setFont(FontFactory.setFont());

		WebScrollPane areaScroll = new WebScrollPane(textArea);
		areaScroll.setPreferredSize(new Dimension(250, 250));
		add(showMavenBuildFieldGrid(), BorderLayout.NORTH);
		add(areaScroll);
		add(addStatusBarPanel(), BorderLayout.SOUTH);
	}

	private Component addStatusBarPanel() {
		northPanel = new WebPanel();
		northPanel.setFont(FontFactory.setFont());
		northPanel.setBackground(Color.white);
		northPanel.setMargin(new Insets(3, 3, 3, 3));
		statusBarLabel = new WebLabel("Dummy Softwares", WebLabel.LEADING).setBoldFont();
		northPanel.add(statusBarLabel);
		return northPanel;
	}

	private Component addJProgressPanel(boolean bvalue) {
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(bvalue);
		progressBar.setPreferredSize(new Dimension(80, 12));
		progressBar.setBackground(Color.WHITE);
		progressBar.setForeground(new Color(34, 139, 34));
		progressBar.setBorderPainted(true);
		return progressBar;
	}

	private Component showMavenBuildFieldGrid() {
		WebPanel targetPanel = new WebPanel();
		targetPanel.setBackground(Color.WHITE);
		targetPanel.setLayout(new GridLayout(2, 2));
		target = new WebTextField(80);
		targetLabel = new WebLabel(mavenBuildTitleTxt).setBoldFont();
		targetLabel.setLabelFor(target);
		targetPanel.add(targetLabel);

		browse = new WebButton("Browse").setBoldFont();
		browse.setStyleId(StyleId.buttonHover);
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WebDirectoryChooser chooser = new WebDirectoryChooser(null);
				chooser.setVisible(true);
				if (chooser.getResult() == DialogOptions.OK_OPTION) {
					final File file = chooser.getSelectedDirectory();
					target.setText(file.getPath());
				}
			}
		});
		submit = new WebButton("Submit").setBoldFont();
		submit.setStyleId(StyleId.buttonHover);
		submit.setToolTipText("Starts Maven Project generation ");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cleanAllAntProject();
			}
		});

		targetPanel.add(new GroupPanel(target, browse, submit));
		GroupPanel gp = new GroupPanel(false, targetPanel);
		gp.setBackground(Color.WHITE);
		gp.setLayout(new GridLayout(1, 10));
		return gp;
	}

	protected void cleanAllAntProject() {
		String dest = target.getText();
		if (dest.equals("")) {
			showErrorMessage(targetLabel, "This field is required.");
			return;
		} else {
			hideErrorMessage(targetLabel, mavenBuildTitleTxt);
		}
		String[] command = { "CMD", "/C", "ant clean all" };
		TextAreaThread th = new TextAreaThread(null, null, null, dest, command);
		th.start();
	}

	private void hideErrorMessage(WebLabel label, String message) {
		label.setBorder(null);
		label.setIcon(null);
		label.setText(message);
	}

	private void showErrorMessage(WebLabel label, String message) {
		Border border = BorderFactory.createLineBorder(Color.RED, 1);
		label.setIcon(Icons.error);
		label.setBorder(border);
		label.setText("<html><font color=red>" + message + "</font></html>");
		label.setBackground(new Color(255, 218, 185));
	}

	class TextAreaThread extends Thread {
		private String dest;
		private String[] command;
		int completedStatus;
		int size;

		public TextAreaThread(String grpId, String afactId, String template, String dest, String[] command) {
			super();
			this.dest = dest;
			this.command = command;
		}

		public void run() {
			northPanel.add(addJProgressPanel(true), BorderLayout.EAST);
			textArea.setText("");
			AlertsManager.info("Building Ant project...");
			statusBarLabel.setIcon(Icons.loader);
			buildStatus = false;
			textArea.append("BREATHE IN....., BREATHE OUT......\n\n");
			ProcessBuilder probuilder = new ProcessBuilder(command);
			probuilder.directory(new File(dest));
			Process process;
			try {
				process = probuilder.start();
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				boolean finishFlag = false;
				statusBarLabel.setText("Running...");
				while ((line = br.readLine()) != null) {
					finishFlag = false;
					if (line.toString().indexOf("BUILD SUCCESSFUL") >= 0
							|| line.toString().indexOf("BUILD SUCCESS") >= 0) {
						buildStatus = true;
					}
					if (line.toString().indexOf("Progress") >= 0) {
						statusBarLabel.setText(line);
					} else {
						textArea.append(line);
						textArea.append("\n");
					}
					finishFlag = true;
				}
				if (finishFlag == true) {
					northPanel.remove(progressBar);
					if (buildStatus) {
						statusBarLabel.setIcon(Icons.success);
						AlertsManager.success("Ant project build successfull");
						statusBarLabel.setText("<html><font color=green>BUILD SUCCESS.</font></html>");
					} else {
						statusBarLabel.setIcon(Icons.error);
						;
						AlertsManager.error("Ant project build failed");
						statusBarLabel.setText("<html><font color=red>BUILD FAILURE.</font></html>");
					}
				}
			} catch (IOException e) {
				statusBarLabel.setIcon(null);
				statusBarLabel.setText("");
				northPanel.remove(progressBar);
				e.printStackTrace();
			}
		}

	}
}
