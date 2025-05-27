package com.armin.utility.file.model.object;

import jakarta.validation.constraints.NotNull;

public class UploadContext {
    @NotNull
    private String physicalPath;
    @NotNull
    private String fileName;

    public String getPhysicalPath() {
        return physicalPath;
    }

    public void setPhysicalPath(String physicalPath) {
        this.physicalPath = physicalPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
