package de.reiss.nomb.controller.nativecode;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import de.reiss.nomb.G;
import de.reiss.nomb.R;

import de.reiss.nomb.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class FileHandlingActivity extends Activity {


    private Activity mActivity;
    private EditText ed_fopen;
    private Spinner sp_fopen;

    private List<String> filePathArray = new ArrayList<String>();


    public native String performfopenwplus(String path);

    public native String performfopenr(String path);


    static {
        final String library = "de_reiss_nomb_controller_nativecode_FileHandlingActivity";
        try {
            System.loadLibrary(library);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nativecode_filehandling);
        mActivity = this;

        for (G.FilePath anzeige : G.FilePath.values()) {
            filePathArray.add(anzeige.toString());
        }
        setSpinnerFopen();

        initButtonClickListeners();
    }

    private void setSpinnerFopen() {

        ed_fopen = (EditText) findViewById(R.id.ed_fopen);

        sp_fopen = (Spinner) findViewById(R.id.sp_fopen);
        sp_fopen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                if (TextUtils.isEmpty(selected)) {
                    return;
                }
                for (G.FilePath filePath : G.FilePath.values()) {
                    if (selected.equals(filePath.toString())) {
                        ed_fopen.setText(filePath.getValue());
                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ed_fopen.setText(G.FilePath.SDCARD_PATHANDFILE.getValue());
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, filePathArray);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_fopen.setAdapter(adapter);
    }



    private void initButtonClickListeners() {

        Button btn_fopen_wplus = (Button) findViewById(R.id.btn_fopen_wplus);
        btn_fopen_wplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showResultInDialog(mActivity, fopenWplus());
            }
        });


        Button btn_fopen_r = (Button) findViewById(R.id.btn_fopen_r);
        btn_fopen_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showResultInDialog(mActivity, fopenR());
            }
        });



    }

    private String fopenWplus() {
        String path = ed_fopen.getText().toString();
        String result = "";
        try {
            result += performfopenwplus(path);
        } catch (Exception e) {
            e.printStackTrace();
            result += e.getLocalizedMessage();
        } catch (Throwable t) {
            t.printStackTrace();
            result += t.getLocalizedMessage();
        }
        return result;
    }


    private String fopenR() {
        String path = ed_fopen.getText().toString();
        String result = "";
        try {
            result += performfopenr(path);
        } catch (Exception e) {
            e.printStackTrace();
            result += e.getLocalizedMessage();
        } catch (Throwable t) {
            t.printStackTrace();
            result += t.getLocalizedMessage();
        }
        return result;
    }


}