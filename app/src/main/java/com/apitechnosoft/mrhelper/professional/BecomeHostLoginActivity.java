package com.apitechnosoft.mrhelper.professional;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.mrhelper.R;
import com.apitechnosoft.mrhelper.activity.BecomeHostActivity;
import com.apitechnosoft.mrhelper.activity.HomeActivity;
import com.apitechnosoft.mrhelper.circlecustomprogress.CircleDotDialog;
import com.apitechnosoft.mrhelper.database.DbHelper;
import com.apitechnosoft.mrhelper.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.mrhelper.framework.ServiceCaller;
import com.apitechnosoft.mrhelper.models.ContentMybooking;
import com.apitechnosoft.mrhelper.models.ContentResponce;
import com.apitechnosoft.mrhelper.models.User;
import com.apitechnosoft.mrhelper.utilities.CompatibilityUtility;
import com.apitechnosoft.mrhelper.utilities.Contants;
import com.apitechnosoft.mrhelper.utilities.FontManager;
import com.apitechnosoft.mrhelper.utilities.Utility;
import com.google.gson.Gson;

public class BecomeHostLoginActivity extends AppCompatActivity {
    Typeface materialdesignicons_font;
    EditText edt_phone, edt_password;
    String passwordstr, phoneStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_host_login);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        initView();
    }

    private void initView() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView phone_icon = (TextView) findViewById(R.id.phone_icon);
        phone_icon.setTypeface(materialdesignicons_font);
        phone_icon.setText(Html.fromHtml("&#xf3f2;"));
        TextView password_icon = (TextView) findViewById(R.id.password_icon);
        password_icon.setTypeface(materialdesignicons_font);
        password_icon.setText(Html.fromHtml("&#xf33e;"));

        //TextView arrowicon = (TextView) findViewById(R.id.arrowicon);
        //arrowicon.setTypeface(materialdesignicons_font);
        // arrowicon.setText(Html.fromHtml("&#xf054;"));
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        edt_password = (EditText) findViewById(R.id.edt_password);
        LinearLayout loginbutton = (LinearLayout) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    callLogin();
                }
            }
        });
        LinearLayout signupbutton = (LinearLayout) findViewById(R.id.signupbutton);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BecomeHostLoginActivity.this, BecomeHostActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(BecomeHostLoginActivity.this)) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    // ----validation -----
    private boolean isValidate() {
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        phoneStr = edt_phone.getText().toString();
        passwordstr = edt_password.getText().toString();

        if (phoneStr.length() == 0) {
            showToast("Please enter phone number");
            return false;
        } else if (phoneStr.length() != 10) {
            showToast("Please enter valid phone number");
            return false;
        } else if (passwordstr.length() == 0) {
            showToast("Please enter password");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(BecomeHostLoginActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void callLogin() {
        if (Utility.isOnline(this)) {
            final CircleDotDialog dotDialog = new CircleDotDialog(BecomeHostLoginActivity.this);
            dotDialog.show();

            ServiceCaller serviceCaller = new ServiceCaller(this);
            serviceCaller.callHostLoginSrvice(phoneStr, passwordstr, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                getProfessionalInfo();
                            } else {
                                Toast.makeText(BecomeHostLoginActivity.this, "Login not Successfully!", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, BecomeHostLoginActivity.this);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, this);//off line msg....
        }

    }

    //get Professional user Info
    private void getProfessionalInfo() {
        if (Utility.isOnline(this)) {
            final CircleDotDialog dotDialog = new CircleDotDialog(BecomeHostLoginActivity.this);
            dotDialog.show();

            ServiceCaller serviceCaller = new ServiceCaller(this);
            serviceCaller.callGetProfessionalInfoService(phoneStr, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentMybooking data = new Gson().fromJson(result, ContentMybooking.class);
                        if (data != null) {
                            if (data.getPartnerDetailsForPartner() != null) {
                                DbHelper dbHelper = new DbHelper(BecomeHostLoginActivity.this);
                                dbHelper.deleteProfessionalData();
                                dbHelper.insertProfessionalUserData(data.getPartnerDetailsForPartner());
                                Toast.makeText(BecomeHostLoginActivity.this, "Login Successfully.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(BecomeHostLoginActivity.this, ProfessionalWorkSheetActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(BecomeHostLoginActivity.this, "Login not Successfully!", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Utility.alertForErrorMessage(Contants.Error, BecomeHostLoginActivity.this);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, this);//off line msg....
        }

    }
}
