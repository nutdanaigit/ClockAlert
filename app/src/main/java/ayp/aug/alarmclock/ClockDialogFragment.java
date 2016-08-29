package ayp.aug.alarmclock;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Nutdanai on 8/24/2016.
 */
public class ClockDialogFragment extends DialogFragment {
    private static final String TAG = "ClockDialogFragment";
    EditText mEditText;
    TimePicker mTimePicker;
    Switch aSwitch;
    Clock mClock;
    ClockLab mClockLab;
    UUID uuid;
    int hourEdit;
    int minuteEdit;
    int currentApiVersion = Build.VERSION.SDK_INT;

    private static final int REQUEST_CODE = 77;

    public static ClockDialogFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable("ARG_TIME", uuid);
        ClockDialogFragment fragment = new ClockDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.clock_layout_set, null);
        mClockLab = ClockLab.getInstance(getActivity());
        mClock = Clock.newInstanceClock();
        mClock = mClockLab.getClockById(uuid);
        Calendar calendar = Calendar.getInstance();


        mEditText = (EditText) v.findViewById(R.id.title_name_editTxt);
        mTimePicker = (TimePicker) v.findViewById(R.id.timePicker);
        aSwitch = (Switch) v.findViewById(R.id.switch_check);

        if (mClock != null) {
            calendar.setTime(mClock.getTime());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
            mEditText.setText(mClock.getTitle());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mClock == null) {
                    mClock = new Clock();
                }
                Intent intent = new Intent();

                Calendar calendar = Calendar.getInstance();

                if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    hourEdit = mTimePicker.getHour();
                    minuteEdit = mTimePicker.getMinute();
                } else {
                    hourEdit = mTimePicker.getCurrentHour();
                    minuteEdit = mTimePicker.getCurrentMinute();
                }

                calendar.set(Calendar.HOUR_OF_DAY, hourEdit);
                calendar.set(Calendar.MINUTE, minuteEdit);
                calendar.set(Calendar.SECOND, 0);


                String titleText = (mEditText.getText().toString().equals("")) ? "" : mEditText.getText().toString();
                mClock.setTitle(titleText);
                mClock.setTime(calendar.getTime());
                mClock.setCheck(true);
                if(!mClock.getUuid().equals(uuid)) {
                    mClockLab.addList(mClock);
                }
                ClockService.setServiceAlarm(getActivity(), mClock, mClock.isCheck());
                getTargetFragment().onActivityResult(REQUEST_CODE, Activity.RESULT_OK, intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

}
