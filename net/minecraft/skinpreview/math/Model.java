package net.minecraft.skinpreview.math;

import net.minecraft.skinpreview.gfx.Bitmap;
import net.minecraft.skinpreview.gfx.Bitmap3D;

public class Model {
	private Vertex[] vertices;
	private Polygon[] polygons;
	private int xTexOffs;
	private int yTexOffs;
	public double x;
	public double y;
	public double z;
	public double xRot;
	public double yRot;
	public double zRot;
	public boolean mirror = false;
	public boolean invert = false;

	public Model(int xTexOffs, int yTexOffs) {
		this.xTexOffs = xTexOffs;
		this.yTexOffs = yTexOffs;
	}

	public void setTexOffs(int xTexOffs, int yTexOffs) {
		this.xTexOffs = xTexOffs;
		this.yTexOffs = yTexOffs;
	}

	public void addBox(double x0, double y0, double z0, int w, int h, int d, float g) {
		this.vertices = new Vertex[8];
		this.polygons = new Polygon[6];
		double x1 = x0 + w;
		double y1 = y0 + h;
		double z1 = z0 + d;
		x0 -= g;
		y0 -= g;
		z0 -= g;
		x1 += g;
		y1 += g;
		z1 += g;
		if (this.mirror) {
			double tmp = x1;
			x1 = x0;
			x0 = tmp;
		}

		Vertex u0 = new Vertex(x0 - g, y0 - g, z0 - g, 0.0, 0.0);
		Vertex u1 = new Vertex(x1 + g, y0 - g, z0 - g, 0.0, 8.0);
		Vertex u2 = new Vertex(x1 + g, y1 + g, z0 - g, 8.0, 8.0);
		Vertex u3 = new Vertex(x0 - g, y1 + g, z0 - g, 8.0, 0.0);
		Vertex l0 = new Vertex(x0 - g, y0 - g, z1 + g, 0.0, 0.0);
		Vertex l1 = new Vertex(x1 + g, y0 - g, z1 + g, 0.0, 8.0);
		Vertex l2 = new Vertex(x1 + g, y1 + g, z1 + g, 8.0, 8.0);
		Vertex l3 = new Vertex(x0 - g, y1 + g, z1 + g, 8.0, 0.0);
		this.vertices[0] = u0;
		this.vertices[1] = u1;
		this.vertices[2] = u2;
		this.vertices[3] = u3;
		this.vertices[4] = l0;
		this.vertices[5] = l1;
		this.vertices[6] = l2;
		this.vertices[7] = l3;
		this.polygons[0] = new Polygon(new Vertex[] { l1, u1, u2, l2 }, this.xTexOffs + d + w, this.yTexOffs + d,
				this.xTexOffs + d + w + d, this.yTexOffs + d + h);
		this.polygons[1] = new Polygon(new Vertex[] { u0, l0, l3, u3 }, this.xTexOffs + 0, this.yTexOffs + d,
				this.xTexOffs + d, this.yTexOffs + d + h);
		this.polygons[2] = new Polygon(new Vertex[] { l1, l0, u0, u1 }, this.xTexOffs + d, this.yTexOffs + 0,
				this.xTexOffs + d + w, this.yTexOffs + d);
		this.polygons[3] = new Polygon(new Vertex[] { u2, u3, l3, l2 }, this.xTexOffs + d + w, this.yTexOffs + 0,
				this.xTexOffs + d + w + w, this.yTexOffs + d);
		this.polygons[4] = new Polygon(new Vertex[] { u1, u0, u3, u2 }, this.xTexOffs + d, this.yTexOffs + d,
				this.xTexOffs + d + w, this.yTexOffs + d + h);
		this.polygons[5] = new Polygon(new Vertex[] { l0, l1, l2, l3 }, this.xTexOffs + d + w + d, this.yTexOffs + d,
				this.xTexOffs + d + w + d + w, this.yTexOffs + d + h);
		if (this.mirror ^ this.invert) {
			for (Polygon polygon : this.polygons) {
				polygon.mirror();
			}
		}

	}

	public void render(Matrix3 matrix, Bitmap texture, Bitmap3D screenTexture) {
		matrix = matrix.clone();
		if (this.x != 0.0 || this.y != 0.0 || this.z != 0.0) {
			matrix = matrix.translate(this.x, this.y, this.z);
		}

		if (this.zRot != 0.0) {
			matrix = matrix.rotZ(this.zRot);
		}

		if (this.yRot != 0.0) {
			matrix = matrix.rotY(this.yRot);
		}

		if (this.xRot != 0.0) {
			matrix = matrix.rotX(this.xRot);
		}

		int i;
		for (i = 0; i < this.vertices.length; ++i) {
			this.vertices[i].transform(matrix);
		}

		for (i = 0; i < this.polygons.length; ++i) {
			this.polygons[i].clipZ(1.0);
			this.polygons[i].project();
			screenTexture.render(this.polygons[i], texture, Bitmap3D.Mode.straight, -1);
		}
	}

	public void setPos(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
