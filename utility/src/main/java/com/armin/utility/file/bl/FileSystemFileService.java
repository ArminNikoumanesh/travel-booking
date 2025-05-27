package com.armin.utility.file.bl;

import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.file.model.object.*;
import com.armin.utility.file.statics.enums.FileServiceStatus;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.statics.constants.ParameterDictionary;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.util.*;

/**
 * Exceptions error code range: 1201-1250
 */
@Primary
@Service
public class FileSystemFileService implements IFileService {

    private final JavaFileService javaFileService;
    private final BaseApplicationProperties applicationProperties;

    @Autowired
    public FileSystemFileService(JavaFileService javaFileService, BaseApplicationProperties applicationProperties) {
        this.javaFileService = javaFileService;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Collection<ContainerInfo> getContainers() throws SystemException {
        File baseFolder = javaFileService.readFile(applicationProperties.getFileCrud().getBaseFilePath());
        File[] listDirectories = baseFolder.listFiles();
        List<ContainerInfo> result = new ArrayList<>();
        if (listDirectories == null) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "path", 1208);
        }
        for (File eachDirectory : listDirectories) {
            if (eachDirectory.isDirectory()) {
                ContainerInfo containerInfo = new ContainerInfo(eachDirectory.getName());
                containerInfo.setCreated(getFileCreationTime(eachDirectory));
                containerInfo.setSize(eachDirectory.length());
                File[] listFiles = eachDirectory.listFiles();
                List<BucketInfo> buckets = new ArrayList<>();
                long bucketSizes = setContainerBuckets(listFiles, buckets);
                containerInfo.setSize(eachDirectory.length() + bucketSizes);
                containerInfo.setBuckets(buckets);
                result.add(containerInfo);
            }
        }
        return result;
    }

    @Override
    public ContainerInfo getContainer(String containerName) throws SystemException {
        File container = javaFileService.readFile(applicationProperties.getFileCrud().getBaseFilePath() + containerName);
        ContainerInfo containerInfo = new ContainerInfo(container.getName());
        containerInfo.setCreated(getFileCreationTime(container));
        File[] listFiles = container.listFiles();
        List<BucketInfo> buckets = new ArrayList<>();
        long bucketSize = setContainerBuckets(listFiles, buckets);
        containerInfo.setSize(container.length() + bucketSize);
        containerInfo.setBuckets(buckets);
        return containerInfo;
    }

    @Override
    public BucketInfo getBucket(String containerName, String bucketName) throws SystemException {
        File bucket = javaFileService.readFile(applicationProperties.getFileCrud().getBaseFilePath() + containerName + ParameterDictionary.FILE_SEPARATOR + bucketName);
        BucketInfo bucketInfo = new BucketInfo(bucket.getName());
        bucketInfo.setCreated(getFileCreationTime(bucket));
        bucketInfo.setSize(bucket.length() + getContainingFileSize(bucket.listFiles()));
        return bucketInfo;
    }

    @Override
    public Collection<FileInfo> getFiles(String containerName, String bucketName) throws SystemException {
        File folder = javaFileService.readFile(applicationProperties.getFileCrud().getBaseFilePath() + containerName + ParameterDictionary.FILE_SEPARATOR + bucketName);
        File[] listOfFiles = folder.listFiles();
        List<FileInfo> result = new ArrayList<>();
        if (listOfFiles == null) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "containerName:" + containerName + ",bucketName" + bucketName, 1201);
        }

        for (File eachFile : listOfFiles) {
            if (eachFile.isFile()) {
                FileInfo fileInfo = new FileInfo(eachFile.getName());
                fileInfo.setSize(eachFile.length());
                fileInfo.setCreated(getFileCreationTime(eachFile));
                result.add(fileInfo);
            }
        }
        return result;
    }

    @Override
    public FileServiceStatus createContainer(String containerName) {
        Path containerPath = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + containerName);
        return javaFileService.createDirectory(containerPath);
    }

    public FileServiceStatus createContainer(String containerName, String customParentPath) {
        Path containerPath = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + customParentPath + ParameterDictionary.FILE_SEPARATOR + containerName);
        return javaFileService.createDirectory(containerPath);
    }

    @Override
    public FileServiceStatus createBucket(String containerName, String bucketName) {
        Path bucketPath = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + containerName
                + ParameterDictionary.FILE_SEPARATOR + bucketName);
        return javaFileService.createDirectory(bucketPath);
    }

    public FileServiceStatus createBucket(String containerName, String bucketName, String customParentPath) {
        Path bucketPath = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + customParentPath + ParameterDictionary.FILE_SEPARATOR + containerName + ParameterDictionary.FILE_SEPARATOR + bucketName);
        return javaFileService.createDirectory(bucketPath);
    }

    @Override
    public FileServiceStatus deleteContainer(String containerName) {
        Path path = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + containerName);
        try {
            Files.delete(path);
        } catch (IOException e) {
            return FileServiceStatus.FAILURE;
        }
        return FileServiceStatus.SUCCESS;
    }

    @Override
    public FileServiceStatus deleteBucket(String containerName, String bucketName) {
        Path path = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + containerName
                + ParameterDictionary.FILE_SEPARATOR + bucketName);
        try {
            Files.delete(path);
        } catch (IOException e) {
            return FileServiceStatus.FAILURE;
        }
        return FileServiceStatus.SUCCESS;
    }

    @Override
    public FileServiceStatus deleteFiles(String filePaths) {
        Path path = Paths.get(filePaths);
        try {
            Files.delete(path);
        } catch (IOException e) {
            return FileServiceStatus.FAILURE;
        }
        return FileServiceStatus.SUCCESS;
    }

    @Override
    public FileServiceStatus deleteFilesByBasePath(String filePaths) {
        Path path = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + filePaths);
        try {
            Files.delete(path);
        } catch (IOException e) {
            return FileServiceStatus.FAILURE;
        }
        return FileServiceStatus.SUCCESS;
    }

    @Override
    public FileServiceStatus deleteTempFiles(String filePaths) {
        Path path = Paths.get(applicationProperties.getFileCrud().getTempFilePath() + filePaths);
        try {
            Files.delete(path);
        } catch (IOException e) {
            return FileServiceStatus.FAILURE;
        }
        return FileServiceStatus.SUCCESS;
    }

    @Override
    public FileServiceStatus deleteFiles(Collection<String> filePaths) {
        for (String path : filePaths) {
            deleteFiles(path);
        }
        return FileServiceStatus.SUCCESS;
    }

    @Override
    public String uploadImage(MultipartFile file) throws SystemException {
        if (file == null || applicationProperties.getFileCrud().getTempFilePath() == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, null,
                    1202);
        if (file.isEmpty()) {
            throw new SystemException(SystemError.UPLOADED_FILE_CORRUPTED, "file", 1203);
        }

        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
            File compressedImageFile = new File(applicationProperties.getFileCrud().getTempFilePath() + fileName);
            InputStream inputStream = file.getInputStream();
            OutputStream outputStream = new FileOutputStream(compressedImageFile);

            float imageQuality = applicationProperties.getFileCrud().getImageQuality();

            BufferedImage bufferedImage = ImageIO.read(inputStream);
            Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(extension);

            if (!imageWriters.hasNext())
                throw new IllegalStateException("Writers Not Found!!");

            ImageWriter imageWriter = imageWriters.next();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);
            ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
            if (!Objects.equals(extension, "png")) {
                imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                imageWriteParam.setCompressionQuality(imageQuality);
            }
            imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

            // close all streams
            inputStream.close();
            outputStream.close();
            imageOutputStream.close();
            imageWriter.dispose();
            return applicationProperties.getFileCrud().getTempFileUrl() + fileName;
        } catch (IOException e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();
            System.out.println(stackTrace);
            throw new SystemException(SystemError.STORE_FILE_FAILED, "file" + file.getName(), 1204);
        }
    }

    @Override
    public Collection<String> upload(Collection<MultipartFile> files) throws SystemException {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null || applicationProperties.getFileCrud().getTempFilePath() == null)
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, null,
                        1202);
            if (file.isEmpty())
                throw new SystemException(SystemError.UPLOADED_FILE_CORRUPTED, "file", 1203);

            try {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
                Path rootLocation = Paths.get(applicationProperties.getFileCrud().getTempFilePath());
                Files.copy(file.getInputStream(), rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                fileNames.add(applicationProperties.getFileCrud().getTempFileUrl() + fileName);
            } catch (IOException e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);
                String stackTrace = stringWriter.toString();
                System.out.println(stackTrace);
                throw new SystemException(SystemError.STORE_FILE_FAILED, "file" + file.getName(), 1204);
            }
        }
        return fileNames;
    }

    @Override
    public String manipulateAttachments(String oldFilePath, String newFilePath, String containerName, String bucketName, String customParentPath) throws SystemException {
        if (customParentPath != null) {
            if (!Files.exists(Paths.get(applicationProperties.getFileCrud().getBaseFilePath() +
                    customParentPath + ParameterDictionary.FILE_SEPARATOR + containerName + ParameterDictionary.FILE_SEPARATOR + bucketName))) {
                createContainer(containerName, customParentPath);
                createBucket(containerName, bucketName, customParentPath);
            }
            return manipulate(oldFilePath, newFilePath, customParentPath + ParameterDictionary.FILE_SEPARATOR + containerName, bucketName);
        } else {
            return manipulate(oldFilePath, newFilePath, containerName, bucketName);
        }
    }

    @Override
    public void replacementFolder(String root, String sourcePath, String destinationPath, String destinationFile) throws IOException, SystemException {
        String baseRoot = this.applicationProperties.getFileCrud().getRoot();
        File sourceFolder = new File(baseRoot + root + sourcePath);
        File destFolder = new File(baseRoot + root + destinationPath);
        File oldFolder = new File(baseRoot + root + destinationPath + destinationFile);
        if (sourceFolder.exists() && destFolder.getParentFile().exists()) {
            if (oldFolder.exists()) {
                FileUtils.deleteDirectory(oldFolder);
            }
            FileUtils.copyDirectoryToDirectory(sourceFolder, destFolder);
        } else {
            throw new SystemException(SystemError.DESTINATION_PATH_NOT_EXISTS, "file not found", 8008);
        }

    }

    public String manipulate(String oldFilePath, String newFilePath, String containerName, String bucketName) throws SystemException {
        String[] pics = null;
        if (oldFilePath != null) {
            pics = oldFilePath.split(",");
            List<String> newPics = new ArrayList<>();
            if (newFilePath != null) {
                newPics = Arrays.asList(newFilePath.split(","));
            }
            boolean mustDelete;
            for (String eachExistPic : pics) {
                eachExistPic = eachExistPic.trim();
                mustDelete = true;
                for (String eachNewPic : newPics) {
                    eachNewPic = eachNewPic.trim();
                    if (eachNewPic.equals(eachExistPic)) {
                        mustDelete = false;
                        break;
                    }
                }
                if (mustDelete) {
                    String existingFileName = eachExistPic.substring(eachExistPic.lastIndexOf(ParameterDictionary.FILE_SEPARATOR) + 1);
                    deleteFiles(createFilePath(containerName, bucketName, existingFileName));
                }
            }
        }
        if (newFilePath != null && newFilePath.length() > 1) {
            String[] newPics = newFilePath.split(",");
            List<String> fileNames = new ArrayList<>();
            boolean mustAdd;
            for (String eachNewPic : newPics) {
                eachNewPic = eachNewPic.trim();
                mustAdd = true;
                if (pics != null) {
                    for (String eachExistPic : pics) {
                        eachExistPic = eachExistPic.trim();
                        if (eachNewPic.equals(eachExistPic)) {
                            fileNames.add(eachExistPic);
                            mustAdd = false;
                            break;
                        }
                    }
                }
                if (mustAdd && eachNewPic.length() > 1) {
                    String extension = FilenameUtils.getExtension(eachNewPic);
                    String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
                    String existingFileName = eachNewPic.substring(eachNewPic.lastIndexOf(ParameterDictionary.FILE_SEPARATOR) + 1);
                    try {
                        if (!Files.exists(Paths.get(applicationProperties.getFileCrud().getTempFilePath() + existingFileName))) {
                            throw new SystemException(SystemError.FILE_NOT_FOUND, "file not found", 1209);
                        }
                        Files.move(Paths.get(applicationProperties.getFileCrud().getTempFilePath() + existingFileName), Paths.get(createFilePath(containerName, bucketName, fileName)));
                    } catch (IOException ignored) {
                    }
                    fileNames.add(containerName + ParameterDictionary.FILE_SEPARATOR + bucketName + ParameterDictionary.FILE_SEPARATOR + fileName);
                }
            }
            return !fileNames.isEmpty() ? String.join(",", fileNames) : null;
        } else {
            return null;
        }
    }

    @Override
    public <T> void manipulateAttachments(T oldModel, T newModel) throws SystemException {
        if (oldModel == null && newModel == null) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "model", 1208);
        }

        T model = oldModel != null ? oldModel : newModel;
        for (Field field : model.getClass().getDeclaredFields()) {
            Attachment attribute = field.getAnnotation(Attachment.class);
            if (attribute != null) {
                try {
                    field.setAccessible(true);
                    Object oldModelField = oldModel != null ? field.get(oldModel) : null;
                    Object newModelField = newModel != null ? field.get(newModel) : null;
                    if (oldModelField != null) {
                        if (!oldModelField.equals(newModelField)) {
                            String name = manipulateAttachments(oldModelField.toString(), newModelField != null ? newModelField.toString() : null, attribute.container(), attribute.bucket(), null);
                            if (newModel != null) {
                                field.set(newModel, name);
                            }
                        }
                    } else if (newModelField != null) {
                        String name = manipulateAttachments(null, newModelField.toString(), attribute.container(), attribute.bucket(), null);
                        field.set(newModel, name);
                    }

                } catch (IllegalAccessException e) {
                    throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "model", 1206);
                }
            }
        }
    }

    @Override
    public <T> void manipulateAttachments(T oldModel, T newModel, String customParentPath) throws SystemException {
        if (oldModel == null && newModel == null) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "model", 1208);
        }

        T model = oldModel != null ? oldModel : newModel;
        for (Field field : model.getClass().getDeclaredFields()) {
            Attachment attribute = field.getAnnotation(Attachment.class);
            if (attribute != null) {
                try {
                    field.setAccessible(true);
                    Object oldModelField = oldModel != null ? field.get(oldModel) : null;
                    Object newModelField = newModel != null ? field.get(newModel) : null;
                    if (oldModelField != null) {
                        if (!oldModelField.equals(newModelField)) {
                            String name = manipulateAttachments(oldModelField.toString(), newModelField != null ? newModelField.toString() : null, attribute.container(), attribute.bucket(), customParentPath);
                            if (newModel != null) {
                                field.set(newModel, name);
                            }
                        }
                    } else if (newModelField != null) {
                        String name = manipulateAttachments(null, newModelField.toString(), attribute.container(), attribute.bucket(), customParentPath);
                        field.set(newModel, name);
                    }

                } catch (IllegalAccessException e) {
                    throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "model", 1206);
                }
            }
        }
    }

    @Override
    public DownloadResult download(String containerName, String bucketName, String fileName) throws SystemException {
        String filePath = applicationProperties.getFileCrud().getBaseFilePath() + containerName + ParameterDictionary.FILE_SEPARATOR + bucketName + ParameterDictionary.FILE_SEPARATOR + fileName;
        DownloadResult result = new DownloadResult();
        Path path = new File(filePath).toPath();
        result.setFile(javaFileService.readFile(path));
        try {
            result.setMimeType(Files.probeContentType(path));
        } catch (IOException e) {
            throw new SystemException(SystemError.IO_EXCEPTION, "mimeType", 1207);
        }

        return result;
    }

    private Timestamp getFileCreationTime(File file) {
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return new Timestamp(fileAttributes.creationTime().toMillis());
        } catch (IOException ignored) {
            return null;
        }
    }

    private long setContainerBuckets(File[] listFiles, List<BucketInfo> buckets) {
        long totalSize = 0;
        if (listFiles != null) {
            for (File eachFile : listFiles) {
                if (eachFile.isDirectory()) {
                    BucketInfo bucketInfo = new BucketInfo(eachFile.getName());
                    long bucketSize = getContainingFileSize(eachFile.listFiles());
                    bucketInfo.setSize(bucketSize);
                    bucketInfo.setCreated(getFileCreationTime(eachFile));
                    totalSize += bucketSize;
                    buckets.add(bucketInfo);
                }
            }
        }
        return totalSize;
    }

    private long getContainingFileSize(File[] listFiles) {
        long totalSize = 0;
        if (listFiles != null) {
            for (File eachFile : listFiles) {
                totalSize += eachFile.length();
            }
        }
        return totalSize;
    }

    private String createFilePath(String container, String bucket, String fileName) {
        return applicationProperties.getFileCrud().getBaseFilePath()
                + container + ParameterDictionary.FILE_SEPARATOR
                + bucket + ParameterDictionary.FILE_SEPARATOR
                + fileName;
    }
}
