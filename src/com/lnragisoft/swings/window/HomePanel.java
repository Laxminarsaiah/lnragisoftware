package com.lnragisoft.swings.window;

import java.awt.BorderLayout;

import com.alee.demo.skin.DemoIcons;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.overlay.WebProgressOverlay;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;

public class HomePanel extends WebPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HomePanel() {
		super();
		final WebPanel panel = new WebPanel(StyleId.panel);
		WebStyledLabel styledLabel = new WebStyledLabel(StyleId.label, WebStyledLabel.CENTER).setBoldFont();
		styledLabel.setText("Dummy Software.");
		styledLabel.setIcon(Icons.leaf);
		styledLabel.setFontSize(20);
		panel.add(styledLabel, BorderLayout.CENTER);
		add(panel);
	}
}
