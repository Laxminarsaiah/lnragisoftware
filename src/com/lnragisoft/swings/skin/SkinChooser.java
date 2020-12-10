package com.lnragisoft.swings.skin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JList;

import com.alee.api.annotations.NotNull;
import com.alee.demo.DemoApplication;
import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.demo.ui.tools.SkinChooserTool;
import com.alee.laf.combobox.ComboBoxCellParameters;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.combobox.WebComboBoxRenderer;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.skin.light.WebLightSkin;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.lnragisoft.swings.window.MainFrame;

public final class SkinChooser extends WebPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs new {@link SkinChooserTool}.
	 */

	public static ArrayList<Skin> skins = CollectionUtils.asList(StyleManager.getSkin(), new WebLightSkin());;

	public SkinChooser() {
		super(StyleId.panelTransparent, new BorderLayout(0, 0));

		// Skin chooser combobox
		final WebComboBox chooser = new WebComboBox(DemoStyles.toolCombobox, skins);
		chooser.setLanguage("demo.tool.skin");
		chooser.setSelectedItem(StyleManager.getSkin());
		chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(@NotNull final ActionEvent e) {
				// Executing later to allow combobox popup to close first
				CoreSwingUtils.invokeLater(new Runnable() {
					@Override
					public void run() {
						final Skin skin = (Skin) chooser.getSelectedItem();
						if (skin != null) {
							StyleManager.setSkin(skin);
						}
					}
				});
			}
		});
		add(chooser, BorderLayout.CENTER);
	}
}
