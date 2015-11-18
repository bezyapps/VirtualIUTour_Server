package com.org.actions.rest;

import boofcv.alg.descriptor.UtilFeature;
import georegression.struct.point.Point2D_F64;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ddogleg.struct.FastQueue;

import boofcv.abst.feature.associate.AssociateDescription;
import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.struct.feature.TupleDesc;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageUInt8;


public class Matcher<Desc extends TupleDesc> {

    DetectDescribePoint<ImageFloat32, Desc> detDesc;
    AssociateDescription<Desc> associate;

    FastQueue<Desc> listSrc;
    FastQueue<Desc> listDst;
    FastQueue<Point2D_F64> locationSrc = new FastQueue<Point2D_F64>(Point2D_F64.class, true);
    FastQueue<Point2D_F64> locationDst = new FastQueue<Point2D_F64>(Point2D_F64.class, true);

    List<Point2D_F64> pointsSrc = new ArrayList<Point2D_F64>();
    List<Point2D_F64> pointsDst = new ArrayList<Point2D_F64>();

    public Matcher() {
        detDesc = CreateDetectorDescriptor.create(CreateDetectorDescriptor.DETECT_FH
                , CreateDetectorDescriptor.DESC_SURF, ImageFloat32.class);
        listSrc = UtilFeature.createQueue(detDesc, 0);
        listDst = UtilFeature.createQueue(detDesc, 0);

    }

    public Desc[] detectDescribe(ImageFloat32 imageFloat32, int i) {
        int count = 0;
        detDesc.detect(imageFloat32);
        describeImage(listSrc, locationSrc);
        System.out.println(i + "    " + listSrc.getSize());
        return listSrc.data;


        //for(Desc d : listSrc.data)


        //System.out.println(d.getDouble(32) );

        //  	System.out.println(d.getDouble(0));
        //	System.out.println(d.getDouble(d.size()-1));


    }

    public Desc[] detectDescribe(ImageFloat32 imageFloat32) {
        int count = 0;
        detDesc.detect(imageFloat32);
        describeImage(listSrc, locationSrc);
        System.out.println("Feature Count: " + listSrc.getSize());
        return listSrc.data;


        //for(Desc d : listSrc.data)


        //System.out.println(d.getDouble(32) );

        //  	System.out.println(d.getDouble(0));
        //	System.out.println(d.getDouble(d.size()-1));


    }


    private void describeImage(FastQueue<Desc> listDesc, FastQueue<Point2D_F64> listLoc) {
        listDesc.reset();
        listLoc.reset();
        int N = detDesc.getNumberOfFeatures();
        for (int i = 0; i < N; i++) {
            listLoc.grow().set(detDesc.getLocation(i));
            listDesc.grow().setTo(detDesc.getDescription(i));
        }
    }
}