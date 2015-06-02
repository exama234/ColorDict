package jp.co.fexd.color.data;

import android.graphics.Color;
import android.util.Log;

public class MyHSV {
	private int h = -1;
	private int s = -1;
	private int v = -1;
	
	public int getH() {
		return h;
	}
	public void setH(int h) {
		h = Math.max(h, 0);
		h = Math.min(h, 360);
		this.h = h;
	}

	public int getS() {
		return s;
	}
	public void setS(int s) {
		s = Math.max(s, 0);
		s = Math.min(s, 100);
		this.s = s;
	}

	public int getV() {
		return v;
	}
	public void setV(int v) {
		v = Math.max(v, 0);
		v = Math.min(v, 100);
		this.v = v;
	}

	public MyHSV() {
		this.h = 0;
		this.s = 0;
		this.v = 0;
	}
	
	public MyHSV(int h, int s, int v) {
		this.setH(h);
		this.setS(s);
		this.setV(v);
	}
	
	public MyRGB toRGB(){
		int c1, c2, c3;
		int r=0, g=0, b=0;
		if (this.s == 0) {
			// Log.d("S=0", toString());
			r=g=b=this.v;
			return new MyRGB(r, g, b);
		}
		
		int tmp = (int) ((this.h * 6) % 360);
		c1 = (this.v * ( 100 -  this.s ) ) / 100;
		c2 = (this.v * ( 100 - (this.s * tmp) / 360)) / 100;
		c3 = (this.v * ( 100 - (this.s *(360 - tmp)) / 360)) / 100;
		switch ((int)this.h /60) {
			case 0: r=this.v; g=c3; b=c1; break;
			case 1: r=c2; g=this.v ; b=c1; break;
			case 2: r=c1; g=this.v ; b=c3; break;
			case 3: r=c1; g=c2; b=this.v ; break;
			case 4: r=c3; g=c1; b=this.v ; break;
			case 5: r=this.v ; g=c1; b=c2; break;
		}
		
		return new MyRGB(r, g, b);
	}
	
	public double getSVRate() {
		double tmp = (this.s + this.v) / 200d;
		Log.d("test", "" + tmp);
		return tmp;
	}
	public int getColor() {
		float[] hsv = new float[3];
		if(this.h >= 360)
			this.h = 359;
		else if(this.h < 0)
			this.h = 0;

		if(this.s > 100)
			this.s = 100;
		else if(this.s < 0)
			this.s = 0;
		
		if(this.v > 100)
			this.v = 100;
		else if(this.v < 0)
			this.v = 0;

		hsv[0] = this.h;
		hsv[1] = this.s / 100.0F;
		hsv[2] = this.v / 100.0F;
		
		return Color.HSVToColor(hsv);
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.h + ",");
		buffer.append(this.s + ",");
		buffer.append(this.v);

		return buffer.toString();
	}
}
