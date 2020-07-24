package com.sample.bot.line.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
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

    public String analyze(InputStream is) throws IOException {

        DetectLabelsRequest request = new DetectLabelsRequest();
        request.withImage(new Image().withBytes(ByteBuffer.wrap(IOUtils.toByteArray(is))));

        AWSCredentials credential = new BasicAWSCredentials(accessKey, secretKey);

        AmazonRekognitionClient client = new AmazonRekognitionClient(credential);
        DetectLabelsResult result = client.detectLabels(request);
        List<Label> labels = result.getLabels();
        String res = "";
        for (Label label: labels) {
            res += label.getName() + ": " + Math.round(label.getConfidence()) + "%" + System.getProperty("line.separator");
        }
        return res;

    }
}