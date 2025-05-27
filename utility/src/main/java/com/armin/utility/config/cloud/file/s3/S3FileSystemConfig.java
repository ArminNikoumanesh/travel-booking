package com.armin.utility.config.cloud.file.s3;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

@Setter
@Getter
public class S3FileSystemConfig implements Diffable<S3FileSystemConfig> {
    private String endpoint;
    private String secretKey;
    private String accessKey;
    private String bucketName;
    private String baseDownloadPath;

    @Override
    public DiffResult<S3FileSystemConfig> diff(S3FileSystemConfig that) {
        return new DiffBuilder<>(this, that, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("endpoint", this.endpoint, that.endpoint)
                .append("secretKey", this.secretKey, that.secretKey)
                .append("accessKey", this.accessKey, that.accessKey)
                .append("bucketName", this.bucketName, that.bucketName)
                .append("baseDownloadPath", this.baseDownloadPath, that.baseDownloadPath)
                .build();
    }
}
