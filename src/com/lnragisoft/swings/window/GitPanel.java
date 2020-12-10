package com.lnragisoft.swings.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;
import com.alee.managers.style.StyleId;
import com.lnragisoft.utils.AlertsManager;
import com.lnragisoft.utils.Icons;

public class GitPanel extends WebPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4726417526328556740L;
	public static WebPanel mainPanel, centerPanel, eastPanel, westPanel;
	public static WebButton pull, status;

	public GitPanel() {
//		super();
		mainPanel = new WebPanel();
		
		westPanel = new WebPanel();
		westPanel.setPreferredSize(120, 0);
		westPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		eastPanel = new WebPanel();
		eastPanel.setPreferredSize(120, 0);
		eastPanel.setAlignmentX(RIGHT_ALIGNMENT);
		
		centerPanel = new WebPanel();
		WebScrollPane areaScroll = new WebScrollPane(centerPanel);
		areaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		centerPanel.add(addDynamicPanel());
		mainPanel.add(areaScroll);
		mainPanel.add(eastPanel,BorderLayout.EAST);
		mainPanel.add(westPanel,BorderLayout.WEST);
		add(mainPanel);
	}

	private Component addDynamicPanel() {
		List<String> repos = loadRepolist();
		List<Component> compList = new ArrayList<>();
		WebPanel gitPanel = new WebPanel();
		gitPanel.setLayout(new GridLayout(8, 1));
		for (String repo : repos) {
			WebPanel panel = new WebPanel(StyleId.panelTransparent);
			panel.setPreferredHeight(150);
			WebStyledLabel styleLabel = new WebStyledLabel(repo, Icons.git).setBoldFont();
			WebStyledLabel statusLabel = new WebStyledLabel();
			panel.add(styleLabel, BorderLayout.NORTH);
			WebTextArea outputTextArea = new WebTextArea(8, 40);
			outputTextArea.setWrapStyleWord(true);
			DefaultCaret caret = (DefaultCaret) outputTextArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			outputTextArea.setEditable(false);
			outputTextArea.setOpaque(true);
			WebScrollPane areaScroll = new WebScrollPane(outputTextArea);
			areaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			pull = new WebButton(StyleId.buttonHover);
			pull.setIcon(Icons.fetch);
			pull.setToolTipText("Command: git pull");
			pull.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String[] command = { "CMD", "/C", "git pull" };
					GitThread gt = new GitThread(repo, command, outputTextArea, statusLabel);
					gt.start();
				}
			});
			status = new WebButton(StyleId.buttonHover);
			status.setIcon(Icons.info);
			status.setToolTipText("Command: git status");
			status.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String[] command = { "CMD", "/C", "git status" };
					GitThread gt = new GitThread(repo, command, outputTextArea, statusLabel);
					gt.start();
				}
			});
			GroupPanel gp = new GroupPanel(GroupingType.fillFirst, styleLabel, statusLabel, pull, status);
			panel.add(gp,BorderLayout.NORTH);
			panel.add(areaScroll, BorderLayout.CENTER);
			compList.add(panel);
		}
		gitPanel.add(compList);
		return gitPanel;
	}

	private List<String> loadRepolist() {
		File file = new File("D:\\.bambkins\\git\\filepaths.txt");
		BufferedReader br;
		List<String> repos = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null) {
				repos.add(st);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return repos;
	}

	class GitThread extends Thread {
		private String dest;
		private String[] command;
		private WebTextArea outputTextArea;
		private WebStyledLabel statusLabel;
		String line;

		public GitThread(String dest, String[] command, WebTextArea outputTextArea, WebStyledLabel statusLabel) {
			super();
			this.dest = dest;
			this.command = command;
			this.outputTextArea = outputTextArea;
			this.statusLabel = statusLabel;
		}

		public void run() {
			outputTextArea.setText("");
			AlertsManager.info(command[2] + ":" + dest);
			statusLabel.setIcon(Icons.loader);
			statusLabel.setText("Running...");
			ProcessBuilder probuilder = new ProcessBuilder(command);
			probuilder.directory(new File(dest));
			Process process;
			try {
				process = probuilder.start();
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				while ((line = br.readLine()) != null) {
					outputTextArea.append(line);
					outputTextArea.append("\n");
				}
				statusLabel.setIcon(null);
				statusLabel.setText("");
				AlertsManager.info("Done");
			} catch (IOException e) {
				statusLabel.setIcon(null);
				statusLabel.setText("");
				AlertsManager.error("something went wrong!!!");
				e.printStackTrace();
			}
		}

	}

}
