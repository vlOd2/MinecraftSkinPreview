package net.minecraft.skinpreview;

import java.awt.BorderLayout;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

public class SkinPreviewApplet extends JApplet {
	private static final long serialVersionUID = 1L;
	private SkinPreview instance;

	@Override
	public void init() {
		if (this.getParameter("width") == null || 
				this.getParameter("height") == null ||
				this.getParameter("textureURL") == null) {
			JOptionPane.showMessageDialog(null, "Applet configuration is invalid!", 
					"Error", JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
			return;
		}
		
		int width = Integer.valueOf(this.getParameter("width"));
		int height = Integer.valueOf(this.getParameter("height"));
		String textureURL = this.getParameter("textureURL");
		System.out.printf("Resolution: %dx%d\n", width, height);
		System.out.printf("Texture URL: %s\n", textureURL);
		
		this.instance = new SkinPreview(width, height, textureURL);
		this.setLayout(new BorderLayout());
		this.add(this.instance, "Center");
	}

	@Override
	public void start() {
		if (this.instance == null) {
			return;
		}
		this.instance.start();
	}

	@Override
	public void stop() {
		if (this.instance == null) {
			return;
		}
		this.instance.stop();
	}
}
