package de.reiss.nomb.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.reiss.nomb.R;


public class HomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        initButtonClickListeners(this);
    }

    private void initButtonClickListeners(Context c) {
        initButton(c, R.id.btn_home_java, JavacodeStartActivity.class);
        initButton(c, R.id.btn_home_native, NativecodeStartActivity.class);
    }

    private void initButton(final Context c, final int viewid,
                            final Class activityToStart) {
        Button b = (Button) findViewById(viewid);
        if (b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(c, activityToStart);
                    c.startActivity(intent);
                }
            });
        }
    }

}