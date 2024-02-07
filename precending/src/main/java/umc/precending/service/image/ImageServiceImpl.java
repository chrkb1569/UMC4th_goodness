package umc.precending.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.bucketName}")
    private String bucketName;

    @Override
    public String saveImage(MultipartFile file, String storedName) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getInputStream().available());

            amazonS3Client.putObject(bucketName, storedName, file.getInputStream(), metadata);
            return amazonS3Client.getUrl(bucketName, storedName).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}