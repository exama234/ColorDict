package jp.co.fexd.color;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	ColorPickerView customView1 = null;
	TextView textView1 = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main);
		
		customView1 = (ColorPickerView) this.findViewById(R.id.CustomView1);
		LinearLayout linearLayout1 = (LinearLayout) this.findViewById(R.id.LinerLayout1);
		customView1.setParentActivity(this);
		customView1.setLayout(linearLayout1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	
}
