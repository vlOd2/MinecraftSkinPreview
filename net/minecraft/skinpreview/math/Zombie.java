package net.minecraft.skinpreview.math;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import net.minecraft.skinpreview.gfx.Bitmap;
import net.minecraft.skinpreview.gfx.Bitmap3D;
import net.minecraft.skinpreview.gfx.ImageBitmap;

public class Zombie {
	public Model head = new Model(0, 0);
	public Model hair;
	public Model iHair;
	public Model body;
	public Model arm0;
	public Model arm1;
	public Model leg0;
	public Model leg1;
	public Bitmap texture;
	public double x;
	public double y;
	public double z;
	public double rot;
	public double size = 1.0;
	public double timeOffs;
	public double speed = 0.800000011920929;
	public double rotA;
	private boolean hasHair = false;

	public Zombie(String textureURL) {
		this.head.addBox(-4.0, -8.0, -4.0, 8, 8, 8, 0.0F);
		this.hair = new Model(32, 0);
		this.hair.addBox(-4.0, -8.0, -4.0, 8, 8, 8, 0.5F);
		this.iHair = new Model(32, 0);
		this.iHair.invert = true;
		this.iHair.addBox(-4.0, -8.0, -4.0, 8, 8, 8, 0.5F);
		this.body = new Model(16, 16);
		this.body.addBox(-4.0, 0.0, -2.0, 8, 12, 4, 0.0F);
		this.arm0 = new Model(40, 16);
		this.arm0.addBox(-3.0, -2.0, -2.0, 4, 12, 4, 0.0F);
		this.arm0.setPos(-5.0, 2.0, 0.0);
		this.arm1 = new Model(40, 16);
		this.arm1.mirror = true;
		this.arm1.addBox(-1.0, -2.0, -2.0, 4, 12, 4, 0.0F);
		this.arm1.setPos(5.0, 2.0, 0.0);
		this.leg0 = new Model(0, 16);
		this.leg0.addBox(-2.0, 0.0, -2.0, 4, 12, 4, 0.0F);
		this.leg0.setPos(-2.0, 12.0, 0.0);
		this.leg1 = new Model(0, 16);
		this.leg1.mirror = true;
		this.leg1.addBox(-2.0, 0.0, -2.0, 4, 12, 4, 0.0F);
		this.leg1.setPos(2.0, 12.0, 0.0);
		this.loadTexture(textureURL);
	}

	public void loadFallbackTexture() {
		this.texture = ImageBitmap.load("/char.png");
	}
	
	public void loadTexture(String textureURL) {
		new Thread("Skin-Fetcher") {
			@Override
			public void run() {
				System.out.println("Loading texture from " + textureURL);
				HttpURLConnection httpConnection = null;

				try {
					httpConnection = (HttpURLConnection) new URL(textureURL).openConnection();
					httpConnection.setDoInput(true);
					httpConnection.setDoOutput(false);
					httpConnection.connect();
					int responseCode = httpConnection.getResponseCode();
					
					if (responseCode != 200) {
						throw new Exception("Server response code was not 200 but " + responseCode);
					}

					InputStream inputStream = httpConnection.getInputStream();
					Bitmap newTexture = ImageBitmap.load(ImageIO.read(inputStream));
					Zombie.this.texture = newTexture;
					Zombie.this.checkHair();
				} catch (Exception ex) {
					System.out.printf("Unable to load the texture from %s:\n", textureURL);
					ex.printStackTrace();
				} finally {
					if (Zombie.this.texture == null) {
						Zombie.this.loadFallbackTexture();
					}
					httpConnection.disconnect();
				}
			}
		}.start();
	}

	private void checkHair() {
		for (int x = 32; x < 64; ++x) {
			for (int y = 0; y < 16; ++y) {
				int a = this.texture.pixels[x + y * 64] >> 24 & 255;
				if (a < 128) {
					this.hasHair = true;
					return;
				}
			}
		}
	}

	public void render(Matrix3 matrix, Bitmap3D screenTexture, double time) {
		if (this.texture == null) {
			return;
		}
		
		matrix = matrix.clone();
		time = time * 10.0 * this.speed + this.timeOffs;
		this.y = -Math.abs(Math.sin(time * 0.6662)) * 5.0 - 24.0 + 20.0;
		matrix = matrix.translate(this.x, this.y, this.z).rotY(this.rot).scale(this.size, this.size, this.size);
		this.head.yRot = Math.sin(time * 0.23) * 1.0;
		this.head.xRot = Math.sin(time * 0.1) * 0.8;
		this.iHair.yRot = this.hair.yRot = this.head.yRot;
		this.iHair.xRot = this.hair.xRot = this.head.xRot;
		this.arm0.xRot = Math.sin(time * 0.6662 + Math.PI) * 2.0;
		this.arm0.zRot = (Math.sin(time * 0.2312) + 1.0) * 1.0;
		this.arm1.xRot = Math.sin(time * 0.6662) * 2.0;
		this.arm1.zRot = (Math.sin(time * 0.2812) - 1.0) * 1.0;
		this.leg0.xRot = Math.sin(time * 0.6662) * 1.4;
		this.leg1.xRot = Math.sin(time * 0.6662 + Math.PI) * 1.4;
		this.head.render(matrix, this.texture, screenTexture);
		
		if (this.hasHair) {
			this.hair.render(matrix, this.texture, screenTexture);
			this.iHair.render(matrix, this.texture, screenTexture);
		}

		this.body.render(matrix, this.texture, screenTexture);
		this.arm0.render(matrix, this.texture, screenTexture);
		this.arm1.render(matrix, this.texture, screenTexture);
		this.leg0.render(matrix, this.texture, screenTexture);
		this.leg1.render(matrix, this.texture, screenTexture);
	}
}
