//package de.reiss.nomb.controller.javacode;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import de.reiss.nomb.G;
//import de.reiss.nomb.R;
//
//import de.reiss.nomb.util.Utils;
//
//import java.io.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//
//public class RuntimeActivity extends Activity {
//
//
//    Activity mActivity;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.javacode_runtime);
//        mActivity = this;
//        initButtonClickListeners();
//    }
//
//    private void initButtonClickListeners() {
//
//        Button btn_runtimeexec = (Button)
//                findViewById(R.id.btn_runtime_lsdata);
//        btn_runtimeexec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
////                    Runtime.getRuntime().exec(new String[]{"/system/bin/su", "-c", "reboot now"});
////                    rebootSU();
//
//                    String cd[] = {"cd", Environment.getExternalStorageDirectory() + ""};
//                    Shell shell = new Shell();
//                    shell.sendShellCommand(cd);
//
//                    String command[] = {"su", "-c", "ls /data/data"};
//                    shell = new Shell();
//                    String text = shell.sendShellCommand(command);
//                    Utils.showResultInDialog(mActivity, text);
//
//                    //TODO does not work. delete?
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Button btn_runtime_getlinuxusername = (Button)
//                findViewById(R.id.btn_runtime_getlinuxusername);
//        btn_runtime_getlinuxusername.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
////                    Runtime.getRuntime().exec(new String[]{"/system/bin/su", "-c", "reboot now"});
////                    rebootSU();
//
//                    String cd[] = {"cd", Environment.getExternalStorageDirectory() + ""};
//                    Shell shell = new Shell();
//                    shell.sendShellCommand(cd);
//
//                    String command[] = {"su", "-c",
//                            "ps -p | grep \"de.reiss.nomb\" | awk '{ print $1 }'"};
//                    shell = new Shell();
//                    String text = shell.sendShellCommand(command);
//                    Utils.showResultInDialog(mActivity, text);
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//    }
//
//    public Boolean execCommands(String... command) {
//        try {
//            Runtime rt = Runtime.getRuntime();
//            Process process = rt.exec("su");
//            DataOutputStream os = new DataOutputStream(process.getOutputStream());
//
//            for (int i = 0; i < command.length; i++) {
//                os.writeBytes(command[i] + "\n");
//                os.flush();
//            }
//            os.writeBytes("exit\n");
//            os.flush();
//            process.waitFor();
//        } catch (IOException e) {
//            return false;
//        } catch (InterruptedException e) {
//            return false;
//        }
//        return true;
//    }
//
//
//    /**
//     * https://code.google.com/p/xda-adk/source/browse/trunk/EclipseProjects/xdasdk/src/com/xdatv/xdasdk/Shell.java
//     */
//    public class Shell {
//
//        //for internal access
//        public Shell() {
//        }
//        //for external access
//
//        public String sendShellCommand(String[] cmd) {
//            System.out.println("\n###executing: " + cmd[0] + "###");
//            String AllText = "";
//            try {
//                String line;
//                Process process = new ProcessBuilder(cmd).start();
//                BufferedReader STDOUT = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                BufferedReader STDERR = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//                try {
//                    process.waitFor();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Shell.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                while ((line = STDERR.readLine()) != null) {
//                    AllText = AllText + "\n" + line;
//                }
//                while ((line = STDOUT.readLine()) != null) {
//                    AllText = AllText + "\n" + line;
//                    while ((line = STDERR.readLine()) != null) {
//                        AllText = AllText + "\n" + line;
//                    }
//                }
//                //log.level0(cmd[0]+"\":"+AllText);
//                return AllText;
//            } catch (IOException ex) {
//                System.out.println("Problem while executing in " +
//                        "Shell.sendShellCommand() Received " + AllText);
//                ex.printStackTrace();
//                return "CritERROR!!!";
//            }
//
//        }
//
//
//    }
//
//
//    public static void rebootSU() {
//        Log.e(G.TAG, "-------------------------------------------------START");
//
//
//        try {
//            Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "mount -o r,remount -t yaffs2 /dev/block/mtdblock3 /system"});
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        Runtime runtime = Runtime.getRuntime();
//        Process proc = null;
//        OutputStreamWriter osw = null;
//        StringBuilder sbstdOut = new StringBuilder();
//        StringBuilder sbstdErr = new StringBuilder();
//
//        String command = "/system/bin/reboot";
//
//        try { // Run Script
//
//            proc = runtime.exec("su");
//            osw = new OutputStreamWriter(proc.getOutputStream());
//            osw.write(command);
//            osw.flush();
//            osw.close();
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } finally {
//            if (osw != null) {
//                try {
//                    osw.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//
//        try {
//            if (proc != null)
//                proc.waitFor();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//        sbstdOut.append(inputStreamToString(proc
//                .getInputStream()));
//        sbstdErr.append(inputStreamToString(proc
//                .getInputStream()));
//
//        if (proc.exitValue() != 0) {
//
//        }
//
//        Log.e(G.TAG, "-------------------------------------------------END");
//    }
//
//    private static String inputStreamToString(InputStream is) {
//        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
//        return s.hasNext() ? s.next() : "";
//    }
//
//}