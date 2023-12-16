package net.minecraft.skinpreview.gfx;

import java.util.Arrays;

import net.minecraft.skinpreview.math.Polygon;
import net.minecraft.skinpreview.math.Vertex;

public class Bitmap3D extends Bitmap {
	private double[] zBuffer;
	private static Vertex[] left = new Vertex[256];
	private static Vertex[] right = new Vertex[256];

	public Bitmap3D(int width, int height) {
		super(width, height);
		this.zBuffer = new double[width * height];
	}

	public void clearZBuffer() {
		Arrays.fill(this.zBuffer, Double.MAX_VALUE);
	}

	public void render(Polygon polygon, Bitmap texture, Bitmap3D.Mode mode, int color) {
		if (polygon.vertexCount >= 3) {
			int top = 0;
			int bottom = 0;

			for (int i = 1; i < polygon.vertexCount; ++i) {
				if (polygon.vertices[i].yt < polygon.vertices[top].yt) {
					top = i;
				}

				if (polygon.vertices[i].yt >= polygon.vertices[bottom].yt) {
					bottom = i;
				}
			}

			double yy0 = polygon.vertices[top].yt;
			double yy1 = polygon.vertices[bottom].yt;
			if (!(yy1 <= yy0) && !(yy0 >= this.height) && !(yy1 <= 0.0)) {
				int lc = 0;
				left[lc++] = polygon.vertices[top];
				int i = 1;

				while (true) {
					Vertex v0 = polygon.vertices[(top + i) % polygon.vertexCount];
					Vertex v1 = polygon.vertices[(top + i + 1) % polygon.vertexCount];
					if (!(v1.yt >= v0.yt)) {
						left[lc++] = polygon.vertices[bottom];
						i = 0;
						right[i++] = polygon.vertices[top];
						int ix = polygon.vertexCount - 1;

						while (true) {
							v1 = polygon.vertices[(top + ix) % polygon.vertexCount];
							Vertex v1x = polygon.vertices[(top + ix + 1) % polygon.vertexCount];
							if (!(v1x.yt <= v1.yt)) {
								right[i++] = polygon.vertices[bottom];
								int y0 = (int) yy0;
								int y1 = (int) yy1;
								i = 0;
								lc = 0;
								if (y0 < 0) {
									y0 = 0;
								}

								if (y1 > this.height) {
									y1 = this.height;
								}

								int aCol = color >> 24 & 0xFF;
								int rCol = color >> 16 & 0xFF;
								int gCol = color >> 8 & 0xFF;
								int bCol = color & 0xFF;
								int tw = texture.width;
								int twm = texture.width - 1;
								int thm = texture.height - 1;
								Vertex l0 = left[lc];
								Vertex l1 = left[lc + 1];
								Vertex r0 = right[i];
								Vertex r1 = right[i + 1];
								double lLen = 1.0 / (l1.yt - l0.yt);
								double rLen = 1.0 / (r1.yt - r0.yt);

								for (int y = y0; y < y1; ++y) {
									while (y >= (int) left[lc + 1].yt) {
										++lc;
										l0 = l1;
										l1 = left[lc + 1];
										lLen = 1.0 / (l1.yt - l0.yt);
									}

									while (y >= (int) right[i + 1].yt) {
										++i;
										r0 = r1;
										r1 = right[i + 1];
										rLen = 1.0 / (r1.yt - r0.yt);
									}

									double lp = (y - l0.yt + 1.0) * lLen;
									double rp = (y - r0.yt + 1.0) * rLen;
									double xx0 = l0.xt + (l1.xt - l0.xt) * lp;
									double xx1 = r0.xt + (r1.xt - r0.xt) * rp;
									if (xx1 <= xx0) {
										return;
									}

									if (!(xx0 >= this.width) && !(xx1 <= 0.0)) {
										int x0 = (int) xx0;
										int x1 = (int) xx1;
										if (x0 < 0) {
											x0 = 0;
										}

										if (x1 > this.width) {
											x1 = this.width;
										}

										double u0 = l0.ut + (l1.ut - l0.ut) * lp;
										double ua = r0.ut + (r1.ut - r0.ut) * rp - u0;
										double v0x = l0.vt + (l1.vt - l0.vt) * lp;
										double va = r0.vt + (r1.vt - r0.vt) * rp - v0x;
										double iz0 = l0.iz + (l1.iz - l0.iz) * lp;
										double iza = r0.iz + (r1.iz - r0.iz) * rp - iz0;
										double ixx = 1.0 / (xx1 - xx0);
										ua *= ixx;
										va *= ixx;
										iza *= ixx;
										u0 -= (xx0 - x0 - 1.0) * ua;
										v0x -= (xx0 - x0 - 1.0) * va;
										iz0 -= (xx0 - x0 - 1.0) * iza;
										int p = y * this.width + x0;
										if (mode == Bitmap3D.Mode.straight) {
											for (int x = x0; x < x1; ++x) {
												double z = 1.0 / iz0;
												if (z > this.zBuffer[p]) {
													++p;
												} else {
													int u = (int) (u0 * z) & twm;
													int v = (int) (v0x * z) & thm;
													int sourceCol = texture.pixels[u + v * tw];
													if ((sourceCol >> 24 & 0xFF) == 255) {
														this.zBuffer[p] = z;
														this.pixels[p++] = sourceCol;
													} else {
														++p;
													}
												}

												u0 += ua;
												v0x += va;
												iz0 += iza;
											}
										} else if (mode == Bitmap3D.Mode.color) {
											for (int x = x0; x < x1; ++x) {
												double z = 1.0 / iz0;
												if (z > this.zBuffer[p]) {
													++p;
												} else {
													this.zBuffer[p] = z;
													int u = (int) (u0 * z) & twm;
													int v = (int) (v0x * z) & thm;
													int col = texture.pixels[u + v * tw];
													int r = (col >> 16 & 0xFF) * rCol >> 8;
													int g = (col >> 8 & 0xFF) * gCol >> 8;
													int b = (col & 0xFF) * bCol >> 8;
													this.pixels[p++] = r << 16 | g << 8 | b;
												}

												u0 += ua;
												v0x += va;
												iz0 += iza;
											}
										} else if (mode == Bitmap3D.Mode.blend) {
											for (int x = x0; x < x1; ++x) {
												double z = 1.0 / iz0;
												if (z > this.zBuffer[p]) {
													++p;
												} else {
													this.zBuffer[p] = z;
													int u = (int) (u0 * z) & twm;
													int v = (int) (v0x * z) & thm;
													int col = texture.pixels[u + v * tw];
													int r = col >> 16 & 0xFF;
													int g = col >> 8 & 0xFF;
													int b = col & 0xFF;
													int ia = 255 - aCol;
													int sCol = this.pixels[p];
													r = r * aCol + (sCol >> 16 & 0xFF) * ia >> 8;
													g = g * aCol + (sCol >> 8 & 0xFF) * ia >> 8;
													b = b * aCol + (sCol & 0xFF) * ia >> 8;
													this.pixels[p++] = r << 16 | g << 8 | b;
												}

												u0 += ua;
												v0x += va;
												iz0 += iza;
											}
										} else if (mode == Bitmap3D.Mode.colorblend) {
											for (int x = x0; x < x1; ++x) {
												double z = 1.0 / iz0;
												if (z > this.zBuffer[p]) {
													++p;
												} else {
													this.zBuffer[p] = z;
													int u = (int) (u0 * z) & twm;
													int v = (int) (v0x * z) & thm;
													int col = texture.pixels[u + v * tw];
													int a = (col >> 24 & 0xFF) * aCol >> 8;
													int r = (col >> 16 & 0xFF) * rCol >> 8;
													int g = (col >> 8 & 0xFF) * gCol >> 8;
													int b = (col & 0xFF) * bCol >> 8;
													int ia = 255 - a;
													int sCol = this.pixels[p];
													r = r * a + (sCol >> 16 & 0xFF) * ia >> 8;
													g = g * a + (sCol >> 8 & 0xFF) * ia >> 8;
													b = b * a + (sCol & 0xFF) * ia >> 8;
													this.pixels[p++] = r << 16 | g << 8 | b;
												}

												u0 += ua;
												v0x += va;
												iz0 += iza;
											}
										} else if (mode == Bitmap3D.Mode.add) {
											for (int x = x0; x < x1; ++x) {
												double z = 1.0 / iz0;
												if (z > this.zBuffer[p]) {
													++p;
												} else {
													this.zBuffer[p] = z;
													int u = (int) (u0 * z) & twm;
													int v = (int) (v0x * z) & thm;
													int col = texture.pixels[u + v * tw];
													int sCol = this.pixels[p];
													int r = (col >> 16 & 0xFF) + (sCol >> 16 & 0xFF);
													int g = (col >> 8 & 0xFF) + (sCol >> 8 & 0xFF);
													int b = (col & 0xFF) + (sCol & 0xFF);
													if (r > 255) {
														r = 255;
													}

													if (g > 255) {
														g = 255;
													}

													if (b > 255) {
														b = 255;
													}

													this.pixels[p++] = r << 16 | g << 8 | b;
												}

												u0 += ua;
												v0x += va;
												iz0 += iza;
											}
										} else if (mode == Bitmap3D.Mode.coloradd) {
											for (int x = x0; x < x1; ++x) {
												double z = 1.0 / iz0;
												if (z > this.zBuffer[p]) {
													++p;
												} else {
													this.zBuffer[p] = z;
													int u = (int) (u0 * z) & twm;
													int v = (int) (v0x * z) & thm;
													int col = texture.pixels[u + v * tw];
													int sCol = this.pixels[p];
													int r = ((col >> 16 & 0xFF) * rCol >> 8) + (sCol >> 16 & 0xFF);
													int g = ((col >> 8 & 0xFF) * gCol >> 8) + (sCol >> 8 & 0xFF);
													int b = ((col & 0xFF) * bCol >> 8) + (sCol & 0xFF);
													if (r > 255) {
														r = 255;
													}

													if (g > 255) {
														g = 255;
													}

													if (b > 255) {
														b = 255;
													}

													this.pixels[p++] = r << 16 | g << 8 | b;
												}

												u0 += ua;
												v0x += va;
												iz0 += iza;
											}
										}
									}
								}

								return;
							}

							right[i++] = v1;
							--ix;
						}
					}

					left[lc++] = v0;
					++i;
				}
			}
		}
	}

	public static enum Mode {
		straight,
		color,
		blend,
		colorblend,
		add,
		coloradd;
	}
}
