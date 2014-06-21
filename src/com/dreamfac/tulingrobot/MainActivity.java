package com.dreamfac.tulingrobot;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
	LinearLayout ll_parent;
	RelativeLayout rl_right_bubble;
	RelativeLayout rl_left_bubble;
	ScrollView sv_content;
	TextView tv_left, tv_right;
	static final String key = "60a4f78f5391dcde946cc8c5d00f7b52";
	String url = "http://www.tuling123.com/openapi/api?key=";
	EditText et_content;
	Button btn_submit;
	LayoutInflater inflater;
	private TextView tv_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initAd();
		initYoumi();
		ll_parent = (LinearLayout) findViewById(R.id.ll_content);
		et_content = (EditText) findViewById(R.id.et_input);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		sv_content = (ScrollView) findViewById(R.id.sv_content);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addLeftBubble("欢饮你的到来！我的主人");
		btn_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				addRightBubble(et_content.getText().toString());
				new GetResult(et_content.getText().toString(), mHandler)
						.start();
				et_content.setText("");
			}
		});
	}

	private void initAd() {/*
		AppConnect.getInstance("984f057f45845a991e31753e853da60c", "default",
				this);
		AppConnect.getInstance(this).initFunAd(this);
		String uriString = "http://www.waps.cn";
		AppConnect.getInstance(this).showBrowser(this, uriString);
	*/}

	private void initYoumi() {
		AdManager.getInstance(this).init("4b2766e4657d1346",
				"3938da652141af39", false);
		SpotManager.getInstance(this).loadSpotAds();
	}

	private String getTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"------yyyy-MM-dd HH:mm:ss------");
		Date curDate = new Date(System.currentTimeMillis());
		String time = dateFormat.format(curDate);
		return time;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			addLeftBubble((String) msg.obj);
		};

	};

	class GetResult extends Thread {
		String input;
		Handler handler;

		public GetResult(String input, Handler handler) {
			this.input = input;
			this.handler = handler;
		}

		@Override
		public void run() {
			getHttpResult(input, handler);
			super.run();
		}
	}

	@Override
	protected void onResume() {
		SpotManager.getInstance(this).setSpotTimeout(5000); // 5秒
		SpotManager.getInstance(this).showSpotAds(this);
		SpotManager.getInstance(this).setShowInterval(200);
		SpotManager.getInstance(this).setAutoCloseSpot(true);// 设置自动关闭插屏开关
		SpotManager.getInstance(this).setCloseTime(6000);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		super.onDestroy();
	}

	public void getHttpResult(String input, Handler handler) {
		HttpGet get = new HttpGet(url + key + "&info=" + input);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result;
				result = EntityUtils.toString(response.getEntity());

				JSONObject jsObj = new JSONObject(result);

				if (jsObj.getString("text") != null) {
					handler.obtainMessage(1, jsObj.getString("text"))
							.sendToTarget();
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addLeftBubble(String text) {
		View leftBubble = inflater.inflate(R.layout.left_bubble, null);
		TextView tv_bubble = (TextView) leftBubble.findViewById(R.id.tv_left);
		tv_time = (TextView) leftBubble.findViewById(R.id.tv_time);
		tv_time.setText(getTime());
		tv_bubble.setText(text);
		ll_parent.addView(leftBubble);
		ll_parent.invalidate();
		ll_parent.getBottom();

		View view = ll_parent.getChildAt(ll_parent.getChildCount() - 1);
		mHandler.postDelayed(new ScrollToPostion(ll_parent.getBottom()), 300);

	}

	class ScrollToPostion implements Runnable {

		View mView;
		int bottom;

		public ScrollToPostion(View view) {
			this.mView = view;
		};

		public ScrollToPostion(int bottom) {
			this.bottom = bottom;
		};

		@Override
		public void run() {
			sv_content.smoothScrollBy(0, bottom);

		}

	}

	private void addRightBubble(String text) {

		View leftBubble = inflater.inflate(R.layout.right_bubble, null);
		TextView tv_bubble = (TextView) leftBubble.findViewById(R.id.tv_right);
		tv_bubble.setText(text);
		ll_parent.addView(leftBubble);
	}

}
