package com.gmail.nelsonr462.opin.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gmail.nelsonr462.opin.helpers.ParseConstants;
import com.gmail.nelsonr462.opin.R;
import com.nineoldandroids.animation.Animator;


public class SurveyWebViewActivity extends AppCompatActivity {
    private String mSurveyUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_web_view);

        final ProgressBar webProgressBar = (ProgressBar) findViewById(R.id.webViewProgressBar);
        final WebView surveyWebView = (WebView) findViewById(R.id.surveyWebView);
        final RelativeLayout webRelativeLayout = (RelativeLayout) findViewById(R.id.surveyWebViewRelativeLayout);

        webProgressBar.setVisibility(View.VISIBLE);
        mSurveyUrl = getIntent().getStringExtra("surveyUrl");

        surveyWebView.getSettings().setJavaScriptEnabled(true);
        surveyWebView.setVisibility(View.INVISIBLE);
        surveyWebView.setBackgroundColor(Color.rgb(
                ParseConstants.COLOR_LOADER_BACKGROUND[0],
                ParseConstants.COLOR_LOADER_BACKGROUND[1],
                ParseConstants.COLOR_LOADER_BACKGROUND[2]
        ));
        surveyWebView.loadUrl(mSurveyUrl);

        surveyWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                YoYo.with(Techniques.SlideOutDown)
                        .duration(300)
                        .withListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                SurveyWebViewActivity.this.finish();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .playOn(webRelativeLayout);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webProgressBar.setVisibility(View.INVISIBLE);
                surveyWebView.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInUp)
                        .duration(300)
                        .playOn(surveyWebView);
            }
        });

    }


}
