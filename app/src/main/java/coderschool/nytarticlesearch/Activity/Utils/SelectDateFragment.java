package coderschool.nytarticlesearch.Activity.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {

    public SelectDateFragment(TextView textView) {
        mTextView = textView;
    }

    private String newDate;
    private TextView mTextView;
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, mYear, mMonth,
                mDay);//it will return dialog setting date with mYear,MMonth and MDay

    }

    @Override
    public void onDateSet(DatePicker view, int year,
                          int monthOfYear, int dayOfMonth) {
        System.out.println("year=" + year + "day=" + dayOfMonth + "month="
                + monthOfYear);
        String yearString = Integer.toString(year);
        String monthString = Integer.toString(monthOfYear + 1);
        String dayString = Integer.toString(dayOfMonth);
        newDate = dayString + "/" + monthString + "/" + yearString;
        mTextView.setText(newDate);
    }
}
