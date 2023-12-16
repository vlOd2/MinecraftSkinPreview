package net.minecraft.skinpreview.math;

public class Matrix3 {
	public double m0;
	public double m1;
	public double m2;
	public double m3;
	public double m4;
	public double m5;
	public double m6;
	public double m7;
	public double m8;
	public double m9;
	public double ma;
	public double mb;

	public Matrix3() {
		this(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	}

	public Matrix3(double _m0, double _m1, double _m2, double _m3, double _m4, double _m5, double _m6, double _m7,
			double _m8, double _m9, double _ma, double _mb) {
		this.m0 = _m0;
		this.m1 = _m1;
		this.m2 = _m2;
		this.m3 = _m3;
		this.m4 = _m4;
		this.m5 = _m5;
		this.m6 = _m6;
		this.m7 = _m7;
		this.m8 = _m8;
		this.m9 = _m9;
		this.ma = _ma;
		this.mb = _mb;
	}

	public Matrix3 set(double _m0, double _m1, double _m2, double _m3, double _m4, double _m5, double _m6, double _m7,
			double _m8, double _m9, double _ma, double _mb) {
		this.m0 = _m0;
		this.m1 = _m1;
		this.m2 = _m2;
		this.m3 = _m3;
		this.m4 = _m4;
		this.m5 = _m5;
		this.m6 = _m6;
		this.m7 = _m7;
		this.m8 = _m8;
		this.m9 = _m9;
		this.ma = _ma;
		this.mb = _mb;
		return this;
	}

	@Override
	public Matrix3 clone() {
		return new Matrix3(this.m0, this.m1, this.m2, this.m3, this.m4, this.m5, this.m6, this.m7, this.m8, this.m9,
				this.ma, this.mb);
	}

	public Vec3 mul(double xs, double ys, double zs) {
		double _x = xs * this.m0 + ys * this.m1 + zs * this.m2 + 1.0 * this.m3;
		double _y = xs * this.m4 + ys * this.m5 + zs * this.m6 + 1.0 * this.m7;
		double _z = xs * this.m8 + ys * this.m9 + zs * this.ma + 1.0 * this.mb;
		return new Vec3(_x, _y, _z);
	}

	public Vec3 mul(Vec3 o) {
		double _x = o.x * this.m0 + o.y * this.m1 + o.z * this.m2 + 1.0 * this.m3;
		double _y = o.x * this.m4 + o.y * this.m5 + o.z * this.m6 + 1.0 * this.m7;
		double _z = o.x * this.m8 + o.y * this.m9 + o.z * this.ma + 1.0 * this.mb;
		return new Vec3(_x, _y, _z);
	}

	public void mul(Vec3 o, Vec3 t) {
		double _x = o.x * this.m0 + o.y * this.m1 + o.z * this.m2 + this.m3;
		double _y = o.x * this.m4 + o.y * this.m5 + o.z * this.m6 + this.m7;
		double _z = o.x * this.m8 + o.y * this.m9 + o.z * this.ma + this.mb;
		t.set(_x, _y, _z);
	}

	public Matrix3 mul(double _m0, double _m1, double _m2, double _m3, double _m4, double _m5, double _m6, double _m7,
			double _m8, double _m9, double _ma, double _mb) {
		return this.set(this.m0 * _m0 + this.m1 * _m4 + this.m2 * _m8 + this.m3 * 0.0,
				this.m0 * _m1 + this.m1 * _m5 + this.m2 * _m9 + this.m3 * 0.0,
				this.m0 * _m2 + this.m1 * _m6 + this.m2 * _ma + this.m3 * 0.0,
				this.m0 * _m3 + this.m1 * _m7 + this.m2 * _mb + this.m3 * 1.0,
				this.m4 * _m0 + this.m5 * _m4 + this.m6 * _m8 + this.m7 * 0.0,
				this.m4 * _m1 + this.m5 * _m5 + this.m6 * _m9 + this.m7 * 0.0,
				this.m4 * _m2 + this.m5 * _m6 + this.m6 * _ma + this.m7 * 0.0,
				this.m4 * _m3 + this.m5 * _m7 + this.m6 * _mb + this.m7 * 1.0,
				this.m8 * _m0 + this.m9 * _m4 + this.ma * _m8 + this.mb * 0.0,
				this.m8 * _m1 + this.m9 * _m5 + this.ma * _m9 + this.mb * 0.0,
				this.m8 * _m2 + this.m9 * _m6 + this.ma * _ma + this.mb * 0.0,
				this.m8 * _m3 + this.m9 * _m7 + this.ma * _mb + this.mb * 1.0);
	}

	public Matrix3 rotX(double r) {
		double s = Math.sin(r);
		double c = Math.cos(r);
		return this.mul(1.0, 0.0, 0.0, 0.0, 0.0, c, -s, 0.0, 0.0, s, c, 0.0);
	}

	public Matrix3 rotY(double r) {
		double s = Math.sin(r);
		double c = Math.cos(r);
		return this.mul(c, 0.0, s, 0.0, 0.0, 1.0, 0.0, 0.0, -s, 0.0, c, 0.0);
	}

	public Matrix3 rotZ(double r) {
		double s = Math.sin(r);
		double c = Math.cos(r);
		return this.mul(c, -s, 0.0, 0.0, s, c, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	}

	public Matrix3 scale(double xs, double ys, double zs) {
		return this.mul(xs, 0.0, 0.0, 0.0, 0.0, ys, 0.0, 0.0, 0.0, 0.0, zs, 0.0);
	}

	public Matrix3 translate(double x, double y, double z) {
		return this.mul(1.0, 0.0, 0.0, x, 0.0, 1.0, 0.0, y, 0.0, 0.0, 1.0, z);
	}
}
