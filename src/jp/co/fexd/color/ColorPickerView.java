package jp.co.fexd.color;

import jp.co.fexd.color.data.ColorDataList;
import jp.co.fexd.color.data.MyHSV;
import jp.co.fexd.color.thread.MyTask;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * カラーピッカーのカスタムビュー。
 * @author Administrator
 *
 */
public class ColorPickerView extends View {
	private Paint mPaint, mPaintC;
	private Paint mOKPaint;
	private final int[] mColors;
	private int[] mChroma;
	private Shader sg, lg;
	private int selectColor;
	private float selectHue = 0;
	ColorDataList colorDataList = new ColorDataList();
	
	private Activity activity = null;
	private LinearLayout layout = null;
	
	
	// ColorPickerView(Context c, OnColorChangedListener l, int color) {
	public void setParentActivity(Activity activity) {
		this.activity = activity;
	}
	public void setLayout(LinearLayout layout) {
		this.layout = layout;
	}
	public ColorPickerView(Context c, AttributeSet attrs) {
		super(c, attrs);
		// selectColor = color;
		selectColor = 1;
		selectHue = getHue(selectColor);
		mColors = new int[] {
			0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
			0xFFFFFF00, 0xFFFF0000
		};
		
		mChroma = new int[] {
				0xFF000000, 0xFF888888, 0xFFFFFFFF
		};
		
		sg = new SweepGradient(0, 0, mColors, null);
		lg = new LinearGradient(OK_X0, 0, OK_X1, 0, mChroma, null, Shader.TileMode.CLAMP);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setShader(sg);
		mPaint.setStrokeWidth(CENTER_RADIUS);

		mPaintC = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintC.setStyle(Paint.Style.FILL);
		mPaintC.setShader(lg);
		mPaintC.setStrokeWidth(2);
		
		mOKPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mOKPaint.setStyle(Paint.Style.FILL);
		mOKPaint.setColor(selectColor);
		mOKPaint.setStrokeWidth(5);
	}
	
	private boolean mTrackingOK;
	private boolean mHighlightOK;
	
	private static final int CENTER_X = 120;
	private static final int CENTER_Y = 120;
	private static final int CENTER_RADIUS = 32;
	// 領域
	private static final float OK_X0 = - CENTER_X/2;
	private static final float OK_X1 =   CENTER_X/2;
	private static final float OK_Y0 = (float) (CENTER_X * 1.2);
	private static final float OK_Y1 = (float) (CENTER_X * 1.5);
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(CENTER_X * 2, (int)(CENTER_Y * 2.8));
	}
	
	/**
	 * SV領域を描画する。
	 * @param canvas
	 */
	private void drawSVRegion(Canvas canvas) {
		final float RESOLUTION = (float)0.01;
		
		for(float y = 0; y < 1; y += RESOLUTION) {
			mChroma = new int[10];

			int i = 0;
			for(float x = 0; i < 10; x += 0.1, i+=1) {
				mChroma[i] = setHSVColor(selectHue, x, y);
			}
			lg = new LinearGradient(OK_X0, 0, OK_X1, 0, mChroma, null, Shader.TileMode.CLAMP);
			mPaintC.setShader(lg);
			
			//canvas.drawRect(OK_X0, OK_X0 + (CENTER_X * y), OK_X1, OK_X0 + (float)(CENTER_X * (y)), mPaintC);
			canvas.drawLine(OK_X0, OK_X0 + (CENTER_X * y), OK_X1, OK_X0 + (float)(CENTER_X * (y)), mPaintC);
		}
	}

	private float getHue(int color) {
		float hsv[] = new float[3];
		Color.colorToHSV(color, hsv);
		return hsv[0];
	}
	
	private int setHSVColor(float hue, float saturation, float value) {
		float[] hsv = new float[3];
		if(hue >= 360)
			hue = 359;
		else if(hue < 0)
			hue = 0;

		if(saturation > 1)
			saturation = 1;
		else if(saturation < 0)
			saturation = 0;
		
		if(value > 1)
			value = 1;
		else if(value < 0)
			value = 0;

		hsv[0] = hue;
		hsv[1] = saturation;
		hsv[2] = value;
		
		return Color.HSVToColor(hsv);
	}

	/**
	 * 描画命令。
	 */
	protected void onDraw(Canvas canvas) {
		float r = CENTER_X - mPaint.getStrokeWidth() * 0.5f;
		
		canvas.translate(CENTER_X, CENTER_X);
		canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
		
		// SV領域の描画。
		drawSVRegion(canvas);

		// 「OK」の周辺の描画
		canvas.drawRoundRect(new RectF(OK_X0, OK_Y0, OK_X1, OK_Y1), 5, 5, mOKPaint);
		// 「OK」の描画
		Paint textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(20);
		textPaint.setAntiAlias(true);
		//canvas.drawText("OK", 0 - 12, (float) (CENTER_X * 1.2) + 22, textPaint);
		canvas.drawText("色", 0 - 14, (float) (CENTER_X * 1.4) + 2, textPaint);
		
		if (mTrackingOK) {
			int c = mOKPaint.getColor();
			mOKPaint.setStyle(Paint.Style.STROKE);
			
			if (mHighlightOK) 
				mOKPaint.setAlpha(0xFF);
			else 
				mOKPaint.setAlpha(0x80);

			float padding = 5;
			//canvas.drawCircle(0, 0, CENTER_RADIUS + mOKPaint.getStrokeWidth(), mOKPaint);
			canvas.drawRoundRect(new RectF(OK_X0 - padding, OK_Y0 - padding, OK_X1 + padding, OK_Y1 + padding), 5, 5, mOKPaint);
			mOKPaint.setStyle(Paint.Style.FILL);
			mOKPaint.setColor(c);
		}					
	}
	
	private int ave(int s, int d, float p) {
		return s + java.lang.Math.round(p * (d - s));
	}
	
	private int interpColor(int colors[], float unit) {
		if (unit <= 0) {
			return colors[0];
		}
		if (unit >= 1) {
			return colors[colors.length - 1];
		}
		
		float p = unit * (colors.length - 1);
		int i = (int)p;
		p -= i;

		// now p is just the fractional part [0...1) and i is the index
		int c0 = colors[i];
		int c1 = colors[i+1];
		int a = ave(Color.alpha(c0), Color.alpha(c1), p);
		int r = ave(Color.red(c0),   Color.red(c1),   p);
		int g = ave(Color.green(c0), Color.green(c1), p);
		int b = ave(Color.blue(c0),  Color.blue(c1),  p);
		
		return Color.argb(a, r, g, b);
	}
	
	Handler handler= new Handler();
	private MyTask myTask = null;
	private static final float PI = 3.1415927f;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		float x = event.getX() - CENTER_X;
		float y = event.getY() - CENTER_Y;
		float r = (float)(java.lang.Math.sqrt(x*x + y*y));
		boolean inOK = false;
		boolean inOval = false;
		boolean inRect = false;
		
		if(r <= CENTER_X) {
			if(r > CENTER_X - CENTER_RADIUS)
				inOval = true;					
			else if(x >= OK_X0 && x < OK_X1 && y >= OK_X0 && y < OK_X1)
				inRect = true;
		}
		else if(x >= OK_X0 && x < OK_X1 && y >= OK_Y0 && y < OK_Y1){
			inOK = true;
		}
			
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTrackingOK = inOK;
				if (inOK) {
					mHighlightOK = true;
					invalidate();
					break;
				}
			case MotionEvent.ACTION_MOVE:
				if (mTrackingOK) {
					if (mHighlightOK != inOK) {
						mHighlightOK = inOK;
						invalidate();
					}
				} else if(inOval) {
					float angle = (float)java.lang.Math.atan2(y, x);
					// need to turn angle [-PI ... PI] into unit [0....1]
					float unit = angle/(2*PI);
					if (unit < 0) {
						unit += 1;
					}
					selectColor = interpColor(mColors, unit);
					mOKPaint.setColor(selectColor);
					//mChroma[1] = selectColor;
					selectHue = getHue(selectColor);
					//lg = new LinearGradient(OK_X0, 0, OK_X1, 0, mChroma, null, Shader.TileMode.CLAMP);
					//mPaintC.setShader(lg);
					// Log.d("色相", "" + selectHue);
					invalidate();
				} else if(inRect){
					int selectColor2 = setHSVColor(selectHue, (x - OK_X0)/CENTER_X, (y - OK_X0)/CENTER_Y);
					selectColor = selectColor2;
					mOKPaint.setColor(selectColor);
					// Log.d("彩度、明度", "" + (x - OK_X0)/CENTER_X + ", " + (y - OK_X0)/CENTER_Y);
					invalidate();
				}
				// 親（画面）に通知する。通知後はボタンの増減処理。mListener
				float f[] = {0, 0, 0};
				Color.HSVToColor(f);
				// textView1.setText(selectHue + ", " + (x - OK_X0)/CENTER_X + ", " + (y - OK_X0)/CENTER_Y);
				int h = (int) selectHue;
				int s = (int) (100.0 * (x - OK_X0)/CENTER_X);
				int v = (int) (100.0 * (y - OK_X0)/CENTER_Y);
				if (inOval) {
					s = 100;
					v = 100;
				}
				
				MyHSV hsvObj = new MyHSV(h, s, v);
				Log.d("time", "start:" + hsvObj.hashCode());
				Log.d("time", "start:" + hsvObj);
				
				if (myTask != null) {
					// スレッドの中止。
			    	Log.d("time3", "onCancelled1!!!!!!!!" + hsvObj);
					myTask.cancel(true);
					SystemClock.sleep(20);
				}
				myTask = new MyTask(activity, colorDataList, layout, hsvObj);
				myTask.execute((Void)null);
				
				Log.d("time", "end:" + hsvObj.hashCode());
				break;
			case MotionEvent.ACTION_UP:
				if (mTrackingOK) {
					if (inOK) {
						// mListener.colorChanged(mOKPaint.getColor());
						Log.d("OK", "OK");
						// myThread.shutdownRequested = true;
						layout.removeAllViews();
					}
					mTrackingOK = false;	// so we draw w/o halo
					invalidate();
				}
				break;
		}
		return true;
	}
}
