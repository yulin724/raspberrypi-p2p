package com.starxteam.raspi.p2pledcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.p2p.pppp_api.PPPP_APIs;

public class P2PLedControlActivity extends Activity {

	public static final int DEVICE_CONNECTED = 0x0;
	public static final int DEVICE_CONNECT_FAILED = 0x1;

	private TextView m_text_apiver;
	private Button m_button_turnon;
	private Button m_button_turnoff;
	private Button m_button_exit;
	private Button m_button_connect;
	private EditText m_edittext_did;

	private Handler mHandler;
	P2PDev m_p2pdevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_p2p_led_control);

		initViews();
		setListeners();
		initHandler();

		m_p2pdevice = new P2PDev(mHandler);

		String apiver;
		int n = PPPP_APIs.ms_verAPI;
		apiver = String.format("API ver: %d.%d.%d.%d", (n >> 24) & 0xff,
				(n >> 16) & 0xff, (n >> 8) & 0xff, n & 0xff);
		m_text_apiver.setText(apiver);

	}

	private void initHandler() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == DEVICE_CONNECTED) {
					m_text_apiver.setText("DEVICE CONNECTED, Mode: " + (String)(msg.obj));
					m_button_connect.setEnabled(false);
					m_button_turnoff.setEnabled(true);
					m_button_turnon.setEnabled(true);

					m_button_connect.setText("Disconnect");
					m_button_connect.setEnabled(true);

				} else if (msg.what == DEVICE_CONNECT_FAILED) {
					m_text_apiver.setText("DEVICE CONNECTED FAILED.");
					m_button_connect.setText("Connect");
					m_button_connect.setEnabled(true);
				}

				super.handleMessage(msg);
			}

		};
	}

	private void setListeners() {
		m_button_turnon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				byte[] DataBuf = new byte[1];
				DataBuf[0] = 1;
				m_p2pdevice.write(DataBuf, 1);
			}
		});

		m_button_turnoff.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				byte[] DataBuf = new byte[1];
				DataBuf[0] = 0;
				m_p2pdevice.write(DataBuf, 1);
			}
		});

		m_button_exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				byte[] DataBuf = new byte[1];
				DataBuf[0] = 2;
				m_p2pdevice.write(DataBuf, 1);

				m_p2pdevice.disconnDev();
				P2PDev.deinitAll();
				finish();
			}
		});

		m_button_connect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (m_button_connect.getText().equals("Connect")) {
					m_button_connect.setText("Connecting");
					m_button_connect.setEnabled(false);

					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {
							P2PDev.initAll();

							String did = m_edittext_did.getEditableText()
									.toString();
							m_p2pdevice.connectDev(did);
						}
					});
					t.start();
				} else {
					byte[] DataBuf = new byte[1];
					DataBuf[0] = 2;
					m_p2pdevice.write(DataBuf, 1);

					m_p2pdevice.disconnDev();

					m_button_connect.setText("Connect");
					m_button_connect.setEnabled(true);

				}
			}
		});
	}

	private void initViews() {
		m_text_apiver = (TextView) findViewById(R.id.text_apiver);

		m_button_connect = (Button) findViewById(R.id.button_connect);

		m_button_turnon = (Button) findViewById(R.id.button_turnon);
		m_button_turnoff = (Button) findViewById(R.id.button_turnoff);

		m_button_exit = (Button) findViewById(R.id.button_exit);

		m_edittext_did = (EditText) findViewById(R.id.editText_did);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_file_transfer, menu);
		return true;
	}

}
