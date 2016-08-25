package ayp.aug.alarmclock;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    private static final int REQUEST_CODE =77;

    public static ClockDialogFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable("ARG_TIME",date);
        ClockDialogFragment fragment = new ClockDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.clock_layout_set,null);
        Date date = (Date) getArguments().getSerializable("ARG_TIME");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);


        mEditText = (EditText) v.findViewById(R.id.title_name_editTxt);
        mTimePicker = (TimePicker) v.findViewById(R.id.timePicker);
        aSwitch = (Switch) v.findViewById(R.id.switch_check);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);

        mClockLab = ClockLab.getInstance(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mClock = new Clock();
                Intent intent = new Intent();

                Calendar calendar =  Calendar.getInstance();

                int hour = mTimePicker.getHour();
                int minute = mTimePicker.getMinute();
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);
//                calendar.set(Calendar.AM_PM,);


                Log.d(TAG,"Date" + calendar.getTime());
                mClock.setTitle(mEditText.getText().toString());
                mClock.setTime(calendar.getTime());
                mClock.setCheck(true);
                Log.d(TAG,"Dialog Hour onClick : " +hour);
                Log.d(TAG,"Dialog Minute onClick : " +minute);

                mClockLab.addList(mClock);
//                ClockService.setServiceAlarm(getActivity(),mClock,mClock.isCheck());
                getTargetFragment().onActivityResult(REQUEST_CODE, Activity.RESULT_OK,intent);
            }
        });
        builder.setNegativeButton("Cancel",null);
        return builder.create();
    }

}
