package com.lnragisoft.swings.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.alee.api.annotations.NotNull;
import com.alee.api.data.BoxOrientation;
import com.alee.api.data.CompassDirection;
import com.alee.api.jdk.SerializableSupplier;
import com.alee.demo.content.ExamplesManager;
import com.alee.demo.skin.DemoAdaptiveExtension;
import com.alee.demo.skin.DemoDarkSkinExtension;
import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoLightSkinExtension;
import com.alee.demo.skin.DemoStyles;
import com.alee.demo.skin.decoration.FeatureStateBackground;
import com.alee.extended.behavior.ComponentResizeBehavior;
import com.alee.extended.canvas.WebCanvas;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.link.UrlLinkAction;
import com.alee.extended.link.WebLink;
import com.alee.extended.memorybar.WebMemoryBar;
import com.alee.extended.overlay.AlignedOverlay;
import com.alee.extended.overlay.WebOverlay;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.rootpane.WindowState;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.laf.window.WebFrame;
import com.alee.managers.icon.Icons;
import com.alee.managers.language.LanguageLocaleUpdater;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.skin.dark.WebDarkSkin;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SystemUtils;
import com.alee.utils.XmlUtils;
import com.lnragisoft.swings.skin.SkinChooser;
import com.lnragisoft.utils.AlertsManager;

public final class MainFrame extends WebFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static WebButton putty;

	/**
	 * Available application and example skins.
	 */

	private static MainFrame instance;

	@NotNull
	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}

	public MainFrame() {
//		super();
//		initialize(StyleId.frame, "Some");
		setIconImages(WebLookAndFeel.getImages());
		updateTitle();
		initializeToolBar();
		initializeStatus();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		registerSettings(new Configuration<WindowState>("application", new SerializableSupplier<WindowState>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public WindowState get() {
				return new WindowState(new Dimension(1200, 820));
			}
		}));
		initializeFrame();
	}

	private Component addPuttyWindow() {
		putty = new WebButton("Open Putty");
		putty.setToolTip("Open Putty Session");
		putty.setStyleId(StyleId.buttonHover);
		putty.setIcon(Icons.external);
		putty.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				new PuttyWindow();
				JOptionPane.showMessageDialog(getParent(), "Coming soon....", "Putty",JOptionPane.INFORMATION_MESSAGE, null);
			}
		});
		return putty;
	}

	public void updateTitle() {
		final StringBuilder title = new StringBuilder();
		title.append("lnragisoft ");
		setTitle(title.toString());
	}

	public void display() {
		setVisible(true);
	}

	private void initializeToolBar() {
		final WebToolBar toolBar = new WebToolBar(StyleId.toolbarAttachedNorth);
		toolBar.setFloatable(false);
		toolBar.add(new SkinChooser());
		toolBar.addToEnd(addPuttyWindow());
		add(toolBar, BorderLayout.NORTH);
		getContentPane().add(addTabbedPane(), BorderLayout.CENTER);
	}

	private Component addTabbedPane() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("  Home  ", Icons.folderHome, new HomePanel());
		tabbedPane.addTab("  New Maven Project  ", Icons.folderNew, new NewMvnProject());
		tabbedPane.addTab("  Build Maven Project  ", Icons.folderOpen, new BuildMavenProjectPanel());
		tabbedPane.addTab("  Build Ant Project  ", Icons.folderUp, new BuildAntProjectPanel());
//		tabbedPane.addTab("  AGM  ", Icons.computer, new AGMPanel());
		tabbedPane.addTab("  Orchestrator  ", Icons.computer, null);
		tabbedPane.addTab("  Git  ", Icons.folderDesktop, new GitPanel());
		return tabbedPane;
	}

	public static void main(String[] args) {
		initializeFrame();
	}

	private static void initializeFrame() {
		CoreSwingUtils.invokeLater(new Runnable() {
			@Override
			public void run() {
				XmlUtils.processAnnotations(FeatureStateBackground.class);
				WebLookAndFeel.install();
				StyleManager.addExtensions(new DemoAdaptiveExtension(), new DemoLightSkinExtension(),
						new DemoDarkSkinExtension());
				StyleManager.setSkin(new WebDarkSkin());
				MainFrame.getInstance().display();

			}
		});
	}

	private void initializeStatus() {
		final WebStatusBar statusBar = new WebStatusBar();
		final WebStyledLabel jvm = new WebStyledLabel(DemoIcons.java16, SwingConstants.CENTER);
		jvm.setText("Running on: "+SystemUtils.getJavaVersion().toString());
		statusBar.addToEnd(jvm);
		add(statusBar, BorderLayout.SOUTH);
	}
}
