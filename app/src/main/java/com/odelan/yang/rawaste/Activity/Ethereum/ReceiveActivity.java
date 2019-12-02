package com.odelan.yang.rawaste.Activity.Ethereum;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveActivity extends AppCompatActivity {

    @BindView(R.id.img_qrcode) ImageView img_qrcode;
    @BindView(R.id.txt_address) TextView txt_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        ButterKnife.bind(this);
        setActivity();
    }

    void setActivity() {
        txt_address.setText(AppData.user.address);
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix  = writer.encode(AppData.user.address.substring(2), BarcodeFormat.QR_CODE, 300, 300);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            if(bmp != null) {
                img_qrcode.setImageBitmap(bmp);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
}
