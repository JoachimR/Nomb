package de.reiss.nomb.controller.javacode;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import de.reiss.nomb.G;
import de.reiss.nomb.R;

import de.reiss.nomb.util.DoMethodInBackgroundAndDisplayResultTask;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;


public class FileHandlingActivity extends Activity {


    public final static int MAX_FILECONTENT_OUTPUTSIZE = 1024;


    private Activity mActivity;
    private EditText ed_file_internal;
    private EditText ed_filejava;
    private Spinner sp_filejava;

    private List<String> filePathArray = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.javacode_filehandling);
        mActivity = this;

        for (G.FilePath anzeige : G.FilePath.values()) {
            filePathArray.add(anzeige.toString());
        }
        setSpinnerJava();

        ed_file_internal = (EditText) findViewById(R.id.ed_file_internal);
        ed_file_internal.setText(G.TESTFILE);

        initButtonClickListeners(this);
    }


    private void setSpinnerJava() {

        ed_filejava = (EditText)
                findViewById(R.id.ed_filejava);

        sp_filejava = (Spinner) findViewById(R.id.sp_filejava);
        sp_filejava.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                if (TextUtils.isEmpty(selected)) {
                    return;
                }
                for (G.FilePath filePath : G.FilePath.values()) {
                    if (selected.equals(filePath.toString())) {
                        ed_filejava.setText(filePath.getValue());
                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ed_filejava.setText(G.FilePath.SDCARD_PATHANDFILE.getValue());
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, filePathArray);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_filejava.setAdapter(adapter);
    }

    private void initButtonClickListeners(Activity activity) {

        Button btn_filejava_create = (Button) findViewById(R.id.btn_filejava_create);
        btn_filejava_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return createFileUsingJava(getJavaFileAccessPath());
                    }
                };
                DoMethodInBackgroundAndDisplayResultTask task =
                        new DoMethodInBackgroundAndDisplayResultTask(mActivity);
                task.execute(callable);
            }
        });


        Button btn_filejava_read = (Button) findViewById(R.id.btn_filejava_read);
        btn_filejava_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return readFromFileUsingJava(getJavaFileAccessPath());
                    }
                };
                DoMethodInBackgroundAndDisplayResultTask task =
                        new DoMethodInBackgroundAndDisplayResultTask(mActivity);
                task.execute(callable);
            }
        });


        Button btn_filejava_write = (Button) findViewById(R.id.btn_filejava_write);
        btn_filejava_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return writeToFileUsingJava(getJavaFileAccessPath());
                    }
                };
                DoMethodInBackgroundAndDisplayResultTask task =
                        new DoMethodInBackgroundAndDisplayResultTask(mActivity);
                task.execute(callable);
            }
        });


        Button btn_create_internal = (Button) findViewById(R.id.btn_create_internal);
        btn_create_internal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return createFileUsingContext(getInternalFileName());
                    }
                };
                DoMethodInBackgroundAndDisplayResultTask task =
                        new DoMethodInBackgroundAndDisplayResultTask(mActivity);
                task.execute(callable);
            }
        });


        Button btn_read_internal = (Button) findViewById(R.id.btn_read_internal);
        btn_read_internal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return readFromFileUsingContext(getInternalFileName());
                    }
                };
                DoMethodInBackgroundAndDisplayResultTask task =
                        new DoMethodInBackgroundAndDisplayResultTask(mActivity);
                task.execute(callable);
            }
        });


        Button btn_write_internal = (Button) findViewById(R.id.btn_write_internal);
        btn_write_internal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callable callable = new Callable<String>() {
                    public String call() {
                        return writeFileUsingContext(getInternalFileName());
                    }
                };
                DoMethodInBackgroundAndDisplayResultTask task =
                        new DoMethodInBackgroundAndDisplayResultTask(mActivity);
                task.execute(callable);
            }
        });

    }


    private String getJavaFileAccessPath() {
        return ed_filejava.getText().toString();
    }

    private String getInternalFileName() {
        return ed_file_internal.getText().toString();
    }


    private String createFileUsingJava(String pathAndFileName) {
        StringBuffer sb = new StringBuffer();

        final String success = String.format("Creating file %s worked!", pathAndFileName);
        final String fail = String.format("Creating file %s did not work!", pathAndFileName);

        File file = new File(pathAndFileName);
        try {
            FileOutputStream f = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(G.TAG, "error: " + e.getMessage());
            sb.append(fail);
            sb.append("\n");
            sb.append(e.toString());
            return sb.toString();
        }
        sb.append(success);
        return sb.toString();
    }


    private String writeToFileUsingJava(String pathAndFileName) {
        StringBuffer sb = new StringBuffer();


        final String success = String.format("Writing file %s worked!", pathAndFileName);
        final String fail = String.format("Writing file %s did not work!", pathAndFileName);

        try {
//            File file = new File(pathAndFileName);
//            if (!file.exists()) {
//                sb.append(fail);
//                sb.append("\n");
//                sb.append("File did not exist!");
//                return sb.toString();
//            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String currentTime = formatter.format(new Date().getTime());
            String textToAppend = "\nWrote to file on " + currentTime;

            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(pathAndFileName, true));
            buf.append(textToAppend);
            buf.newLine();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(G.TAG, "error: " + e.getMessage());
            sb.append(fail);
            sb.append("\n");
            sb.append(e.toString());
            return sb.toString();
        }

        return success;
    }


    private String readFromFileUsingJava(String pathAndFileName) {
        StringBuffer sb = new StringBuffer();

        final String success = String.format("Reading file %s worked!", pathAndFileName);
        final String fail = String.format("Reading file %s did not work!", pathAndFileName);

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathAndFileName));
            String line;

            while ((line = br.readLine()) != null
                    && text.length() <= MAX_FILECONTENT_OUTPUTSIZE) {
                text.append(line);
                text.append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(G.TAG, "error: " + e.getMessage());
            sb.append(fail);
            sb.append("\n");
            sb.append(e.toString());
            return sb.toString();
        }

        sb.append(success);
        sb.append("\nContent of file:\n" + text);
        return sb.toString();

    }


    private String createFileUsingContext(String fileName) {
        StringBuffer sb = new StringBuffer();

        final String success = String.format("Creating file %s worked!", fileName);
        final String fail = String.format("Creating file %s did not work!", fileName);

        try {
            FileOutputStream fos = mActivity.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(G.TAG, "error: " + e.getMessage());
            sb.append(fail);
            sb.append("\n");
            sb.append(e.toString());
            return sb.toString();
        }

        sb.append(success);
        return sb.toString();
    }

    private String writeFileUsingContext(String fileName) {
        StringBuffer sb = new StringBuffer();

        final String success = String.format("Writing file %s worked!", fileName);
        final String fail = String.format("Writing file %s did not work!", fileName);

        String textToAppend = "";
        try {
            FileOutputStream fos = mActivity.openFileOutput(fileName, Context.MODE_APPEND);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String currentTime = formatter.format(new Date().getTime());
            textToAppend = "\nWrote to file on " + currentTime;

            fos.write(textToAppend.getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(G.TAG, "error: " + e.getMessage());
            sb.append(fail);
            sb.append("\n");
            sb.append(e.toString());
            return sb.toString();
        }
        sb.append(success);
        sb.append("\nWrote to file:\n");
        sb.append(textToAppend);
        return sb.toString();
    }


    private String readFromFileUsingContext(String fileName) {
        StringBuffer sb = new StringBuffer();

        final String success = String.format("Reading file %s worked!", fileName);
        final String fail = String.format("Reading file %s did not work!", fileName);

        StringBuffer buffer = new StringBuffer();

        try {
            FileInputStream fis = mActivity.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String read;
            if (fis != null) {
                while ((read = reader.readLine()) != null
                        && buffer.length() <= MAX_FILECONTENT_OUTPUTSIZE) {
                    buffer.append(read + "\n");
                }
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(G.TAG, "error: " + e.getMessage());
            sb.append(fail);
            sb.append("\n");
            sb.append(e.toString());
            return sb.toString();
        }

        sb.append(success);
        sb.append("\nContent of file:\n" + buffer.toString());
        return sb.toString();
    }


}