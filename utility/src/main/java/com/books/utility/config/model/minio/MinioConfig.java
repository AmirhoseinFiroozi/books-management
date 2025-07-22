package com.books.utility.config.model.minio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinioConfig {
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
