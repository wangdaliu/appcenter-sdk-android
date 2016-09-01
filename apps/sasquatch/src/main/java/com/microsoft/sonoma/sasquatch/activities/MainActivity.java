package com.microsoft.sonoma.sasquatch.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.sonoma.analytics.Analytics;
import com.microsoft.sonoma.core.Sonoma;
import com.microsoft.sonoma.core.utils.UUIDUtils;
import com.microsoft.sonoma.errors.AbstractErrorReportingListener;
import com.microsoft.sonoma.errors.ErrorAttachments;
import com.microsoft.sonoma.errors.ErrorReporting;
import com.microsoft.sonoma.errors.model.ErrorAttachment;
import com.microsoft.sonoma.errors.model.ErrorReport;
import com.microsoft.sonoma.sasquatch.R;
import com.microsoft.sonoma.sasquatch.features.TestFeatures;
import com.microsoft.sonoma.sasquatch.features.TestFeaturesListAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Sonoma.setLogLevel(Log.VERBOSE);
        ErrorReporting.setListener(new AbstractErrorReportingListener() {
            @Override
            public boolean shouldAwaitUserConfirmation() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setTitle(R.string.crash_confirmation_dialog_title)
                        .setMessage(R.string.crash_confirmation_dialog_message)
                        .setPositiveButton(R.string.crash_confirmation_dialog_send_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ErrorReporting.notifyUserConfirmation(ErrorReporting.SEND);
                            }
                        })
                        .setNegativeButton(R.string.crash_confirmation_dialog_not_send_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ErrorReporting.notifyUserConfirmation(ErrorReporting.DONT_SEND);
                            }
                        })
                        .setNeutralButton(R.string.crash_confirmation_dialog_always_send_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ErrorReporting.notifyUserConfirmation(ErrorReporting.ALWAYS_SEND);
                            }
                        });
                builder.create().show();
                return true;
            }

            @Override
            public ErrorAttachment getErrorAttachment(ErrorReport errorReport) {
                return ErrorAttachments.attachment("This is a text attachment.", "This is a binary attachment.".getBytes(), "binary.txt", "text/plain");
            }

            @Override
            public void onSendingFailed(ErrorReport errorReport, Exception e) {
                Toast.makeText(MainActivity.this, R.string.crash_sent_failed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSendingSucceeded(ErrorReport errorReport) {
                String message = String.format("%s\nCrash ID: %s\nThrowable: %s", R.string.crash_sent_succeeded, errorReport.getId(), errorReport.getThrowable().toString());
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

        Sonoma.start(getApplication(), UUIDUtils.randomUUID().toString(), Analytics.class, ErrorReporting.class);

        TestFeatures.initialize(this);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new TestFeaturesListAdapter(TestFeatures.getAvailableControls()));
        listView.setOnItemClickListener(TestFeatures.getOnItemClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return true;
    }
}