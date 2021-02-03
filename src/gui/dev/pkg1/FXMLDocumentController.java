/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.dev.pkg1;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.awt.Checkbox;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import sun.rmi.runtime.Log;
import java.util.Locale;

/**
 *
 * @author WildFire
 */
public class FXMLDocumentController implements Initializable
{

    private DirectoryChooser mDirectoryChooser = new DirectoryChooser();
    private File mCNNSourceDirectory = null, mCNNDestDirectory = null, mTestingDirectory = null, mModelFile = null, mLabelsFile = null, mCNNHighSourceDirectory = null, mCNNHighDestDirectory = null, mTestingSingleDirectory = null, mModelSingleFile = null, mLabelsSingleFile = null;
    private ArrayList<File> mAvailableCNNDirectory, mSelectedCNNDirectory;
    private JFrame frame;
    private StringBuffer mCNNHighStringBuffer;
    private ExecutorService service;
    private boolean mServiceRunning = false, mNormalRandomManipulations = false, mProMode = false, mHighRandomManipulations = false, mIsMobile = false, mIsMobileHigh = false, mIsMobileTesting = false;
    private ArrayList<TestingData> mTestingDataArrayList = null;
    private FileChooser mFileChooser = new FileChooser();
    private float total, positive;
    private String mModuleNames = null;


    //Panes Combined
    @FXML
    private Pane pane_CNN, pane_2, pane_3, pane_home, pane_4, pro_mode_pane, modes_pane, pro_mode_test_pane;

    //Buttons Combined
    @FXML
    private JFXButton btn_CNN, btn_2, btn_3, btn_home, btn_4, mobile_mode_btn, mobile_high_btn, mobile_single_btn, mobile_batch_btn;

    //TextFields from V1
    @FXML
    private JFXTextField cnn_source, cnn_dest, test_source, test_acc, test_model, test_label, cnn_high_source, cnn_high_dest, test_single_source, test_single_acc, test_single_model, test_single_label, testing_single_result;

    //TextFields for V2 Training Pro Mode
    @FXML
    private JFXTextField num_steps, model_name, label_name, logs_name, learning_rate, testing_percentage, validation_percentage, ev_step_interval, train_batch_size, validation_batch_size, final_layer;

    //TextFields for V2 Testing Pro Mode
    @FXML
    private JFXTextField test_input_height, test_input_width, test_input_mean, test_input_std, test_input_layer, test_output_layer;

    //Checkboxes from V1
    @FXML
    private JFXCheckBox cnn_verbose, cnn_high_verbose, testing_single_verbose, testing_verbose;

    //TextAreas from V1
    @FXML
    private JFXTextArea cnn_text_area, testing_text_area, cnn_high_text_area, testing_single_text_area;

    //ProgressBar from V1
    @FXML
    private ProgressBar cnn_progress, testing_progress_bar, cnn_high_progress, testing_single_progress_bar;

    //ToggleButtons for V2
    @FXML
    private JFXToggleButton normal_pro_mode, normal_random_manipulations, high_random_manipulations, high_pro_mode, batch_testing_pro_mode, single_testing_pro_mode;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        GUIDev1.stage.setResizable(false);
        GUIDev1.stage.initStyle(StageStyle.TRANSPARENT);
        setListeners();
        mAvailableCNNDirectory = new ArrayList<>();
        mSelectedCNNDirectory = new ArrayList<>();
        cnn_text_area.setMouseTransparent(true);
        service = Executors.newFixedThreadPool(1);
        checkPythonVersion();
        checkPythonModules();
    }

    private boolean isInternetConnected()
    {
        try
        {
            URL url = new URL("https://www.google.com/");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e)
        {
            return false;
        }
    }

    @FXML
    private void Min_circle()
    {
        GUIDev1.stage.setIconified(true);
    }

    @FXML
    private void Exit_Circle()
    {
        System.exit(0);
    }

    @FXML
    private void btnCNNClicked()
    {
        pane_CNN.toFront();
        btn_CNN.setStyle(" -fx-background-color:#5977fb;-fx-text-fill: #fff");
        btn_2.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_3.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_home.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_4.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_mode_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_high_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_single_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_batch_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mIsMobile = false;
    }

    @FXML
    private void btn2Clicked()
    {
        pane_2.toFront();
        btn_2.setStyle(" -fx-background-color:#5977fb;-fx-text-fill: #fff");
        btn_CNN.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_3.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_home.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_4.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_mode_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_high_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_single_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_batch_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mIsMobile = false;
    }

    @FXML
    private void btn3Clicked()
    {
        pane_3.toFront();
        btn_3.setStyle(" -fx-background-color:#5977fb;-fx-text-fill: #fff");
        btn_2.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_CNN.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_home.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_4.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_mode_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_high_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_single_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_batch_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mIsMobile = false;
    }

    @FXML
    private void btn4Clicked()
    {
        pane_4.toFront();
        btn_4.setStyle(" -fx-background-color:#5977fb;-fx-text-fill: #fff");
        btn_2.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_CNN.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_home.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_3.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_mode_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_high_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_single_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_batch_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mIsMobile = false;
    }

    @FXML
    private void mobileModeBtnClicked()
    {
        pane_CNN.toFront();
        mobile_mode_btn.setStyle(" -fx-background-color:#5977fb;-fx-text-fill: #fff");
        btn_2.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_3.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_home.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_4.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_CNN.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_high_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_single_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_batch_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mIsMobile = true;
    }

    @FXML
    private void mobileHighBtnClicked()
    {
        pane_2.toFront();
        mobile_high_btn.setStyle(" -fx-background-color:#5977fb;-fx-text-fill: #fff");
        btn_CNN.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_3.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_home.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_4.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_mode_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_2.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_single_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_batch_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mIsMobile = true;
    }

    @FXML
    private void mobileSingleBtnClicked()
    {
        pane_4.toFront();
        mobile_single_btn.setStyle(" -fx-background-color:#5977fb;-fx-text-fill: #fff");
        btn_2.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_CNN.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_home.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_4.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_mode_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_high_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_3.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_batch_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mIsMobile = true;
    }

    @FXML
    private void mobileBatchBtnClicked()
    {
        pane_3.toFront();
        mobile_batch_btn.setStyle(" -fx-background-color:#5977fb;-fx-text-fill: #fff");
        btn_2.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_CNN.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_home.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_3.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_mode_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_high_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_single_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_4.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mIsMobile = true;
    }

    @FXML
    private void btnHomeClicked()
    {
        pane_home.toFront();
        btn_home.setStyle(" -fx-background-color:#5977fb;-fx-text-fill: #fff");
        btn_2.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_3.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_CNN.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        btn_4.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_mode_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_high_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_single_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mobile_batch_btn.setStyle(" -fx-background-color:#f4f4f4;-fx-text-fill: #000");
        mIsMobile = false;
    }

    @FXML
    private void browse()
    {
        mCNNSourceDirectory = mDirectoryChooser.showDialog(null);
        if (mCNNSourceDirectory != null)
        {
            cnn_source.setText(mCNNSourceDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void cnnDestinationBrowse()
    {
        mCNNDestDirectory = mDirectoryChooser.showDialog(null);
        if (mCNNDestDirectory != null)
        {
            cnn_dest.setText(mCNNDestDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void cnnTrain()
    {
        if (!mServiceRunning)
        {
            if (!cnn_source.getText().equals("") && !cnn_dest.getText().equals(""))
            {
                mServiceRunning = true;
                cnn_progress.visibleProperty().setValue(true);
                if (mNormalRandomManipulations)
                {
                    ProcessRandomManipulations process_random_manipulations;
                    if (mIsMobile)
                    {
                        if (isInternetConnected())
                        {
                            process_random_manipulations = new ProcessRandomManipulations(Constants.MOBILE_MODE);
                        } else
                        {
                            showInternetError();
                            return;
                        }
                    } else
                    {
                        process_random_manipulations = new ProcessRandomManipulations(Constants.NORMAL_MODE);
                    }
                    service.submit(process_random_manipulations);
                } else
                {
                    if (mIsMobile)
                    {
                        if (isInternetConnected())
                        {
                            preTrain(Constants.MOBILE_MODE);
                        } else
                        {
                            showInternetError();
                            return;
                        }
                    } else
                    {
                        preTrain(Constants.NORMAL_MODE);
                    }
                }
            } else
            {
                showDirectoryError();
            }
        } else
        {
            showServiceError();
        }
    }

    public class ProcessRandomManipulations implements Callable<Integer>
    {

        private int mMode;

        public ProcessRandomManipulations(int mode)
        {
            mMode = mode;
        }

        public Integer call() throws Exception
        {
            StringBuffer string_buffer = new StringBuffer("");
            String line = null;
            ProcessBuilder builder;
            File[] subDirs = null;
            if (mMode == Constants.NORMAL_MODE || mMode == Constants.MOBILE_MODE)
            {
                subDirs = new File(cnn_source.getText()).listFiles(new FileFilter()
                {
                    public boolean accept(File pathname)
                    {
                        return pathname.isDirectory();
                    }
                });
            } else
            {
                subDirs = new File(cnn_high_source.getText()).listFiles(new FileFilter()
                {
                    public boolean accept(File pathname)
                    {
                        return pathname.isDirectory();
                    }
                });
            }
            ArrayList<File> directories_arraylist = new ArrayList<>();
            for (File subDir : subDirs)
            {
                //System.out.println(subDir);
                directories_arraylist.add(subDir);
            }
            try
            {
                for (int i = 0; i < directories_arraylist.size(); i++)
                {
                    String command = "python randomize_data.py \"" + directories_arraylist.get(i).toString() + "\"";
                    System.out.println(command);
                    builder = new ProcessBuilder("cmd.exe", "/c", command);
                    builder.redirectErrorStream(true);
                    Process process = builder.start();
                    BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while ((line = r.readLine()) != null)
                    {
                        System.out.println(line);
                        string_buffer.append(line + "\n");
                    }
                    String newString = String.valueOf(string_buffer);
                    if (mMode == Constants.NORMAL_MODE || mMode == Constants.MOBILE_MODE)
                    {
                        cnn_text_area.appendText(newString);
                    } else
                    {
                        cnn_high_text_area.appendText(newString);
                    }
                    process.waitFor();
                }
            } catch (IOException ex)
            {
                System.out.println(ex.toString());
            } catch (InterruptedException ex)
            {
                System.out.println(ex.toString());
            }
            preTrain(mMode);
            return null;
            //trainModel(false);
        }
    }

    private void preTrain(int mode)
    {
        if (mProMode)
        {
            if (checkProMode())
            {
                if (mode == Constants.NORMAL_MODE || mode == Constants.MOBILE_MODE)
                {
                    cnn_progress.visibleProperty().setValue(true);
                } else
                {
                    cnn_high_progress.visibleProperty().setValue(true);;
                }
                trainModel(mode);
            } else
            {
                showProModeError();
            }
        } else
        {
            cnn_progress.visibleProperty().setValue(true);
            trainModel(mode);
        }
    }

    private void trainModel(int mode)
    {
        ProcessRetrain process_retrain = null;
        if (mode == Constants.NORMAL_MODE || mode == Constants.MOBILE_MODE)
        {
            process_retrain = new ProcessRetrain(cnn_source.getText(), cnn_dest.getText(), mode);
        } else
        {
            process_retrain = new ProcessRetrain(cnn_high_source.getText(), cnn_high_dest.getText(), mode);
        }
        service.submit(process_retrain);
    }

    private void checkPythonVersion()
    {
        String line = null;
        ProcessBuilder builder;
        String new_string = "";
        StringBuffer string_buffer_local = new StringBuffer();
        try
        {
            builder = new ProcessBuilder("cmd.exe", "/c", "python --version");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = r.readLine()) != null)
            {
                System.out.println(line);
                string_buffer_local.append(line + "\n");
            }
            new_string = String.valueOf(string_buffer_local);
            process.waitFor();
        } catch (IOException | InterruptedException ex)
        {
            System.out.println(ex.toString());
        }
        char[] char_array = new_string.toCharArray();
        for (int i = 0; i < new_string.length(); i++)
        {
            if (Character.isDigit(char_array[i]))
            {
                if (Integer.parseInt(String.valueOf(char_array[i])) < 3)
                {
                    showVersionError();
                }
                break;
            }
        }
    }

    private void checkPythonModules()
    {
        /*String line = null;
        ProcessBuilder builder;
        String new_string = "";
        StringBuffer string_buffer_local = new StringBuffer();
        try
        {
            builder = new ProcessBuilder("cmd.exe", "/c", "python module_check.py");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = r.readLine()) != null)
            {
                System.out.println(line);
                string_buffer_local.append(line + "\n");
            }
            new_string = String.valueOf(string_buffer_local);
            process.waitFor();
        } catch (IOException | InterruptedException ex)
        {
            System.out.println(ex.toString());
        }
        if (!new_string.equals("0"))
        {
            showModulesError();
        }
        System.out.println(new_string);*/
        //ModuleCheck module_check = new ModuleCheck();
        mServiceRunning = true;
        ModuleCheck moduleCheck = new ModuleCheck();
        Future<String> future = service.submit(moduleCheck);
        try
        {
            if (!future.get().contains("0"))
            {
                mModuleNames = future.get();
                showModulesError();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class ModuleCheck implements Callable<String>
    {

        @Override
        public String call() throws Exception
        {
            String line = null;
            ProcessBuilder builder;
            String new_string = "";
            StringBuffer string_buffer_local = new StringBuffer();
            try
            {
                builder = new ProcessBuilder("cmd.exe", "/c", "python module_check.py");
                builder.redirectErrorStream(true);
                Process process = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = r.readLine()) != null)
                {
                    System.out.println(line);
                    string_buffer_local.append(line + "\n");
                }
                new_string = String.valueOf(string_buffer_local);
                process.waitFor();
            } catch (IOException | InterruptedException ex)
            {
                System.out.println(ex.toString());
            }
            mServiceRunning = false;
            return new_string;
        }

    }

    /*Callable<String> ModuleCheck = new Callable<String>()
    {
        @Override
        public String call()
        {
            String line = null;
            ProcessBuilder builder;
            String new_string = "";
            StringBuffer string_buffer_local = new StringBuffer();
            try
            {
                builder = new ProcessBuilder("cmd.exe", "/c", "python module_check.py");
                builder.redirectErrorStream(true);
                Process process = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = r.readLine()) != null)
                {
                    System.out.println(line);
                    string_buffer_local.append(line + "\n");
                }
                new_string = String.valueOf(string_buffer_local);
                process.waitFor();
            } catch (IOException | InterruptedException ex)
            {
                System.out.println(ex.toString());
            }
            mServiceRunning = false;
            return new_string;
        }
    };*/
    private void setListeners()
    {
        cnn_verbose.selectedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    cnn_text_area.visibleProperty().setValue(true);
                    /*cnn_text_area.setMouseTransparent(false);
                    cnn_text_area.setStyle("-text-area-background:#000;-fx-control-inner-background:#000000;");*/
                } else
                {
                    cnn_text_area.visibleProperty().setValue(false);
                    /*cnn_text_area.setMouseTransparent(true);
                    cnn_text_area.setStyle("-fx-background-color:#fff;-fx-text-fill:#fff");*/
                }
            }
        });
        cnn_high_verbose.selectedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    cnn_high_text_area.visibleProperty().setValue(true);
                    /*cnn_high_text_area.setMouseTransparent(false);
                    cnn_high_text_area.setStyle("-text-area-background:#000;-fx-control-inner-background:#000000;");*/
                } else
                {
                    cnn_high_text_area.visibleProperty().setValue(false);
                    /*cnn_high_text_area.setMouseTransparent(true);
                    cnn_high_text_area.setStyle("-fx-background-color:#fff;-fx-text-fill:#fff");*/
                }
            }
        });
        testing_single_verbose.selectedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    testing_single_text_area.visibleProperty().setValue(true);
                    /*testing_single_text_area.setMouseTransparent(false);
                    testing_single_text_area.setStyle("-text-area-background:#000;-fx-control-inner-background:#000000;");*/
                } else
                {
                    testing_single_text_area.visibleProperty().setValue(false);
                    /*testing_single_text_area.setMouseTransparent(true);
                    testing_single_text_area.setStyle("-fx-background-color:#fff;-fx-text-fill:#fff");*/
                }
            }
        });
        testing_verbose.selectedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    testing_text_area.visibleProperty().setValue(true);
                    /*testing_single_text_area.setMouseTransparent(false);
                    testing_single_text_area.setStyle("-text-area-background:#000;-fx-control-inner-background:#000000;");*/
                } else
                {
                    testing_text_area.visibleProperty().setValue(false);
                    /*testing_single_text_area.setMouseTransparent(true);
                    testing_single_text_area.setStyle("-fx-background-color:#fff;-fx-text-fill:#fff");*/
                }
            }
        });
        ChangeListener<Boolean> trainModeChangeListener = new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    pro_mode_pane.toFront();
                    mProMode = true;
                } else
                {
                    modes_pane.toFront();
                    mProMode = false;
                }
            }
        };
        normal_pro_mode.selectedProperty().addListener(trainModeChangeListener);
        high_pro_mode.selectedProperty().addListener(trainModeChangeListener);
        normal_random_manipulations.selectedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                mNormalRandomManipulations = newValue;
            }
        });
        high_random_manipulations.selectedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                mHighRandomManipulations = newValue;
            }
        });

        ChangeListener<Boolean> testingProModeChangeListener = new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    pro_mode_test_pane.toFront();
                    mProMode = true;
                } else
                {
                    modes_pane.toFront();
                    mProMode = false;
                }
            }

        };
        single_testing_pro_mode.selectedProperty().addListener(testingProModeChangeListener);
        batch_testing_pro_mode.selectedProperty().addListener(testingProModeChangeListener);
    }

    private boolean checkProMode()
    {
        if (num_steps.getText().toString().equals(""))
        {
            return false;
        }
        if (model_name.getText().toString().equals(""))
        {
            return false;
        }
        if (label_name.getText().toString().equals(""))
        {
            return false;
        }
        if (logs_name.getText().toString().equals(""))
        {
            return false;
        }
        if (learning_rate.getText().toString().equals(""))
        {
            return false;
        }
        if (testing_percentage.getText().toString().equals(""))
        {
            return false;
        }
        if (validation_percentage.getText().toString().equals(""))
        {
            return false;
        }
        if (ev_step_interval.getText().toString().equals(""))
        {
            return false;
        }
        if (train_batch_size.getText().toString().equals(""))
        {
            return false;
        }
        if (validation_batch_size.getText().toString().equals(""))
        {
            return false;
        }
        if (final_layer.getText().toString().equals(""))
        {
            return false;
        }
        return true;

    }

    public class DirectoryPanel extends JPanel
    {

        {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            if (mAvailableCNNDirectory != null)
            {
                for (int i = 0; i < mAvailableCNNDirectory.size(); i++)
                {
                    JCheckBox checkbox = new JCheckBox(mAvailableCNNDirectory.get(i).toString());
                    checkbox.addItemListener(new ItemListener()
                    {
                        @Override
                        public void itemStateChanged(ItemEvent e)
                        {
                            int state = e.getStateChange();
                            File file = new File(((JCheckBox) e.getItem()).getText());
                            if (state == ItemEvent.SELECTED)
                            {
                                mSelectedCNNDirectory.add(file);
                            } else
                            {
                                if (mSelectedCNNDirectory.contains(file))
                                {
                                    mSelectedCNNDirectory.remove(file);
                                }
                            }
                        }
                    });
                    this.add(checkbox);
                }
                JButton button = new JButton("Next");
                button.addActionListener((java.awt.event.ActionEvent e) ->
                {
                    frame.setVisible(false);
                    processHighAccuracy();
                });
                this.add(button);
            }
        }
    }

    public class DirectoryError extends JPanel
    {

        {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("Please insert valid Source and Destination!");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    public class ProModeError extends JPanel
    {

        {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("Please insert valid Pro Mode Values!");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    public class VersionError extends JPanel
    {

        {

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("Unified Machine Learning Tool requires Python Version 3.0 and above");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    public class ModulesError extends JPanel
    {

        {

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("<HTML>Unified Machine Learning Tool requires Python Modules<br>Please make sure you have these Modules installed:\n" + mModuleNames + "</HTML>");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    public class InternetError extends JPanel
    {

        {

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("<HTML>Unified Machine Learning Tool requires Internet Connection for this feature to work.</HTML>");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    public class NormalDirectorySteps extends JPanel
    {

        {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("<html>Steps:<br>1. Presort Objects to be recognized in folders named with their credentials.<br>2. Put all the objects directory in one folder.<br>3. Select this folder as dataset directory.<br>4. Select the directory where you want to save the model in model saving directory.<br>5. Click on train model.<br>&nbsp</html>");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    public class HIghFaceAccuracySteps extends JPanel
    {

        {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("<html>Steps:<br>1. Presort Objects to be recognized in folders named with their credentials.<br>2. Put all the objects directory in one folder.<br>3. Select this folder as dataset directory.<br>4. Select the directory where you want to save the model in model saving directory.<br>5. Click on train.<br>6. Select the datasets you want to apply high face accuracy upon.<br>7. Click Next. <br>&nbsp</html>");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    @FXML
    private void showHIghFaceAccuracySteps()
    {
        HIghFaceAccuracySteps hifas = new HIghFaceAccuracySteps();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(hifas);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;

        frame.setLocation(x, y);

    }

    public class BatchTestingSteps extends JPanel
    {

        {

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("<html>Steps:<br>1. Put the images to be tested in one directory.<br>2. Rename the test files to their credentials.<br>3. Select this directory as Testing Data Directory.<br>4. Set your minimum criteria(0.9 means 90%).<br>5. Select the trained model file as modile file.<br>6. Select the corresponding labels file as labels file.<br>7. Click on Test.<br>&nbsp</html>");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    @FXML
    private void showBatchTestingSteps()
    {
        BatchTestingSteps bts = new BatchTestingSteps();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(bts);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;

        frame.setLocation(x, y);

    }

    public class SingleTestSteps extends JPanel
    {

        {

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("<html>Steps:<br>1. Rename the test file to its credential.<br>2. Select this file as Test File.<br>3. Set your minimum criteria(0.9 means 90%).<br>4. Select the trained model file as modile file.<br>5. Select the corresponding labels file as labels file.<br>6. Click on Test.<br>&nbsp</html>");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    @FXML
    private void showSingleTestSteps()
    {
        SingleTestSteps sts = new SingleTestSteps();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(sts);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;

        frame.setLocation(x, y);

    }

    public class Error extends JPanel
    {

        {

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("<html><center><br>&nbsp<br>An Error occured please check Verbose Mode!<br>&nbsp</center></html>");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    @FXML
    private void showError()
    {
        Error e = new Error();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(e);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;

        frame.setLocation(x, y);

    }

    public class ServiceError extends JPanel
    {

        {

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JLabel label = new JLabel("A service is already running!");
            this.add(label);
            JButton button = new JButton("Okay");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
        }
    }

    private void processHighAccuracy()
    {
        processFaces.run();
    }

    Thread processFaces = new Thread()
    {
        @Override
        public void run()
        {
            mCNNHighStringBuffer = new StringBuffer("");
            String line = null;
            ProcessBuilder builder;
            for (int i = 0; i < mSelectedCNNDirectory.size(); i++)
            {
                try
                {
                    builder = new ProcessBuilder("cmd.exe", "/c", "python faceDetectionImageTest2.py \"" + mSelectedCNNDirectory.get(i).toString() + "\"");
                    builder.redirectErrorStream(true);
                    Process process = builder.start();
                    BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while ((line = r.readLine()) != null)
                    {
                        System.out.println(line);
                        mCNNHighStringBuffer.append(line + "\n");
                    }
                    String newString = String.valueOf(mCNNHighStringBuffer);
                    if (newString.toLowerCase().contains("error"))
                    {
                        showError();
                        mServiceRunning = false;
                        cnn_high_progress.setVisible(false);
                    }
                    cnn_high_text_area.appendText(newString);
                    mCNNHighStringBuffer = new StringBuffer("");
                    process.waitFor();
                } catch (IOException ex)
                {
                    System.out.println(ex.toString());
                } catch (InterruptedException ex)
                {
                    System.out.println(ex.toString());
                }
            }
            if (mHighRandomManipulations)
            {
                ProcessRandomManipulations process_random_manipulations;
                if (mIsMobileHigh)
                {
                    process_random_manipulations = new ProcessRandomManipulations(Constants.MOBILE_HIGH_FACE_ACCURACY_MODE);
                } else
                {
                    process_random_manipulations = new ProcessRandomManipulations(Constants.HIGH_FACE_ACCURACY_MODE);
                }
                service.submit(process_random_manipulations);
            } else
            {
                if (mIsMobileHigh)
                {
                    preTrain(Constants.MOBILE_HIGH_FACE_ACCURACY_MODE);
                } else
                {
                    preTrain(Constants.HIGH_FACE_ACCURACY_MODE);
                }
            }
        }
    };

    public class ProcessRetrain implements Callable<Integer>
    {

        private String source = null;
        private String dest = null;
        private StringBuffer string_buffer;
        private int mode;

        private ProcessRetrain(String source, String dest, int mode)
        {
            this.source = source;
            this.dest = dest;
            this.mode = mode;
        }

        @Override
        public Integer call()
        {
            String line = null;
            ProcessBuilder builder;
            String command = "python retrain.py --image_dir=\"" + source + "\" ";
            if (mProMode)
            {
                String num_steps_string = num_steps.getText().toString();
                String model_name_string = model_name.getText().toString();
                String label_name_string = label_name.getText().toString();
                String logs_name_string = logs_name.getText().toString();
                String learning_rate_string = learning_rate.getText().toString();
                String testing_percentage_string = testing_percentage.getText().toString();
                String validation_perc_string = validation_percentage.getText().toString();
                String ev_step_inter_string = ev_step_interval.getText().toString();
                String train_batch_size_string = train_batch_size.getText().toString();
                String valid_batch_size_string = validation_batch_size.getText().toString();
                String final_string_string = final_layer.getText().toString();
                command = command + "--output_graph=\"" + dest + model_name_string + "\" --output_labels=\"" + dest + label_name_string + "\" --summaries_dir=\"" + logs_name_string + "\" --how_many_training_steps=\"" + num_steps_string + "\" --learning_rate=\"" + learning_rate_string + "\" --testing_percentage=\"" + testing_percentage_string + "\" --validation_percentage=\"" + validation_perc_string + "\" --eval_step_interval=\"" + ev_step_inter_string + "\" --train_batch_size=\"" + train_batch_size_string + "\" --validation_batch_size=\"" + valid_batch_size_string + "\" --final_tensor_name=\"" + final_string_string + "\"";
            } else
            {
                command = command + "--output_graph=\"" + dest + "model.pb\" --output_labels=\"" + dest + "label.txt\"";
            }
            if (mode == Constants.MOBILE_MODE || mode == Constants.MOBILE_HIGH_FACE_ACCURACY_MODE)
            {
                command = command + " --tfhub_module=https://tfhub.dev/google/imagenet/mobilenet_v2_100_224/feature_vector/2";
            }
            System.out.println(command);
            string_buffer = new StringBuffer();
            try
            {
                builder = new ProcessBuilder("cmd.exe", "/c", command);
                builder.redirectErrorStream(true);
                Process process = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = r.readLine()) != null)
                {
                    System.out.println(line);
                    string_buffer.append(line + "\n");
                }
                String newString = String.valueOf(string_buffer);
                if (newString.toLowerCase().contains("error"))
                {
                    showError();
                    mServiceRunning = false;
                    cnn_high_progress.setVisible(false);
                    cnn_progress.setVisible(false);
                    return null;
                }
                if (mode == Constants.NORMAL_MODE || mode == Constants.MOBILE_MODE)
                {
                    cnn_text_area.appendText(newString);
                } else
                {
                    cnn_high_text_area.appendText(newString);;
                }
                string_buffer = new StringBuffer("");
                process.waitFor();
            } catch (IOException ex)
            {
                System.out.println(ex.toString());
            } catch (InterruptedException ex)
            {
                System.out.println(ex.toString());
            }
            cnn_progress.visibleProperty().setValue(false);
            cnn_high_progress.visibleProperty().setValue(false);
            mServiceRunning = false;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Process Complete!\nWould you like to open the destination Directory?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION)
            {
                try
                {
                    Desktop.getDesktop().open(new File(cnn_dest.getText()));
                } catch (IOException ex)
                {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return null;
        }
    }

    @FXML
    private void testBrowse()
    {
        mTestingDirectory = mDirectoryChooser.showDialog(null);
        if (mTestingDirectory != null)
        {
            test_source.setText(mTestingDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void startTesting()
    {
        mIsMobileTesting = mIsMobile;
        if (!test_source.getText().equals("") && !test_acc.getText().equals("") && !test_model.getText().equals("") && !test_label.getText().equals(""))
        {
            File folder = new File(test_source.getText());
            File[] list_of_files = folder.listFiles();
            TestData test_Data = new TestData(list_of_files);
            if (!mServiceRunning)
            {
                if (mProMode)
                {
                    if (checkTestingProMode())
                    {
                        testing_progress_bar.visibleProperty().setValue(true);
                        mServiceRunning = true;
                        service.submit(test_Data);
                    } else
                    {
                        showProModeError();
                        return;
                    }
                } else
                {
                    testing_progress_bar.visibleProperty().setValue(true);
                    mServiceRunning = true;
                    service.submit(test_Data);
                }
            } else
            {
                showServiceError();
            }
        } else
        {
            showDirectoryError();
        }
    }

    private void showDirectoryError()
    {
        DirectoryError dp = new DirectoryError();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(dp);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;
        frame.setLocation(x, y);
        mServiceRunning = false;
        cnn_high_progress.visibleProperty().setValue(false);
        cnn_progress.visibleProperty().setValue(false);
    }

    private void showProModeError()
    {
        ProModeError dp = new ProModeError();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(dp);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;
        frame.setLocation(x, y);
        mServiceRunning = false;
        cnn_high_progress.visibleProperty().setValue(false);
        cnn_progress.visibleProperty().setValue(false);
    }

    private void showVersionError()
    {
        VersionError dp = new VersionError();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(dp);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;
        frame.setLocation(x, y);
        mServiceRunning = false;
        cnn_high_progress.visibleProperty().setValue(false);
        cnn_progress.visibleProperty().setValue(false);
    }

    private void showModulesError()
    {
        ModulesError dp = new ModulesError();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(dp);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;
        frame.setLocation(x, y);
        mServiceRunning = false;
        cnn_high_progress.visibleProperty().setValue(false);
        cnn_progress.visibleProperty().setValue(false);
    }

    private void showInternetError()
    {
        InternetError dp = new InternetError();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(dp);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;
        frame.setLocation(x, y);
        mServiceRunning = false;
        cnn_high_progress.visibleProperty().setValue(false);
        cnn_progress.visibleProperty().setValue(false);
    }

    @FXML
    private void showNormalDirectorySteps()
    {
        NormalDirectorySteps nds = new NormalDirectorySteps();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(nds);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;
        frame.setLocation(x, y);
    }

    private void showServiceError()
    {
        ServiceError dp = new ServiceError();
        frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.add(dp);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;

        frame.setLocation(x, y);

    }

    private class TestingData
    {

        private String filename = "";
        private float accuracy = 0;
        private String recognized_label = "";
        private String result = "";

        public TestingData(String filename, String recognized, float accuracy, String result)
        {
            this.filename = filename;
            this.recognized_label = recognized;
            this.accuracy = accuracy;
            this.result = result;
        }

        public String getResult()
        {
            return result;
        }

        public void setResult(String result)
        {
            this.result = result;
        }

        public String getRecognized_label()
        {
            return recognized_label;
        }

        public void setRecognized_label(String recognized_label)
        {
            this.recognized_label = recognized_label;
        }

        public String getFilename()
        {
            return filename;
        }

        public void setFilename(String filename)
        {
            this.filename = filename;
        }

        public float getAccuracy()
        {
            return accuracy;
        }

        public void setAccuracy(float accuracy)
        {
            this.accuracy = accuracy;
        }

    }

    public class TestData implements Callable<Integer>
    {

        File[] mListOfFiles;
        private StringBuffer str;

        public TestData(File[] list_of_data)
        {
            System.out.println("Constructor Call \n List of files : " + list_of_data.length);
            this.mListOfFiles = list_of_data;
            str = new StringBuffer("");
        }

        @Override
        public Integer call() throws Exception
        {
            int p = 0;
            System.out.println("Call");
            mTestingDataArrayList = new ArrayList<>();
            String line = null;
            ProcessBuilder builder;
            String acc_name;
            String line_1 = null;
            int flag = 0;
            String file_name = null;
            int j = 0;
            Float criteria_acc = Float.valueOf(test_acc.getText());
            System.out.println("" + criteria_acc);
            Float acc;
            String result;
            for (int i = 0; i < mListOfFiles.length; i++)
            {
                System.out.println("" + i);
                j = 0;
                try
                {
                    String command = "python label_image.py --graph=\"" + test_model.getText() + "\" --labels=\"" + test_label.getText() + "\" --image=\"" + mListOfFiles[i].getAbsolutePath() + "\" ";
                    if (mProMode)
                    {
                        String input_height = test_input_height.getText().toString();
                        String input_width = test_input_width.getText().toString();
                        String input_mean = test_input_mean.getText().toString();
                        String input_std = test_input_std.getText().toString();
                        String input_layer = test_input_layer.getText().toString();
                        String output_layer = test_output_layer.getText().toString();
                        command = command + "--input_height=\"" + input_height + "\" --input_width=\"" + input_width + "\" --input_mean=\"" + input_mean + "\" --input_std=\"" + input_std + "\" --input_layer=\"" + input_layer + "\" --output_layer=\"" + output_layer + "\"";
                    } else if (mIsMobileTesting)
                    {
                        command = command + "--input_layer=Placeholder --output_layer=final_result --input_height=224 --input_width=224";
                    } else
                    {
                        command = command + "--input_layer=Placeholder --output_layer=final_result";
                    }
                    System.out.println(command);
                    builder = new ProcessBuilder("cmd.exe", "/c", command);
                    builder.redirectErrorStream(true);
                    Process process = builder.start();
                    BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while ((line = r.readLine()) != null)
                    {
                        System.out.println(line);
                        line_1 = line;
                        str.append(line + "\n");
                    }
                    String newString = String.valueOf(str);
                    if (newString.toLowerCase().contains("error"))
                    {
                        showError();
                        mServiceRunning = false;
                        testing_progress_bar.setVisible(false);
                    }
                    testing_text_area.appendText(newString);
                    System.out.println("line_1 : " + line_1);
                    for (int k = line_1.length() - 1; k >= 0; k--)
                    {
                        if (line_1.charAt(k) == ' ')
                        {
                            flag = k + 1;
                            break;
                        }
                    }
                    int pos = mListOfFiles[i].getName().lastIndexOf(".");
                    if (pos > 0)
                    {
                        file_name = mListOfFiles[i].getName().substring(0, pos);
                    }
                    acc = Float.valueOf(line_1.substring(flag, line_1.length() - 1));
                    acc_name = line_1.substring(0, flag - 1);
                    if (acc > criteria_acc && file_name.toLowerCase().contains(acc_name.toLowerCase()))
                    {
                        result = "Pass";
                        p++;
                    } else
                    {
                        result = "Fail";
                    }
                    mTestingDataArrayList.add(new TestingData(file_name, acc_name, acc, result));
                    System.out.println("Completed arraylist stuff!");
                    //mTestingVerboseStringBuilder = new StringBuilder("");
                    System.out.println("Wait for!");
                    process.waitFor();
                    System.out.println("wait for complete!");
                } catch (IOException ex)
                {
                    System.out.println(ex.toString());
                } catch (InterruptedException ex)
                {
                    System.out.println(ex.toString());
                }
                str = new StringBuffer("");
            }
            System.out.println("Exit from Main for loop");
            for (int i = 0; i < mTestingDataArrayList.size(); i++)
            {
                System.out.println("Name : " + mTestingDataArrayList.get(i).getFilename() + " recognized as : " + mTestingDataArrayList.get(i).getRecognized_label() + " accuracy : " + mTestingDataArrayList.get(i).getAccuracy() + " result : " + mTestingDataArrayList.get(i).getResult());
            }
            testing_progress_bar.visibleProperty().setValue(false);
            FXMLDocumentController.this.total = mListOfFiles.length;
            FXMLDocumentController.this.positive = p;
            showTable(mTestingDataArrayList);
            mServiceRunning = false;
            return null;
        }

    }

    @FXML
    private void testModelBrowse()
    {
        mModelFile = mFileChooser.showOpenDialog(null);
        if (mModelFile != null)
        {
            test_model.setText(mModelFile.getAbsolutePath());
        }
    }

    @FXML
    private void testLabelBrowse()
    {
        mLabelsFile = mFileChooser.showOpenDialog(null);
        if (mLabelsFile != null)
        {
            test_label.setText(mLabelsFile.getAbsolutePath());
        }
    }

    @FXML
    private void cnnHighSourceBrowse()
    {
        mCNNHighSourceDirectory = mDirectoryChooser.showDialog(null);
        if (mCNNHighSourceDirectory != null)
        {
            cnn_high_source.setText(mCNNHighSourceDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void cnnHighDestBrowse()
    {
        mCNNHighDestDirectory = mDirectoryChooser.showDialog(null);
        if (mCNNHighDestDirectory != null)
        {
            cnn_high_dest.setText(mCNNHighDestDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void cnnHighTrain()
    {
        mIsMobileHigh = mIsMobile;
        if (!mServiceRunning)
        {
            if (!cnn_high_source.getText().equals("") && !cnn_high_dest.getText().equals(""))
            {
                mServiceRunning = true;
                cnn_high_progress.visibleProperty().setValue(true);
                mAvailableCNNDirectory.clear();
                mSelectedCNNDirectory.clear();
                File[] subDirs = new File(cnn_high_source.getText()).listFiles(new FileFilter()
                {
                    public boolean accept(File pathname)
                    {
                        return pathname.isDirectory();
                    }
                });
                for (File subDir : subDirs)
                {
                    mAvailableCNNDirectory.add(subDir);
                }
                cnn_high_progress.visibleProperty().setValue(true);
                if (mIsMobileHigh)
                {
                    if (!isInternetConnected())
                    {
                        showInternetError();
                        return;
                    }
                }
                showDirectoryPanel();
            } else
            {
                showDirectoryError();
            }
        } else
        {
            showServiceError();
        }
    }

    private void showDirectoryPanel()
    {
        DirectoryPanel dp = new DirectoryPanel();
        frame = new JFrame();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        //frame.setUndecorated(true);
        frame.add(dp);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;
        frame.setLocation(x, y);
    }

    @FXML
    private void testSingleBrowse()
    {
        mTestingSingleDirectory = mFileChooser.showOpenDialog(null);
        if (mTestingSingleDirectory != null)
        {
            test_single_source.setText(mTestingSingleDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void testSingleModelBrowse()
    {
        testing_single_result.visibleProperty().set(false);
        mModelSingleFile = mFileChooser.showOpenDialog(null);
        if (mModelSingleFile != null)
        {
            test_single_model.setText(mModelSingleFile.getAbsolutePath());
        }
    }

    @FXML
    private void testSingleLabelBrowse()
    {
        mLabelsSingleFile = mFileChooser.showOpenDialog(null);
        if (mLabelsSingleFile != null)
        {
            test_single_label.setText(mLabelsSingleFile.getAbsolutePath());
        }
    }

    @FXML
    private void testSingleFile()
    {
        mIsMobileTesting = mIsMobile;
        if (!test_single_source.getText().equals("") && !test_single_model.getText().equals("") && !test_single_label.getText().equals(""))
        {
            if (!mServiceRunning)
            {
                testing_single_progress_bar.visibleProperty().setValue(true);
                if (mProMode)
                {
                    if (checkTestingProMode())
                    {
                        TestSingleFile testSingleFile = new TestSingleFile(test_single_source.getText(), test_single_model.getText(), test_single_label.getText(), Float.valueOf(test_single_acc.getText()));
                        mServiceRunning = true;
                        service.submit(testSingleFile);
                    } else
                    {
                        testing_single_progress_bar.visibleProperty().setValue(false);
                        showProModeError();
                        return;
                    }
                } else
                {
                    TestSingleFile testSingleFile = new TestSingleFile(test_single_source.getText(), test_single_model.getText(), test_single_label.getText(), Float.valueOf(test_single_acc.getText()));
                    mServiceRunning = true;
                    service.submit(testSingleFile);
                }
            } else
            {
                showServiceError();
            }
        } else
        {
            showDirectoryError();

        }
    }

    public class TestSingleFile implements Callable<Boolean>
    {

        private String source_file;
        private String model_file;
        private String label_file;
        private float accuracy_criteria;
        private StringBuffer buffer;
        private String file_name;
        private float acc;
        private String acc_name;

        public TestSingleFile(String source_file, String model_file, String label_file, float accuracy_criteria)
        {
            this.source_file = source_file;
            this.model_file = model_file;
            this.label_file = label_file;
            this.accuracy_criteria = accuracy_criteria;
            buffer = new StringBuffer("");
        }

        @Override
        public Boolean call() throws Exception
        {
            String line = null;
            ProcessBuilder builder;
            String line_1 = null;
            int flag = 0;
            try
            {
                String command = "python label_image.py --graph=\"" + model_file + "\" --labels=\"" + label_file + "\" --image=\"" + source_file + "\" ";
                if (mProMode)
                {
                    String input_height = test_input_height.getText().toString();
                    String input_width = test_input_width.getText().toString();
                    String input_mean = test_input_mean.getText().toString();
                    String input_std = test_input_std.getText().toString();
                    String input_layer = test_input_layer.getText().toString();
                    String output_layer = test_output_layer.getText().toString();
                    command = command + "--input_height=\"" + input_height + "\" --input_width=\"" + input_width + "\" --input_mean=\"" + input_mean + "\" --input_std=\"" + input_std + "\" --input_layer=\"" + input_layer + "\" --output_layer=\"" + output_layer + "\"";
                } else if (mIsMobileTesting)
                {
                    command = command + "--input_layer=Placeholder --output_layer=final_result --input_height=224 --input_width=224";
                } else
                {
                    command = command + "--input_layer=Placeholder --output_layer=final_result ";
                }
                System.out.println(command);
                builder = new ProcessBuilder("cmd.exe", "/c", command);
                builder.redirectErrorStream(true);
                Process process = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = r.readLine()) != null)
                {
                    System.out.println(line);
                    line_1 = line;
                    buffer.append(line + "\n");
                }
                String newString = String.valueOf(buffer);
                if (newString.toLowerCase().contains("error"))
                {
                    showError();
                    mServiceRunning = false;
                    testing_single_progress_bar.setVisible(false);
                    return null;
                }
                testing_single_text_area.appendText(newString);
                for (int k = line_1.length() - 1; k >= 0; k--)
                {
                    if (line_1.charAt(k) == ' ')
                    {
                        flag = k + 1;
                        break;
                    }
                }
                int pos = new File(source_file).getName().lastIndexOf(".");
                if (pos > 0)
                {
                    System.out.println(source_file);
                    file_name = new File(source_file).getName().substring(0, pos);
                }
                acc = Float.valueOf(line_1.substring(flag, line_1.length() - 1));
                acc_name = line_1.substring(0, flag - 1);
                process.waitFor();
            } catch (IOException ex)
            {
                System.out.println(ex.toString());
            } catch (InterruptedException ex)
            {
                System.out.println(ex.toString());
            }
            System.out.println("Name : " + file_name + " recongnized as : " + acc_name + " accuracy : " + acc);
            System.out.println("Setting Visibility");
            testing_single_result.visibleProperty().set(true);
            testing_single_progress_bar.visibleProperty().setValue(false);
            mServiceRunning = false;
            if (acc > accuracy_criteria && file_name.toLowerCase().contains(acc_name.toLowerCase()))
            {
                System.out.println("Setting text");
                testing_single_result.setText("PASS!");
                return true;
            } else
            {
                System.out.println("Setting text");
                testing_single_result.setText("Fail!");
                return false;
            }
        }
    }

    private void showTable(ArrayList<TestingData> mTestingDataArrayList)
    {
        System.out.println("Showing");
        TestingTable tt = new TestingTable();
        frame = new JFrame();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        //frame.setUndecorated(true);
        frame.add(tt);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - frame.getSize().width) / 2;
        int y = (dim.height - frame.getSize().height) / 2;

        frame.setLocation(x, y);

    }

    public class TestingTable extends JPanel
    {

        {
            float percentage = FXMLDocumentController.this.positive / FXMLDocumentController.this.total;
            percentage = percentage * 100;
            System.out.println("static block");
            TestingData tmp_testing_data;

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            System.out.println("Table start");
            JTable tbl = new JTable();
            System.out.println("Table Model");
            DefaultTableModel dtm = new DefaultTableModel(0, 0);
            String header[] = new String[]
            {
                "Name", "Recognized as", "Accuracy", "Result"
            };
            System.out.println("Setting header");
            dtm.setColumnIdentifiers(header);
            System.out.println("header set");
            tbl.setModel(dtm);
            System.out.println("for loop start");
            if (mTestingDataArrayList == null)
            {
                System.out.println("null");
            }
            System.out.println("size : " + mTestingDataArrayList.size());
            for (int i = 0; i < mTestingDataArrayList.size(); i++)
            {
                System.out.println("i : " + i);
                tmp_testing_data = mTestingDataArrayList.get(i);
                dtm.addRow(new Object[]
                {
                    tmp_testing_data.getFilename(), tmp_testing_data.getRecognized_label(), String.valueOf(tmp_testing_data.getAccuracy()), tmp_testing_data.getResult()
                });
            }
            System.out.println("For Loop end");
            this.add(new JScrollPane(tbl));
            tbl.setFillsViewportHeight(true);
            System.out.println("table added");
            JLabel jlabel = new JLabel("Success Rate : " + percentage);
            this.add(jlabel);
            JButton button = new JButton("Done");
            button.addActionListener((java.awt.event.ActionEvent e) ->
            {
                frame.setVisible(false);
            });
            this.add(button);
            System.out.println("static block end");
        }
    }

    private boolean checkTestingProMode()
    {
        if (test_input_height.getText().toString().equals(""))
        {
            return false;
        }
        if (test_input_width.getText().toString().equals(""))
        {
            return false;
        }
        if (test_input_mean.getText().toString().equals(""))
        {
            return false;
        }
        if (test_input_std.getText().toString().equals(""))
        {
            return false;
        }
        if (test_input_layer.getText().toString().equals(""))
        {
            return false;
        }
        if (test_output_layer.getText().toString().equals(""))
        {
            return false;
        }
        return true;
    }
}
