package lab.s2jh.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.zxing.activity.CaptureActivity;

public class MainActivity extends Activity {

	private String externalInvokerId;
	private WebView mWebView;
	private EditText textUrl;
	private Button btnGo;
	private Button btnBarcodeScan;

	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_main);

		this.mWebView = ((WebView) findViewById(R.id.webview));

		WebSettings mWebSettings = this.mWebView.getSettings();
		mWebSettings.setUseWideViewPort(true);
		mWebSettings.setLoadWithOverviewMode(true);
		mWebSettings.setAllowFileAccess(false);
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setDefaultTextEncodingName("utf-8");

		this.mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});

		this.mWebView.addJavascriptInterface(this, "wst");

		btnBarcodeScan = (Button) findViewById(R.id.btnBarcodeScan);
		// btnBarcodeScan.setVisibility(View.INVISIBLE);
		btnBarcodeScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startupBarcodeScan("barcode");
			}
		});

		textUrl = (EditText) findViewById(R.id.textUrl);
		textUrl.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					btnGo.callOnClick();
					return true;
				}
				return false;
			}
		});

		btnGo = (Button) findViewById(R.id.btnGo);
		btnGo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = textUrl.getText().toString();
				mWebView.loadUrl(url);
			}
		});
		btnGo.callOnClick();
	}

	public void startupBarcodeScan(String elementId) {
		System.out.println("Scan ExternalInvokerId: " + elementId);
		externalInvokerId = elementId;
		Intent openCameraIntent = new Intent(MainActivity.this,
				CaptureActivity.class);
		startActivityForResult(openCameraIntent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");

			String scripts = "javascript:scanBarcodeCallback('"
					+ externalInvokerId + "','" + scanResult + "')";
			System.out.println("Load :" + scripts);
			mWebView.loadUrl(scripts);
		}
	}
}
