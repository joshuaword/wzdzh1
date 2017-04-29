package com.dajia.activity;

import net.k76.wzd.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ApplyInvoiceBaseActivity extends BaseActivity implements
		OnClickListener {

	private TextView applytitle;
	private TextView title;
	private LinearLayout btn_invoiceApply;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_invoice_base);

		title = (TextView) findViewById(R.id.title);
		title.setText("发票申请");

		applytitle = (TextView) findViewById(R.id.tv_apply_invoice_title);
		applytitle.setText("最大可开票额39元");

		btn_invoiceApply = (LinearLayout) findViewById(R.id.btn_invoiceApply);
		btn_invoiceApply.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_invoiceApply:

			Intent intent = new Intent(ApplyInvoiceBaseActivity.this,
					ApplyInvoiceActivity.class);
			startActivity(intent);

			break;

		default:
			break;
		}
	}
}
