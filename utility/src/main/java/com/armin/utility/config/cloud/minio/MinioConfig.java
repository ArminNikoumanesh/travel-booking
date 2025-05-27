package com.armin.utility.config.cloud.minio;

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
