package com.example.steam.infra;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.WritePendingException;
import java.util.UUID;

@Slf4j
@Configuration
public class S3Util {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.cloudfront.url}")
    private String cloudfrontDomain;

    private S3Client s3Client;

    // 이미지 크기
    private static final int IMAGE_WIDTH = 184;
    private static final int IMAGE_HEIGHT = 184;
    // webp 품질
    private  static final int WEBP_QUALITY = 80;

    @PostConstruct
    public void initS3Client() {
        s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

//    @Bean
//    public S3Presigner s3Presigner() {
//        return S3Presigner.builder()
//                .credentialsProvider(
//                        StaticCredentialsProvider.create(
//                                AwsBasicCredentials.create(accessKey, secretKey)
//                        )
//                )
//                .region(Region.of(region))
//                .build();
//    }

    public String uploadImageFile(MultipartFile imageFile, String S3DirectoryName) throws IOException {
        // 1. WebP 변환 및 리사이즈
        byte[] webpBytes = convertToWebp(imageFile);

        // 2. S3에 저장할 파일명 생성 (.webp 확장자)
        String key = S3DirectoryName + "/" + UUID.randomUUID() + ".webp";

        // 3. S3 업로드 요청 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentLength((long) webpBytes.length)
                .contentType("image/webp")
                .build();

        // 4. S3에 업로드
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(webpBytes));
        } catch (S3Exception e) {
            throw new SteamException(ErrorCode.UPLOAD_FAIL);
        }

        // 5. CloudFront URL 반환
        return cloudfrontDomain + "/" + key;
    }

    private byte[] convertToWebp(MultipartFile imageFile) {
        try {
            ImmutableImage image = ImmutableImage.loader()
                    .fromStream(imageFile.getInputStream());

            ImmutableImage resizedImage = image.scaleTo(IMAGE_WIDTH, IMAGE_HEIGHT);

            // WebP 변환 (바이트 배열로 직접 추출)
            return resizedImage.bytes(WebpWriter.DEFAULT.withQ(WEBP_QUALITY));
        } catch (Exception e) {
            throw new SteamException(ErrorCode.IMAGE_CONVERT_FAIL);
        }
    }




}
