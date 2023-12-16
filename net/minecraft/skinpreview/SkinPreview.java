package net.minecraft.skinpreview;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.minecraft.skinpreview.gfx.Bitmap3D;
import net.minecraft.skinpreview.gfx.ImageBitmap;
import net.minecraft.skinpreview.math.Matrix3;
import net.minecraft.skinpreview.math.Zombie;

public class SkinPreview extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	private Thread thread;
	private int width;
	private int height;
	private volatile boolean running = false;
	private ImageBitmap screenTexture;
	private Zombie zombie;
	private boolean paused;
	private int xOld;
	private int yOld;
	private long lastTime = System.nanoTime();
	private float time;
	private float yRot;
	private float xRot;

	public SkinPreview(int width, int height, String textureURL) {
		this.width = width;
		this.height = height;
		this.screenTexture = new ImageBitmap(160, 160);
		this.zombie = new Zombie(textureURL);
		
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SkinPreview.this.paused = !SkinPreview.this.paused;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				SkinPreview.this.xOld = e.getX();
				SkinPreview.this.yOld = e.getY();
			}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int xMove = e.getX();
				int yMove = e.getY();
				SkinPreview preview = SkinPreview.this;

				preview.yRot += (xMove - preview.xOld) / 80.0F;
				preview.xRot += (yMove - preview.yOld) / 80.0F;
				preview.xOld = xMove;
				preview.yOld = yMove;
				
				float max = 1.5707964F;
				if (preview.xRot < -max) {
					preview.xRot = -max;
				}

				if (preview.xRot > max) {
					preview.xRot = max;
				}
			}
		});
	}

	private void updateTime() {
		long currentTime = System.nanoTime();
		
		if (!this.paused) {
			this.time -= (currentTime - this.lastTime) / 1.0E9F;
		}
		
		this.lastTime = currentTime;
	}
	
	public void render(Bitmap3D screenTexture) {
		screenTexture.clearZBuffer();
		screenTexture.fill(0, 0, screenTexture.width, screenTexture.height, 0xA0B0E0);
		Matrix3 matrix = new Matrix3().translate(0.0, 0.0, 30.0).rotX(this.xRot).rotY(this.yRot);
		this.zombie.render(matrix, screenTexture, this.time);
	}

	@Override
	public void paint(Graphics g) {
	}

	@Override
	public void update(Graphics g) {
	}

	public synchronized void start() {
		if (this.thread != null) {
			return;
		}

		this.thread = new Thread(this);
		this.running = true;
		this.thread.start();
	}

	public synchronized void stop() {
		if (this.thread == null) {
			return;
		}
		this.running = false;

		try {
			this.thread.join();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		this.thread = null;
	}

	@Override
	public void run() {
		while (this.running) {
			this.updateTime();
			this.render(this.screenTexture);
			
			Graphics graphics = this.getGraphics();
			graphics.drawImage(this.screenTexture.getImage(), 0, 0, this.width, this.height, null);
			graphics.dispose();

			try {
				Thread.sleep(5L);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String textureURL = null;
		
		if (args.length >= 1) {
			textureURL = args[0];
		}
		
		if (textureURL == null) {
			JOptionPane.showMessageDialog(null, "Not enough arguments were specified!", "Error", 
					JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
			return;
		}
		
		SkinPreview instance = new SkinPreview(160, 160, textureURL);
		JFrame frame = new JFrame("Minecraft Skin Preview");
		frame.setLayout(new BorderLayout());
		frame.add(instance, "Center");
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		instance.start();
	}
}
