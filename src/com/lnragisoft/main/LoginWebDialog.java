package com.lnragisoft.main;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.AbstractStylePreview;
import com.alee.demo.api.example.AbstractStylePreviewExample;
import com.alee.demo.api.example.FeatureState;
import com.alee.demo.api.example.FeatureType;
import com.alee.demo.api.example.Preview;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.lnragisoft.swings.window.MainFrame;

public class LoginWebDialog extends AbstractStylePreviewExample {

	@NotNull
	@Override
	public String getId() {
		return "jdialog";
	}

	@NotNull
	@Override
	protected String getStyleFileName() {
		return "dialog";
	}

	@NotNull
	@Override
	public FeatureType getFeatureType() {
		return FeatureType.swing;
	}

	public LoginWebDialog() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List<Preview> createPreviews() {
		// TODO Auto-generated method stub
		return null;
	}

	protected class DialogPreview extends AbstractStylePreview {

		public DialogPreview(final String id, final FeatureState state, final StyleId styleId) {
			super(LoginWebDialog.this, id, state, styleId);
		}

		@Override
		protected List<? extends JComponent> createPreviewElements() {
			{
				final WebButton button = new WebButton(getExampleLanguagePrefix() + "show");
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent e) {
						final Window parent = CoreSwingUtils.getNonNullWindowAncestor(button);
						final String title = getExampleLanguagePrefix() + "content";
						final JDialog dialog = new JDialog(parent);
						UILanguageManager.registerComponent(dialog.getRootPane(), title);
						dialog.getRootPane().putClientProperty(StyleId.STYLE_PROPERTY, getStyleId());
						dialog.setIconImages(WebLookAndFeel.getImages());
						dialog.add(new WebLabel(title, WebLabel.CENTER));
						dialog.setSize(500, 400);
						dialog.setLocationRelativeTo(MainFrame.getInstance());
						dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
					}
				});
				return CollectionUtils.asList(button);
			}
		}
	}
}