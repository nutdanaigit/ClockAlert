package ayp.aug.alarmclock;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Nutdanai on 8/24/2016.
 */
public class ClockListFragment extends Fragment {
    RecyclerView mRecyclerView;
    ClockAdapter mClockAdapter;
    private static final String TAG = "ClockListFragment";
    private static final String REQUEST_STRING_DIALOG = "requestTag_string_to_dialog";
    private static final int REQUEST_CODE_DIALOG = 77;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.clock_layout_recycler, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    class ClockHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        protected Clock mClock;
        private ClockLab mClockLab;
        private int mPosition;
        private TextView mTitleText;
        private TextView mTimeAlertText;
        private Switch aSwitch;
        private LinearLayout mLinearLayout;

        public ClockHolder(View itemView) {
            super(itemView);
            mClock = Clock.newInstanceClock();
            mTitleText = (TextView) itemView.findViewById(R.id.txt_title_name);
            mTimeAlertText = (TextView) itemView.findViewById(R.id.txt_show_timeAlert);
            aSwitch = (Switch) itemView.findViewById(R.id.switch_check);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.list_item_for_holder);
            mClockLab = ClockLab.getInstance(getActivity());


        }

        public void bind(Clock clock, int position) {
            mClock = clock;
            mPosition = position;

            mTitleText.setText(mClock.getTitle());
            mTimeAlertText.setText(mClockLab.setFormatTime(mClock.getTime()));
            Log.d(TAG, "Show new Format Time : " + mClockLab.setFormatTime(mClock.getTime()));
            aSwitch.setChecked(mClock.isCheck());

            mLinearLayout.setOnLongClickListener(this);
            mLinearLayout.setOnClickListener(this);
            aSwitch.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.switch_check:
                    mClock.setCheck(!mClock.isCheck());
                    ClockService.setServiceAlarm(getActivity(),mClock,mClock.isCheck());
                    Log.d(TAG, "Status Switch " + mPosition + " :" + aSwitch.isChecked());
                    return;
                case R.id.list_item_for_holder:
                    Log.d(TAG,"Come to edit list");
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    ClockDialogFragment clockDialogFragment = ClockDialogFragment.newInstance(mClock.getUuid());
                    clockDialogFragment.setTargetFragment(ClockListFragment.this, REQUEST_CODE_DIALOG);
                    clockDialogFragment.show(fm, REQUEST_STRING_DIALOG);
                    Log.d(TAG, "Position : " + mPosition);
                    return;
                default:
                    Log.d(TAG, " default");
                    return;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            Log.d(TAG,"! On long Click !");
            new AlertDialog.Builder(getActivity())
                    .setTitle("Do you want to delete list?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           ClockLab.getInstance(getActivity()).delete(mPosition);
                            Log.d(TAG,"Size : "+ClockLab.getInstance(getActivity()).getClock().size());
                            updateUI();
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show().create();
            return true;
        }
    }

    /**
     * ClockAdapter Class
     */

    class ClockAdapter extends RecyclerView.Adapter<ClockHolder> {
        private List<Clock> _clock;

        public ClockAdapter(List<Clock> clocks) {
            _clock = clocks;
        }

        @Override
        public ClockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.clock_item_list, parent, false);
            return new ClockHolder(v);
        }

        @Override
        public void onBindViewHolder(ClockHolder holder, int position) {
            Clock clock = _clock.get(position);
            holder.bind(clock, position);
        }

        @Override
        public int getItemCount() {
            return _clock.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_plus_time:
                FragmentManager fm = getActivity().getSupportFragmentManager();

                ClockDialogFragment clockDialogFragment = ClockDialogFragment.newInstance(Clock.newInstanceClock().getUuid());
                clockDialogFragment.setTargetFragment(ClockListFragment.this, REQUEST_CODE_DIALOG);
                clockDialogFragment.show(fm, REQUEST_STRING_DIALOG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_item, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DIALOG) {
            if (resultCode == Activity.RESULT_OK) {
                updateUI();
            }
        }
    }

    public void updateUI() {
        ClockLab clockLab = ClockLab.getInstance(getActivity());
        List<Clock> mClockLists = clockLab.getClock();
        if (mClockAdapter == null) {
            mClockAdapter = new ClockAdapter(mClockLists);
            mRecyclerView.setAdapter(mClockAdapter);
            Toast.makeText(getActivity(),"คุณตั้งเวลาไว้ : " + Clock.newInstanceClock().getTime(),Toast.LENGTH_LONG).show();
        } else {
            mClockAdapter.notifyDataSetChanged();
        }

    }


}
