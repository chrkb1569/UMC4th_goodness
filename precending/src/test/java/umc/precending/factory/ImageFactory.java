package umc.precending.factory;

import org.springframework.mock.web.MockMultipartFile;
import umc.precending.domain.image.MemberImage;
import umc.precending.domain.image.PostImage;

public enum ImageFactory {
    IMAGE_1("IMAGE1.jpeg", "STORED_NAME_1", "ACCESS_URL_1"),
    IMAGE_2("IMAGE2.jpeg", "STORED_NAME_2", "ACCESS_URL_2"),
    IMAGE_3("IMAGE3.jpeg", "STORED_NAME_3", "ACCESS_URL_3")
    ;

    ImageFactory(String originalName, String storedName, String accessUrl) {
        this.originalName = originalName;
        this.storedName = storedName;
        this.accessUrl = accessUrl;
    }

    private String originalName;
    private String storedName;
    private String accessUrl;

    public MemberImage getMemberImage() {
        MockMultipartFile file = new MockMultipartFile("file", this.originalName,
                "multipart/form-data", new byte[]{});
        return new MemberImage(file);
    }

    public PostImage getPostImage() {
        return new PostImage(this.originalName, this.accessUrl);
    }

    public String getOriginalName() {
        return this.originalName;
    }

    public String getStoredName() {
        return this.storedName;
    }

    public String getAccessUrl() {
        return this.accessUrl;
    }
}
