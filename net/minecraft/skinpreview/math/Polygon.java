package net.minecraft.skinpreview.math;

public class Polygon {
	private Vertex[] orgVertices;
	private Vertex[] tmpVertices;
	public Vertex[] vertices;
	public int vertexCount;
	private static int ab = 0;
	private static int nc = 0;
	private static int pc = 0;
	private static int tot = 0;

	public Polygon(Vertex[] vertices) {
		this.vertexCount = 0;
		this.orgVertices = vertices;
		this.tmpVertices = new Vertex[vertices.length + 1];
		this.vertices = this.orgVertices;
		this.vertexCount = vertices.length;
	}

	public Polygon(Vertex[] vertices, int u0, int v0, int u1, int v1) {
		this(vertices);
		vertices[0] = vertices[0].remap(u1, v0);
		vertices[1] = vertices[1].remap(u0, v0);
		vertices[2] = vertices[2].remap(u0, v1);
		vertices[3] = vertices[3].remap(u1, v1);
	}

	public static void dumpStats() {
		System.out.println("Stats for " + tot + " clips");
		System.out.println("  All behind: " + ab * 10000 / tot / 100.0 + "%");
		System.out.println("     No clip: " + nc * 10000 / tot / 100.0 + "%");
		System.out.println("Partial clip: " + pc * 10000 / tot / 100.0 + "%");
		tot = 0;
		pc = 0;
		nc = 0;
		ab = 0;
	}

	public final void clipZ(double zd) {
		++tot;
		boolean shouldClip = false;
		boolean allBehind = true;

		for (int i = 0; i < this.orgVertices.length && (!shouldClip || allBehind); ++i) {
			if (this.orgVertices[i].tPos.z < zd) {
				shouldClip = true;
			} else {
				allBehind = false;
			}
		}

		if (allBehind) {
			this.vertices = this.tmpVertices;
			this.vertexCount = 0;
			++ab;
		} else if (!shouldClip) {
			this.vertices = this.orgVertices;
			this.vertexCount = this.vertices.length;
			++nc;
		} else {
			++pc;
			this.vertices = this.tmpVertices;
			this.vertexCount = 0;
			Vertex v0 = this.orgVertices[this.orgVertices.length - 1];

			for (Vertex v1 : this.orgVertices) {
				Vec3 p0 = v0.tPos;
				Vec3 p1 = v1.tPos;
				if (p0.z >= zd && p1.z >= zd) {
					this.vertices[this.vertexCount++] = v1;
				} else if (!(p0.z < zd) || !(p1.z < zd)) {
					double d = (zd - p0.z) / (p1.z - p0.z);
					this.vertices[this.vertexCount++] = v0.interpolateTo(v1, d);
					if (p0.z < zd && p1.z >= zd) {
						this.vertices[this.vertexCount++] = v1;
					}
				}

				v0 = v1;
			}

		}
	}

	public void project() {
		for (int i = 0; i < this.vertexCount; ++i) {
			this.vertices[i].project();
		}

	}

	public void transform(Matrix3 m) {
		int i;
		for (i = 0; i < this.orgVertices.length; ++i) {
			Vertex v = this.orgVertices[i];
			v.transform(m);
		}

		this.clipZ(1.0);

		for (i = 0; i < this.vertexCount; ++i) {
			this.vertices[i].project();
		}

	}

	public void mirror() {
		Vertex[] newVertices = new Vertex[this.vertices.length];

		for (int i = 0; i < this.vertices.length; ++i) {
			newVertices[i] = this.vertices[this.vertices.length - i - 1];
		}

		this.vertices = newVertices;
		this.orgVertices = this.vertices;
		this.tmpVertices = new Vertex[this.vertices.length + 1];
		this.vertices = this.orgVertices;
		this.vertexCount = this.vertices.length;
	}
}
