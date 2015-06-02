package jp.co.fexd.color.data;

public class MyRGB {
	private int r = -1;
	private int g = -1;
	private int b = -1;
	
	public MyRGB() {
		r = 0;
		g = 0;
		b = 0;
	}
	public MyRGB(int r, int g, int b) {
		this.setR(r);
		this.setG(g);
		this.setB(b);
	}
	
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}

	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.r + ",");
		buffer.append(this.g + ",");
		buffer.append(this.b);

		return buffer.toString();
	}
	
	public String toColorCode() {
		StringBuffer buffer = new StringBuffer();
		// RGBを16進数2ケタ表示。
		buffer.append(String.format("%1$02X", this.r));
		buffer.append(String.format("%1$02X", this.g));
		buffer.append(String.format("%1$02X", this.b));

		return buffer.toString();
	}
}
