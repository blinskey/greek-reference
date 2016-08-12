package com.benlinskey.greekreference.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ScrollView;
import android.widget.TextView;

import com.benlinskey.greekreference.R;

public class DisplayHelpDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_help);

        TextView textView = new TextView(getActivity());
        textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setPadding(25, 25, 25, 25);
        textView.setText(Html.fromHtml(getString(R.string.message_help)));
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.addView(textView);
        builder.setView(scrollView);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }
}
