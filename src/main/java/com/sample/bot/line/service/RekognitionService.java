package com.sample.bot.line.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.util.IOUtils;

@Service
public class RekognitionService {

    @Value("${aws.accessKey}")
    String accessKey;

    @Value("${aws.secretKey}")
    String secretKey;

    public String detect(InputStream is) throws IOException {
        // AWS API
        AWSCredentials credential = new BasicAWSCredentials(accessKey, secretKey);

        // Amazon Rekognition
        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
                .standard()
                .withRegion(Regions.AP_NORTHEAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credential))
                .build();

        DetectLabelsRequest request = new DetectLabelsRequest().withImage(new Image().withBytes(ByteBuffer.wrap(IOUtils.toByteArray(is))));

        // 物体検出
        DetectLabelsResult result = rekognitionClient.detectLabels(request);

        List<Label> labels = result.getLabels();
        String res = "";
        for (Label label: labels) {
            res += label.getName() + ": " + Math.round(label.getConfidence()) + "%" + System.getProperty("line.separator");
        }
        return res;
    }

}