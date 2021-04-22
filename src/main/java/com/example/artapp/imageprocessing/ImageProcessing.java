package com.example.artapp.imageprocessing;


import com.example.artapp.service.UserService;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageProcessing {


    @Autowired
    private UserService userService;

    public static Mat loadImage(String imagePath) {
        Imgcodecs imageCodecs = new Imgcodecs();
        return imageCodecs.imread(imagePath);
    }

    public static void saveImage(Mat imageMatrix, String targetPath) {
        Imgcodecs imgcodecs = new Imgcodecs();
        imgcodecs.imwrite(targetPath, imageMatrix);
    }

    public void processImage(String imagePath, String model){
        try {
            OpenCV.loadShared();
        } catch (ExceptionInInitializerError e) {
            System.out.println("OPENCV.LOADSHARE() ERROR");
        }

        Net net = Dnn.readNetFromTorch("models/" + model);
        System.out.println(imagePath);

        Mat image1 = loadImage(imagePath);
        Mat image = new Mat();
        Size sz = new Size(600,400);
        Imgproc.resize( image1, image, sz );
        Scalar mean = new Scalar(103.939, 116.779, 123.680);
        net.setInput(Dnn.blobFromImage(image, 1.0, image.size(), mean, false, false));


        Mat result = net.forward();
       // String savePath = "results/" + userService.getCurrentUser().getUserId() +"/"+imagePath.split("/")[1];\
        String savePath = "results/" + userService.getCurrentUser().getUserId() +"/download.jpg";
        int H = result.size(2);
        int W = result.size(3);

// step 1: reshape it to a long vertical strip:
        Mat strip = result.reshape(1, H * 3);

// step 2: collect the color planes into a list:
        List<Mat> lis = new ArrayList<>();
        lis.add(strip.submat(0,H, 0,W));
        lis.add(strip.submat(H,2*H, 0,W));
        lis.add(strip.submat(2*H,3*H, 0,W));

// step 3: merge planes into final bgr image
        Mat bgr = new Mat();
        Core.merge(lis, bgr);

// last: add the mean value
        Core.add(bgr, new Scalar(103.939, 116.779, 123.680), bgr);

        saveImage(bgr, savePath);

    }


}
