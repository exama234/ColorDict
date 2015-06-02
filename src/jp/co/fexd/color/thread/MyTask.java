package jp.co.fexd.color.thread;

import java.util.Vector;

import jp.co.fexd.color.MyOnClickListener;
import jp.co.fexd.color.data.ColorData;
import jp.co.fexd.color.data.ColorDataList;
import jp.co.fexd.color.data.MyHSV;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyTask extends AsyncTask<Object, Object, Object> {
	private Activity activity;
	private ColorDataList colorDataList;
	private LinearLayout layout;
	private MyHSV hsvObj;

    public MyTask(Activity activity, ColorDataList colorDataList, LinearLayout layout, MyHSV hsvObj) {
		this.activity = activity;
		this.colorDataList = colorDataList;
		this.layout = layout;
		this.hsvObj = hsvObj;
    }

    /** 非同期処理 */
	@Override
	protected Object doInBackground(Object... arg0) {
        long start = System.currentTimeMillis();

        // 複数スレッドで同時に処理されないように保護する。
        synchronized (activity) {
            // 処理にかかった時間を返す
            return System.currentTimeMillis() - start;
        }
    }

    /** doInBackground が終わったら呼び出される。 */
	@Override
    protected void onPostExecute(Object  result) {
        // 結果を表示 "タスク名 - カウンタ値 - 処理時間ms"
		Log.d("time3", "start:" + hsvObj.hashCode());
		layout.removeAllViews();
		Vector<ColorData> list = colorDataList.getNearColorList(hsvObj);
		for (ColorData colorData : list) {
            // キャンセルされたら抜ける
            if (this.isCancelled()) {
            	Log.d("time3", "Cancel" + hsvObj);
                return ;
            }
            
			TextView tv1 = new TextView(activity);
//				tv1.setBackgroundColor(selectColor);
			tv1.setBackgroundColor(colorData.getHsv().getColor());
			tv1.setWidth(60);
			tv1.setHeight(60);
			// tv1.setTag(colorData.getHsv().toRGB().toColorCode());
			tv1.setOnClickListener(new MyOnClickListener(activity));
			
			TextView tv2 = new TextView(activity);
			tv2.setWidth(320);
			tv2.setHeight(60);
			tv2.setPadding(10, 0, 10, 0);
			// tv2.setTag(colorData.getHsv().toRGB().toColorCode());
			tv2.setOnClickListener(new MyOnClickListener(activity));
			StringBuffer buffer = new StringBuffer();
			buffer.append(colorData.getName() + "(" + colorData.getKana() + ")<br>");
			buffer.append(colorData.getHsv().toRGB().toColorCode());
			buffer.append(" - ");
			buffer.append(colorData.getHsv().toString());
			if (colorData.existDiscription()) {
				buffer.append(" <font color='fuchsia'>(解説)</font>");
				tv1.setTag(colorData);
				tv2.setTag(colorData);
			}
			tv2.setText(Html.fromHtml(buffer.toString()));
			tv2.setTextColor(Color.WHITE);
			tv2.setHighlightColor(Color.BLACK);
			
	        StateListDrawable drawables = new StateListDrawable();
	        int statePressed=android.R.attr.state_pressed;
	        drawables.addState(new int[]{statePressed}, new ColorDrawable( Color.BLACK));
	        drawables.addState(new int[]{            }, new ColorDrawable( Color.GRAY));
	        tv2.setBackgroundDrawable(drawables);
			
			LinearLayout tmpLay = new LinearLayout(activity);
			tmpLay.setOrientation(LinearLayout.HORIZONTAL);
			tmpLay.setPadding(0, 0, 0, 1);
			tmpLay.setGravity(Gravity.CENTER_VERTICAL);
			tmpLay.addView(tv1);
			tmpLay.addView(tv2);
			layout.addView(tmpLay);
		}
		Log.d("time3", "end:" + hsvObj.hashCode());
		Log.d("time3", "Fin!!!!!!!!!" + hsvObj);
    }

	
    /** cancel() がコールされると呼び出される。 */
    @Override
    protected void onCancelled() {
    	Log.d("time3", "onCancelled2!!!!!!!!" + hsvObj);
    }
}
