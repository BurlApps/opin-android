package com.gmail.nelsonr462.opin.ui;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gmail.nelsonr462.opin.helpers.ParseConstants;
import com.gmail.nelsonr462.opin.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddClassFragment extends android.support.v4.app.Fragment {

    @Bind(R.id.classCodeEditText) EditText mClassCode;
    @Bind(R.id.addClassProgressBar) ProgressBar mProgressBar;
    @Bind(R.id.addClassButton) Button mAddClassButton;

    public AddClassFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_class, container, false);
        ButterKnife.bind(this, rootView);

//        mClassCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        EditText classCodeEditText = (EditText) getView().findViewById(R.id.classCodeEditText);
        classCodeEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        Button addClassButton = (Button) getView().findViewById(R.id.addClassButton);
        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClass();
            }
        });
    }

    public void addClass() {
        final EditText classCodeEditText = (EditText) getView().findViewById(R.id.classCodeEditText);
        final Button addClassButton = (Button) getView().findViewById(R.id.addClassButton);
        final ProgressBar addClassProgressBar = (ProgressBar) getView().findViewById(R.id.addClassProgressBar);


        if (!ParseConstants.isNetworkAvailable) {
            Toast.makeText(getActivity(), "Network unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        addClassButton.setEnabled(false);
//        mAddClassButton.setEnabled(false);
        addClassProgressBar.setVisibility(View.VISIBLE);
//        mProgressBar.setVisibility(View.VISIBLE);

        String classCode = /*mClassCode*/classCodeEditText.getText().toString();
        classCode = classCode.replaceAll("[^A-Za-z0-9]", "");
        classCode = classCode.toLowerCase();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_CLASS);
        query.whereEqualTo(ParseConstants.KEY_CODE, classCode);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> classes, ParseException e) {
                /*mProgressBar*/addClassProgressBar.setVisibility(View.INVISIBLE);
                /*mAddClassButton*/addClassButton.setEnabled(true);
                /*mClassCode*/classCodeEditText.setText("");

                if(e == null && classes.size() > 0) {
                    ParseObject classObject = classes.get(0);
                    ParseRelation<ParseInstallation> relation = classObject.getRelation(ParseConstants.KEY_STUDENTS_RELATION);
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    relation.add(installation);
                    classObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                alertUser(1);
                            } else {
                                Toast.makeText(getActivity(), R.string.success_toast_add_class, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    alertUser(0);
                }
            }
        });
    }

    private void alertUser(int errorType) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        if (errorType == 0) {
            builder.setMessage(R.string.add_class_error_message)
                    .setTitle(R.string.error_title)
                    .setPositiveButton(android.R.string.ok, null);
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        } else if (errorType == 1) {
            builder.setMessage(R.string.save_add_class_error_message)
                    .setTitle(R.string.error_title)
                    .setPositiveButton(android.R.string.ok, null);
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}
