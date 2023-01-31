//package com.MyMusic.v1.bin.scratchpad;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//
//import java.io.File;
//
//public class Main {
//    public static void main(String[] args) {
//        // Replace these values with your own
//        String accessKeyId = "AKIA2RF2TVJI3MTYMY6V";
//        String secretAccessKey = "MXlaI0v7hsgALaFioK+nC1MqJhCIW2imLtK7Hvrx";
//        String bucketName = "mymusicapplication";
//        String filePath = "C:\\Users\\iorda\\Downloads\\MyMusic\\MyMusic\\src\\main\\resources\\image.jpg";
//
//        // Create an S3 client
//        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
//        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withRegion("us-east-1")
//                .build();
//
//        // Upload the file to the S3 bucket
//        PutObjectRequest request = new PutObjectRequest(bucketName, "file.txt", new File(filePath));
//        s3Client.putObject(request);
//    }
//}
//
