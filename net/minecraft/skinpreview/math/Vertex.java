package net.minecraft.skinpreview.math;

public class Vertex {
	public Vec3 pos;
	public Vec3 tPos;
	public double u;
	public double v;
	public double xt;
	public double yt;
	public double zt;
	public double ut;
	public double vt;
	public double iz;

	public Vertex(double x, double y, double z, double u, double v) {
		this(new Vec3(x, y, z), u, v);
	}

	public Vertex remap(double u, double v) {
		return new Vertex(this, u, v);
	}

	public Vertex(Vertex vertex, double u, double v) {
		this.pos = vertex.pos;
		this.tPos = vertex.tPos;
		this.u = u;
		this.v = v;
	}

	public Vertex(Vec3 pos, double u, double v) {
		this.pos = pos;
		this.tPos = new Vec3(pos.x, pos.y, pos.z);
		this.u = u;
		this.v = v;
	}

	public Vertex interpolateTo(Vertex t, double p) {
		Vec3 vec = this.tPos.interpolateTo(t.tPos, p);
		double ut = this.u + (t.u - this.u) * p;
		double vt = this.v + (t.v - this.v) * p;
		return new Vertex(vec, ut, vt).project();
	}

	public Vertex transform(Matrix3 m) {
		m.mul(this.pos, this.tPos);
		return this;
	}

	public Vertex project() {
		this.xt = this.tPos.x / this.tPos.z * 100.0 + 80.0;
		this.yt = this.tPos.y / this.tPos.z * 100.0 + 80.0;
		this.zt = this.tPos.z;
		this.iz = 1.0 / this.zt;
		this.ut = this.u / this.zt;
		this.vt = this.v / this.zt;
		return this;
	}
}
