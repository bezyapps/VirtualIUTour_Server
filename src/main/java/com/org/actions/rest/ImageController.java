package com.org.actions.rest;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.ImageFloat32;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ericbhatti on 11/18/15.
 */
public class ImageController implements ModelDriven<Object> {

    private static final long serialVersionUID = 89268917575477696L;
    ImageTest imageTest = new ImageTest();
    Matcher matcher = new Matcher();

    public HttpHeaders create() throws Exception {
        String out = "nothing";
        long start = System.currentTimeMillis();
        out = convert(imageTest.getData());
        long end = System.currentTimeMillis();

        System.out.println("TIME: " + String.valueOf(end - start));
        imageTest.setData("");
        imageTest.setLocation(out);
        return new DefaultHttpHeaders("create");
    }


    private String convert(String base64) throws Exception
    {

        byte[] data = Base64.decodeBase64(base64);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(data));
        ImageFloat32 float32 = new ImageFloat32(320,240);
        ConvertBufferedImage.convertFrom(bufferedImage, float32);
        int size = matcher.detectDescribe(float32).length;
        return String.valueOf(size);
    }

    public byte[] hexStringToByteArray(String s) {
        s = s.replace(" ", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    @Override
    public Object getModel() {
        return imageTest;
    }

    public HttpHeaders index() {
        imageTest.setLocation("The world is now!");
        return new DefaultHttpHeaders("index").disableCaching();
    }
}
