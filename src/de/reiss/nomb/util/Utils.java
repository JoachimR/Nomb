package de.reiss.nomb.util;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.reiss.nomb.R;

import java.util.regex.Pattern;

public class Utils {

    public static void showResultInDialog(final Context context, final String message) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.resultdialog);

        final String dlgTitle = context.getString(R.string.dlg_title);
        dialog.setTitle(dlgTitle);

        TextView text = (TextView) dialog.findViewById(R.id.dlg_tv_result);
        text.setText(message);

        Button dlg_btn_copy = (Button) dialog.findViewById(R.id.dlg_btn_copy);
        dlg_btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard =
                        (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(dlgTitle, message);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Result copied to clipboard",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button dlg_btn_ok = (Button) dialog.findViewById(R.id.dlg_btn_ok);
        dlg_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }


    public static boolean isTextEmailAddress(String text) {
        final Pattern rfc2822 = Pattern.compile(
                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
        );

        if (!rfc2822.matcher(text).matches()) {
            return false;
        }
        return true;
    }
}
