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

import butterknife.Bind;
import butterknife.ButterKnife;

public class SurveyWebViewActivity extends AppCompatActivity {
    private String mSurveyUrl;

    @Bind(R.id.surveyWebView) WebView mSurveyWebView;
    @Bind(R.id.webViewProgressBar) ProgressBar mProgressBar;
    @Bind(R.id.surveyWebViewRelativeLayout) RelativeLayout mRelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_web_view);
        ButterKnife.bind(this);

        final ProgressBar webProgressBar = (ProgressBar) findViewById(R.id.webViewProgressBar);
        final WebView surveyWebView = (WebView) findViewById(R.id.surveyWebView);
        final RelativeLayout webRelativeLayout = (RelativeLayout) findViewById(R.id.surveyWebViewRelativeLayout);

        /*mProgressBar*/webProgressBar.setVisibility(View.VISIBLE);
        mSurveyUrl = getIntent().getStringExtra("surveyUrl");

        /*mSurveyWebView*/surveyWebView.getSettings().setJavaScriptEnabled(true);
        /*mSurveyWebView*/surveyWebView.setVisibility(View.INVISIBLE);
        /*mSurveyWebView*/surveyWebView.setBackgroundColor(Color.rgb(
                ParseConstants.COLOR_LOADER_BACKGROUND[0],
                ParseConstants.COLOR_LOADER_BACKGROUND[1],
                ParseConstants.COLOR_LOADER_BACKGROUND[2]
        ));
        /*mSurveyWebView*/surveyWebView.loadUrl(mSurveyUrl);

        /*mSurveyWebView*/surveyWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                mSurveyWebView.setVisibility(View.INVISIBLE);
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
                /*mProgressBar*/webProgressBar.setVisibility(View.INVISIBLE);
                /*mSurveyWebView*/surveyWebView.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInUp)
                        .duration(300)
                        .playOn(/*mSurveyWebView*/surveyWebView);
            }
        });

    }


}
