package ca.cmpt276.assignment2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CancelEditFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.cancel_edit_dialog_layout, null);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        ((NewGameActivity) getActivity()).finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }

            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle("Discard Changes")
                .setView(v)
                .setPositiveButton(android.R.string.yes, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create();
//        return super.onCreateDialog(savedInstanceState);
    }
}
