package coderschool.nytarticlesearch.Activity.Utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.List;

import coderschool.nytarticlesearch.Activity.activity.MainActivity;
import coderschool.nytarticlesearch.R;


public class ShowSearchDialogFragment extends DialogFragment {
    private View mView;
    private TextView tvDataPicker;
    private String currentDate;
    private EditDateDialogListener mEditDateDialogListener;
    private List<String> settingList;

    public String getCurrentDate() {
        return currentDate;
    }

    public interface EditDateDialogListener {
        void onFinishEditDialog (String pickDate,
                                 boolean isArt,
                                 boolean isFS,
                                 boolean isSport,
                                 String isNewest);
    }

    public void resultDialogListener (EditDateDialogListener listener){
        this.mEditDateDialogListener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.item_advance_search, container, false);
        final CheckBox cbArt;
        final CheckBox cbFS;
        final CheckBox cbSport;
        final ToggleButton tbSort;
        Button btnSetSearch;


        tvDataPicker = (TextView) mView.findViewById(R.id.tvDataPicker);
        cbArt = (CheckBox) mView.findViewById(R.id.cbArt);
        cbFS = (CheckBox) mView.findViewById(R.id.cbFS);
        cbSport = (CheckBox) mView.findViewById(R.id.cbSport);
        tbSort = (ToggleButton) mView.findViewById(R.id.tbSort);
        btnSetSearch = (Button) mView.findViewById(R.id.btnSetSearch);

        final Calendar c = Calendar.getInstance();
        String mYear = String.valueOf(c.get(Calendar.YEAR));
        String mMonth = String.valueOf(c.get(Calendar.MONTH)+1);
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));

        tvDataPicker.setText(mDay+"/"+mMonth+"/"+mYear);
        tvDataPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment;
                newFragment= new SelectDateFragment(tvDataPicker);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        btnSetSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditDateDialogListener.onFinishEditDialog(tvDataPicker.getText().toString(),
                        cbArt.isChecked(),
                        cbFS.isChecked(),
                        cbSport.isChecked(),
                        tbSort.isChecked()?"newest":"oldest");
                dismiss();
            }
        });


        return mView;
    }
}
