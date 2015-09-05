package com.thsware.secretbook;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 世祥 on 2015/9/4.
 */
public class ActivityCollector {

    private static List<Activity> activities=new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity ac : activities){
            if (!ac.isFinishing()){
                ac.finish();
            }
        }
    }
}
