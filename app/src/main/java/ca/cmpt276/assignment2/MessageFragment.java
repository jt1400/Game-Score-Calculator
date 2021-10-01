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

public class MessageFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the view to.show
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.cancel_delete_layout, null);

        //Create a button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        ((NewGameActivity) getActivity()).deleteGame();
                        ((NewGameActivity) getActivity()).finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        //Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                    .setTitle("Delete Game")
                .setView(v)
                .setPositiveButton(android.R.string.yes, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create();
    }
}
