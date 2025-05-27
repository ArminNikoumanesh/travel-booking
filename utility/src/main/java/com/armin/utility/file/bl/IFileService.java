package com.armin.utility.file.bl;

import com.armin.utility.file.model.object.BucketInfo;
import com.armin.utility.file.model.object.ContainerInfo;
import com.armin.utility.file.model.object.DownloadResult;
import com.armin.utility.file.model.object.FileInfo;
import com.armin.utility.file.statics.enums.FileServiceStatus;
import com.armin.utility.object.SystemException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

public interface IFileService {
    Collection<ContainerInfo> getContainers() throws SystemException;

    ContainerInfo getContainer(String containerName) throws SystemException;

    BucketInfo getBucket(String containerName, String bucketName) throws SystemException;

    Collection<FileInfo> getFiles(String containerName, String bucketName) throws SystemException;

    FileServiceStatus createContainer(String containerName);

    FileServiceStatus createBucket(String containerName, String bucketName);

    FileServiceStatus deleteContainer(String containerName);

    FileServiceStatus deleteBucket(String containerName, String bucketName);

    FileServiceStatus deleteFiles(String filePaths) throws SystemException;

    FileServiceStatus deleteFilesByBasePath(String filePaths) throws SystemException;

    FileServiceStatus deleteTempFiles(String filePaths);

    FileServiceStatus deleteFiles(Collection<String> filePaths) throws SystemException;

    String uploadImage(MultipartFile file) throws SystemException;

    Collection<String> upload(Collection<MultipartFile> files) throws SystemException;

    String manipulateAttachments(String oldFilePaths, String newFilePaths, String containerName, String bucketName, String customParentPath) throws SystemException;

    <T> void manipulateAttachments(T oldModel, T newModel) throws SystemException;

    <T> void manipulateAttachments(T oldModel, T newModel, String customParentPath) throws SystemException;

    DownloadResult download(String containerName, String bucketName, String fileName) throws SystemException;

    void replacementFolder(String root, String sourcePath, String destinationPath, String destinationFile) throws IOException, SystemException;

}
