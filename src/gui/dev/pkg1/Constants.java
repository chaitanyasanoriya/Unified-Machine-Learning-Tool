package gui.dev.pkg1;

import java.io.File;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author WildFire
 */
public class Constants
{

    public static final int NORMAL_MODE = 1;
    public static final int HIGH_FACE_ACCURACY_MODE = 2;
    public static final int MOBILE_MODE = 3;
    public static final int MOBILE_HIGH_FACE_ACCURACY_MODE = 4;

    private static ArrayList<File> mAvailableCNNDirectories = null;

    public static ArrayList<File> getmAvailableCNNDirectories()
    {
        return mAvailableCNNDirectories;
    }

    public static void setmAvailableCNNDirectories(ArrayList<File> mAvailableCNNDirectories)
    {
        Constants.mAvailableCNNDirectories = mAvailableCNNDirectories;
    }

}
