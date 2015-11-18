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
                randomSubSpace = (RandomSubSpace) getModelFromResources("RandomSubSpace.model");
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
        headBuilder.append("@attribute class {e303,e304}\n\n");
        headBuilder.append("@data\n");
    }

    public StringBuilder getARFF_Header() {
        return headBuilder;
    }
}
