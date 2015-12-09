package com.org.actions.rest;

import weka.classifiers.meta.RandomSubSpace;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Created by BezyApps on 11/18/2015.
 */
public class LocationHelper {

    private static LocationHelper locationHelper;
    private StringBuilder headBuilder;
    private static RandomSubSpace randomSubSpace;
    private LocationHelper() {
        buildHeader();
    }


    public RandomSubSpace getModel(){
        return randomSubSpace;
    }


    private static Object getModelFromResources(String modelName) throws IOException {
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(modelName);
            Object model = ((new ObjectInputStream(inputStream)).readObject());
            return model;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LocationHelper getInstance() {
        if (locationHelper == null) {
            locationHelper = new LocationHelper();
            try {
                randomSubSpace = (RandomSubSpace) getModelFromResources("big.model");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return locationHelper;
    }

    private void buildHeader() {
        headBuilder = new StringBuilder("@relation test_set\n\n");
        for (int i = 1; i < 65; i++) {
            headBuilder.append("@attribute data_" + i + " numeric\n");
        }
      //   headBuilder.append("@attribute class {Faculty_Room_1,Faculty_Room_2}\n\n");
       // headBuilder.append("@attribute class {e303,e304}\n\n");
        ///Change image controller classes too
       headBuilder.append("@attribute class {Coordinator_Office,E_301,E_302,E_303,E_304,E_305,E_307,E_309,Male_Staff_Washroom,Optical_Communication_Lab,E_310,E_311,E_Lab_308,Faculty_Room,Left_End,Left_to_Middle,Male_WashRoom,Middle_To_Left,Middle_to_Right,Right_End,Right_To_Middle}\n\n");
        headBuilder.append("@data\n");
    }

    public StringBuilder getARFF_Header() {
        return headBuilder;
    }
}
