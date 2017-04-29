package com.dajia.activity;

import net.k76.wzd.R;

import android.os.Bundle;
import android.widget.TextView;

public class ApplyInvoiceActivity extends BaseActivity {
	
	private TextView applytitle;
	private TextView title;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_applyinvoice);
	
	title = (TextView) findViewById(R.id.title);
	title.setText("发票申请");
	
	applytitle = (TextView) findViewById(R.id.tv_apply_invoice_title);
	applytitle.setText("39元代驾费");
	
	
}
}
