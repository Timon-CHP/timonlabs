package com.gkxim.timon.labs.androider;

import java.io.FileNotFoundException;

import com.gkxim.timon.labs.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BlurBitmapActivity extends Activity {

	Button btnLoadImage;
	ImageView imageResult, imageOriginal;
	TextView textDur;

	final int RQS_IMAGE1 = 1;

	Uri source;
	Bitmap bitmapMaster;
	Canvas canvasMaster;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blurbitmap);

		btnLoadImage = (Button) findViewById(R.id.loadimage);
		imageResult = (ImageView) findViewById(R.id.result);
		imageOriginal = (ImageView)findViewById(R.id.original);
		textDur = (TextView)findViewById(R.id.dur);

		btnLoadImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, RQS_IMAGE1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case RQS_IMAGE1:
				source = data.getData();

				try {
					bitmapMaster = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									source));
					
					imageOriginal.setImageBitmap(bitmapMaster);
					loadBlurBitmap(bitmapMaster);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
		}
	}

	private void loadBlurBitmap(Bitmap src) {
		if (src != null) {
			
			Bitmap bmBlur;
			
			long startTime = System.currentTimeMillis();
			bmBlur = getBlurBitmap(src);
			long duration = System.currentTimeMillis() - startTime;
			
			imageResult.setImageBitmap(bmBlur);
			textDur.setText("processing duration(ms): " + duration);
		}
	}

	private Bitmap getBlurBitmap(Bitmap src) {
		
		final int widthKernal = 5;
		final int heightKernal = 5;

		int w = src.getWidth();
		int h = src.getHeight();

		Bitmap blurBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {

				int r = 0;
				int g = 0;
				int b = 0;
				int a = 0;
				
				for (int xk = 0; xk < widthKernal; xk++) {
					for (int yk = 0; yk < heightKernal; yk++) {
						int px = x + xk -2;
						int py = y + yk -2;

						if(px < 0){
							px = 0;
						}else if(px >= w){
							px = w-1;
						}
						
						if(py < 0){
							py = 0;
						}else if(py >= h){
							py = h-1;
						}
						
						int intColor = src.getPixel(px, py);
						r += Color.red(intColor);
						g += Color.green(intColor);
						b += Color.blue(intColor);
						a += Color.alpha(intColor);
						
					}
				}
				
				blurBitmap.setPixel(x, y, Color.argb(a/25, r/25, g/25, b/25));

			}
		}

		return blurBitmap;
	}

}