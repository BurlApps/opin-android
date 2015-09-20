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


public class AddClassFragment extends android.support.v4.app.Fragment {
    private View mView;

    public AddClassFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_class, container, false);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        final EditText classCodeEditText = (EditText) mView.findViewById(R.id.classCodeEditText);
        classCodeEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        Button addClassButton = (Button) mView.findViewById(R.id.addClassButton);
        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classCodeEditText.clearFocus();
                addClass();
            }
        });
    }

    public void addClass() {
        final EditText classCodeEditText = (EditText) mView.findViewById(R.id.classCodeEditText);
        final Button addClassButton = (Button) mView.findViewById(R.id.addClassButton);
        final ProgressBar addClassProgressBar = (ProgressBar) mView.findViewById(R.id.addClassProgressBar);

        if (!ParseConstants.isNetworkAvailable) {
            Toast.makeText(getActivity(), R.string.network_unavailable_message, Toast.LENGTH_SHORT).show();
            return;
        }

        addClassButton.setEnabled(false);
        addClassProgressBar.setVisibility(View.VISIBLE);

        String classCode = classCodeEditText.getText().toString();
        classCode = classCode.replaceAll("[^A-Za-z0-9]", "");
        classCode = classCode.toLowerCase();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_CLASS);
        query.whereEqualTo(ParseConstants.KEY_CODE, classCode);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> classes, ParseException e) {
                addClassProgressBar.setVisibility(View.INVISIBLE);
                addClassButton.setEnabled(true);
                classCodeEditText.setText("");

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
