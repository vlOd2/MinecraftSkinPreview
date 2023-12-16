package net.minecraft.skinpreview.gfx;

public class Bitmap {
	public final int width;
	public final int height;
	public int[] pixels;

	public Bitmap(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	protected void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

	public void draw(Bitmap bitmap, int xPos, int yPos) {
		int x0 = xPos;
		int y0 = yPos;
		int x1 = xPos + bitmap.width;
		int y1 = yPos + bitmap.height;
		if (xPos < 0) {
			x0 = 0;
		}

		if (yPos < 0) {
			y0 = 0;
		}

		if (x1 > this.width) {
			x1 = this.width;
		}

		if (y1 > this.height) {
			y1 = this.height;
		}

		int[] inPixels = bitmap.pixels;

		for (int y = y0; y < y1; ++y) {
			int tp = y * this.width;
			int sp = (y - yPos) * bitmap.width - xPos;

			for (int x = x0; x < x1; ++x) {
				if (inPixels[sp + x] != 0) {
					this.pixels[tp + x] = inPixels[sp + x];
				}
			}
		}
	}

	public void color(Bitmap bitmap, int xPos, int yPos, int color) {
		int x0 = xPos;
		int y0 = yPos;
		int x1 = xPos + bitmap.width;
		int y1 = yPos + bitmap.height;
		if (xPos < 0) {
			x0 = 0;
		}

		if (yPos < 0) {
			y0 = 0;
		}

		if (x1 > this.width) {
			x1 = this.width;
		}

		if (y1 > this.height) {
			y1 = this.height;
		}

		int[] inPixels = bitmap.pixels;

		for (int y = y0; y < y1; ++y) {
			int tp = y * this.width;
			int sp = (y - yPos) * bitmap.width - xPos;

			for (int x = x0; x < x1; ++x) {
				if (inPixels[sp + x] != 0) {
					this.pixels[tp + x] = color;
				}
			}
		}

	}

	public void draw(Bitmap bitmap, int xPos, int yPos, int xx0, int yy0, int xx1, int yy1) {
		int x0 = xPos;
		int y0 = yPos;
		int x1 = xPos + xx1 - xx0;
		int y1 = yPos + yy1 - yy0;
		if (xx0 < 0) {
			x0 = xPos - xx0;
		}

		if (yy0 < 0) {
			y0 = yPos - yy0;
		}

		if (xx1 > bitmap.width) {
			x1 -= xx1 - bitmap.width;
		}

		if (yy1 > bitmap.height) {
			y1 -= yy1 - bitmap.height;
		}

		if (x0 < 0) {
			x0 = 0;
		}

		if (y0 < 0) {
			y0 = 0;
		}

		if (x1 > this.width) {
			x1 = this.width;
		}

		if (y1 > this.height) {
			y1 = this.height;
		}

		int[] inPixels = bitmap.pixels;

		for (int y = y0; y < y1; ++y) {
			int tp = y * this.width;
			int sp = (y - yPos + yy0) * bitmap.width - xPos + xx0;

			for (int x = x0; x < x1; ++x) {
				if (inPixels[sp + x] != 0) {
					this.pixels[tp + x] = inPixels[sp + x];
				}
			}
		}

	}

	public void fill(int x0, int y0, int x1, int y1, int col) {
		if (x0 < 0) {
			x0 = 0;
		}

		if (y0 < 0) {
			y0 = 0;
		}

		if (x1 > this.width) {
			x1 = this.width;
		}

		if (y1 > this.height) {
			y1 = this.height;
		}

		for (int y = y0; y < y1; ++y) {
			int tp = y * this.width + x0;

			for (int x = x0; x < x1; ++x) {
				this.pixels[tp + x] = col;
			}
		}
	}
}
