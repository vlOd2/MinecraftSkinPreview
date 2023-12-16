package net.minecraft.skinpreview.math;

public class Vec3 {
	public double x;
	public double y;
	public double z;

	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3 interpolateTo(Vec3 t, double p) {
		double xt = this.x + (t.x - this.x) * p;
		double yt = this.y + (t.y - this.y) * p;
		double zt = this.z + (t.z - this.z) * p;
		return new Vec3(xt, yt, zt);
	}

	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
