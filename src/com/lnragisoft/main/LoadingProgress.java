package com.lnragisoft.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.WindowConstants;

import com.alee.api.annotations.NotNull;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alee.laf.window.WebFrame;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.skin.dark.WebDarkSkin;
import com.alee.utils.CoreSwingUtils;
import com.lnragisoft.swings.window.MainFrame;
import com.lnragisoft.utils.Icons;

public final class LoadingProgress extends WebFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7378968673152098338L;
	private static LoadingProgress instance;
	public static WebLabel erroMsg;
	public static WebPanel mainPanel, westPanel, eastPanel, northPanel, southPanel, centerPanel;

	public LoadingProgress() {
		initialize(StyleId.frameDecorated, "Login");
		setTitle("Login");
		display();
	}

	@NotNull
	public static LoadingProgress getInstance() {
		if (instance == null) {
			instance = new LoadingProgress();
		}
		return instance;
	}

	protected void display() {
//		getRootPane().putClientProperty(StyleId.STYLE_PROPERTY, getStyleId());
		setAlwaysOnTop(true);
		setBounds(380, 190, 600, 400);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		setIconImages(WebLookAndFeel.getImages());
		getContentPane().add(addLoginFields());
	}

	private Component addLoginFields() {
		mainPanel = new WebPanel();
		westPanel = new WebPanel();
		westPanel.setPreferredSize(30, 0);
		westPanel.setAlignmentX(LEFT_ALIGNMENT);
//		westPanel.setBorder(BorderFactory.createTitledBorder("westPanel"));

		eastPanel = new WebPanel();
		eastPanel.setPreferredSize(120, 0);
		eastPanel.setAlignmentX(RIGHT_ALIGNMENT);
//		eastPanel.setBorder(BorderFactory.createTitledBorder("eastPanel"));

		northPanel = new WebPanel();
		northPanel.setPreferredSize(0, 70);
		northPanel.setAlignmentY(TOP_ALIGNMENT);

		southPanel = new WebPanel();
		southPanel.setPreferredSize(0, 30);
		southPanel.setBackground(new Color(36,33,33));
		southPanel.setAlignmentY(BOTTOM_ALIGNMENT);
		southPanel.setBorder(BorderFactory.createEtchedBorder());

		centerPanel = new WebPanel();
		centerPanel.setLayout(new GridLayout(7, 7));
		centerPanel.setAlignmentY(CENTER_ALIGNMENT);
//		centerPanel.setBorder(BorderFactory.createTitledBorder("centerPanel"));

		WebPanel userPanel = new WebPanel();
		userPanel.setLayout(new GridLayout(1, 2));
		final WebTextField username = new WebTextField("", 20);
		username.setStyleId(StyleId.textfield);
		WebLabel userLabel = new WebLabel("                       Username:").setBoldFont();
		userLabel.setLabelFor(username);
		userPanel.add(userLabel);
		userPanel.add(username);

		WebPanel passwordPanel = new WebPanel();
		passwordPanel.setLayout(new GridLayout(1, 2));
		final WebPasswordField password = new WebPasswordField("", 20);
		password.setStyleId(StyleId.textfield);
		WebLabel pwdLabel = new WebLabel("                        Password:").setBoldFont();
		pwdLabel.setLabelFor(password);
		passwordPanel.add(pwdLabel);
		passwordPanel.add(password);

		centerPanel.add(userPanel);
		centerPanel.add(passwordPanel);

		final WebButton cancel = new WebButton("Cancel");
		cancel.setStyleId(StyleId.buttonHover);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		final WebButton login = new WebButton("Login");
		login.setStyleId(StyleId.buttonHover);
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				erroMsg.setText("");
				erroMsg.setIcon(null);
				if (validateUser(username.getText(), password.getText())) {
					new LoadingThread().start();
				}
			}

		});
		erroMsg = new WebLabel().setBoldFont();
		southPanel.add(new GroupPanel(GroupingType.fillFirst, cancel, login), BorderLayout.EAST);
		southPanel.add(erroMsg, BorderLayout.CENTER);

		mainPanel.add(westPanel, BorderLayout.WEST);
		mainPanel.add(eastPanel, BorderLayout.EAST);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);

		return mainPanel;
	}

	private boolean validateUser(String username, String password) {
		if (!username.equals("admin") || !password.equals("admin")) {
			erroMsg.setIcon(Icons.error_sm);
			erroMsg.setText("<html><font color=red>Invalid username/password</font></html>");
			return false;
		} else {
			return true;
		}
	}

	public static void main(String[] args) {
		CoreSwingUtils.invokeLater(new Runnable() {
			@Override
			public void run() {
				WebLookAndFeel.install();
				StyleManager.setSkin(new WebDarkSkin());
				LoadingProgress.getInstance();
			}
		});
	}

	class LoadingThread extends Thread {

		public LoadingThread() {
			super();
		}

		@Override
		public void run() {
			try {
				erroMsg.setIcon(Icons.spinner);
				String[] task = { "Please wait...", "Loading application....", "Opening window...." };
				for (int i = 0; i < task.length; i++) {
					erroMsg.setText(task[i]);
					Thread.sleep(1000);
				}
				CoreSwingUtils.invokeLater(new Runnable() {
					public void run() {
						new MainFrame();
						dispose();
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}
}
