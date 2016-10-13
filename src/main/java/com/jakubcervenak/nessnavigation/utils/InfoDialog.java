package com.jakubcervenak.nessnavigation.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.jakubcervenak.nessnavigation.R;
import com.jakubcervenak.nessnavigation.core.Constants;


/**
 * Created by jakubcervenak on 21/09/16.
 */
public class InfoDialog extends DialogFragment {
    String mTitle, mMessage, mButtonText;

    public static InfoDialog newInstance(Bundle args) {
        InfoDialog dialog = new InfoDialog();
        dialog.setArguments(args);

        dialog.mTitle = args.getString(Constants.INFODIALOG_TITLE);
        dialog.mMessage = args.getString(Constants.INFODIALOG_MESSAGE);
        dialog.mButtonText = args.getString(Constants.INFODIALOG_BUTTON_TEXT);

        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (null != mTitle && !mTitle.equals("")) {
            builder.setTitle(mTitle);
        }
        builder.setMessage(mMessage);
        if (null != mButtonText && !mButtonText.equals("")) {
            builder.setPositiveButton(mButtonText, null);
        } else {
            builder.setPositiveButton(R.string.ok_btn, null);
        }
        return builder.create();
    }
}
