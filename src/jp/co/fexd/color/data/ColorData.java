package jp.co.fexd.color.data;


public class ColorData {
	private int no = -1;
	private String name = null;
	private String kana = null;
	private String discription = null;
	private MyHSV hsv = null;
	
	public ColorData(int no, String name, String kana, int h, int s, int v, String discription) {
		this.no = no;
		this.name = name;
		this.kana = kana;
		this.discription = discription;
		MyHSV hsv = new MyHSV(h, s, v);
		this.hsv = hsv;
	}
	
	public int getNo() {
		return no;
	}
	public String getName() {
		return name;
	}
	public String getKana() {
		return kana;
	}
	public String getDiscription() {
		return discription;
	}
	public boolean existDiscription() {
		if (discription.equals("")) {
			return false;
		}
		return true;
	}
	public MyHSV getHsv() {
		return hsv;
	}
	
	/**
	 * オブジェクトが引数の色と近い色かを判定する。
	 * @param hsv
	 * @return
	 */
	public boolean isNearColor_old(MyHSV hsv) {
		getVlaueNearColorH(hsv);
		int sub_h = this.hsv.getH() - hsv.getH();
		if (Math.abs(sub_h) < 21) {
			// 差が20以内なら近い色と判定。
			return true;
		}
		if (339 < Math.abs(sub_h)) {
			// 差が20以内なら近い色と判定。
			return true;
		}
		
		return false;
	}
	public boolean isNearColor(MyHSV hsv) {
		if (20 < getVlaueNearColorH(hsv) ) {
			return false;
		}
		
		double svNearValue = getVlaueNearColorSV(hsv);
		if (40 < svNearValue){
			return false;
		}
		/*

		double hNearRate = getVlaueNearColorHRate(hsv);
		if (hNearRate < 0.01) {
			// 反対の色。
			return false;
		}
		
		
		double svRate = hsv.getSVRate();
		if (svRate > hNearRate){
			// 
			return false;
		}
		
		
		
		double min = 5;
		double max = 33;
		double svNearValue = getVlaueNearColorSV(hsv);
		double tmp = min + hNearRate * (max - min);
		Log.d("color", "color" + hsv);
		Log.d("color", "" + this.hsv);
		Log.d("color", "" + hNearRate);
		Log.d("color", "" + svRate);
		Log.d("color", "svNearValue:" + svNearValue);
		Log.d("color", "" + tmp);
		if (svNearValue < tmp) {
			return true;
		}
		Log.d("color", "svNearValue:" + svNearValue);
		Log.d("color", "" + tmp);
		*/
		return true;
	}
	/*
	private double getVlaueNearColorHRate(MyHSV hsv) {
		double min = 20;
		double max = 160;
		
		int sub_h = getVlaueNearColorH(hsv);
		
		if (sub_h < min+1) {
			return 1;
		}
		if (max < sub_h) {
			return 0;
		}
		
		double tmp = sub_h - min;
		return 1 - (tmp / (max-min));
	}
	*/
	private int getVlaueNearColorH(MyHSV hsv) {
		int sub_h = this.hsv.getH() - hsv.getH();
		int tmp = Math.abs(sub_h);
		if (180 < tmp) {
			tmp = 360 - tmp;
			// Log.d("sub", "sub1=" + tmp);
			return tmp;
		}
		
		// Log.d("sub", "sub2=" + tmp);
		return tmp;
	}
	private double getSaSiGo(int sa, int si) {
		int tmp = sa * sa + si * si;
		return Math.sqrt((double) tmp);
	}
	private int getVlaueNearColorSV(MyHSV hsv) {
		int sub_s = this.hsv.getS() - hsv.getS();
		int sub_v = this.hsv.getV() - hsv.getV();
		int tmp = (int)getSaSiGo(sub_s, sub_v);
		
		return tmp;
	}
}
