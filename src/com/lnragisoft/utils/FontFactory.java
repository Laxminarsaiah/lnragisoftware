package com.lnragisoft.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontFactory {

	public static Font setFont() {
		InputStream is = FontFactory.class.getResourceAsStream("/MONACO.TTF");
		Font font = null;
		Font sizedFont = null;
		try {
			font = Font.createFont(Font.PLAIN, is);
			sizedFont = font.deriveFont(13f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		return sizedFont;
	}
}
