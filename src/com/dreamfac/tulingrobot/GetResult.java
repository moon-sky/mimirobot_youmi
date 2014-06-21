package com.dreamfac.tulingrobot;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

class GetResult extends Thread {
	String input;
	Handler handler;
	public static GetResult mResult = null;

	public static GetResult getInstance(String input, Handler handler) {
		if (mResult == null)
			mResult = new GetResult(input, handler);
		return mResult;

	}

	public GetResult(String input, Handler handler) {
		this.input = input;
		this.handler = handler;
	}

	@Override
	public void run() {
		getHttpResult(input, handler);
		super.run();
	}

	public void getHttpResult(String input, Handler handler) {/*
		HttpGet get = new HttpGet(MainActivity.url + MainActivity.key
				+ "&info=" + input);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);
			this.interrupt();
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
	*/}

}
