package com.armin.utility.config.cloud.file;

import com.armin.utility.config.cloud.file.s3.S3FileSystemConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

@Getter
@Setter
@NoArgsConstructor
public class FileCrud implements Diffable<FileCrud> {
    private String root;
    private String baseUrl;
    private String baseFilePath;
    private String tempFilePath;
    private String tempFileUrl;
    private float imageQuality;
    private S3FileSystemConfig s3Config;

    public FileCrud(FileCrud fileCrud) {
        this.root = fileCrud.getRoot();
        this.baseUrl = fileCrud.getBaseUrl();
        this.baseFilePath = fileCrud.getBaseFilePath();
        this.tempFilePath = fileCrud.getTempFilePath();
        this.tempFileUrl = fileCrud.getTempFileUrl();
        this.imageQuality = fileCrud.getImageQuality();
        this.s3Config = fileCrud.getS3Config();
    }

    @Override
    public DiffResult<FileCrud> diff(FileCrud fileCrud) {
        return new DiffBuilder(this, fileCrud, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("root", this.root, fileCrud.getRoot())
                .append("baseUrl", this.baseUrl, fileCrud.getBaseUrl())
                .append("baseFilePath", this.baseFilePath, fileCrud.getBaseFilePath())
                .append("tempFilePath", this.tempFilePath, fileCrud.getTempFilePath())
                .append("tempFileUrl", this.tempFileUrl, fileCrud.getTempFileUrl())
                .append("imageQuality", this.imageQuality, fileCrud.getImageQuality())
                .append("s3Config", this.s3Config.diff(fileCrud.getS3Config()))
                .build();
    }
}
