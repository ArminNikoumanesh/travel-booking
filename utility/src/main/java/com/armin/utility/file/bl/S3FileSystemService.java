package com.armin.utility.file.bl;//package com.armin.utility.file.bl;
//
//import com.amazonaws.SdkClientException;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.CopyObjectRequest;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.armin.utility.config.property.ApplicationProperties;
//import com.armin.utility.file.model.object.*;
//import com.armin.utility.file.statics.enums.FileServiceStatus;
//import com.armin.utility.model.object.SystemError;
//import com.armin.utility.model.object.SystemException;
//import com.armin.utility.statics.constants.ParameterDictionary;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.joda.time.DateTime;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
///**
// * Exceptions error code range: 1201-1250
// */
//@Service("s3")
//public class S3FileSystemService implements IFileService {
//    private final ApplicationProperties applicationProperties;
//    private final AmazonS3 amazonS3;
//
//    public S3FileSystemService(ApplicationProperties applicationProperties) {
//        this.applicationProperties = applicationProperties;
//        BasicAWSCredentials credentials = new BasicAWSCredentials(getAccessKey(), getSecretKey());
//        this.amazonS3 = AmazonS3ClientBuilder.standard()
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(getEndpoint(), "us-east-1"))
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .build();
//    }
//
//    @Override
//    public Collection<ContainerInfo> getContainers() throws SystemException {
//        return amazonS3.listBuckets().stream().map(o -> new ContainerInfo(o.getName())).collect(Collectors.toList());
//
//    }
//
//    @Override
//    public ContainerInfo getContainer(String containerName) throws SystemException {
////        amazonS3.getObject();
//        return null;
//    }
//
//    @Override
//    public BucketInfo getBucket(String containerName, String bucketName) throws SystemException {
//        return null;
//    }
//
//    @Override
//    public Collection<FileInfo> getFiles(String containerName, String bucketName) throws SystemException {
//        return null;
//    }
//
//    @Override
//    public FileServiceStatus createContainer(String containerName) {
//        return null;
//    }
//
//    @Override
//    public FileServiceStatus createBucket(String containerName, String bucketName) {
//        return null;
//    }
//
//    @Override
//    public FileServiceStatus deleteContainer(String containerName) {
//        return null;
//    }
//
//    @Override
//    public FileServiceStatus deleteBucket(String containerName, String bucketName) {
//        return null;
//    }
//
//    @Override
//    public FileServiceStatus deleteFiles(String filePaths) throws SystemException {
//        return deleteTempFiles(filePaths);
//    }
//
//    @Override
//    public FileServiceStatus deleteTempFiles(String filePaths) {
//        amazonS3.deleteObject(getBucketName(), filePaths);
//
//        return FileServiceStatus.SUCCESS;
//    }
//
//    //todo speak
//    @Override
//    public FileServiceStatus deleteFiles(Collection<String> filePaths) throws SystemException {
//        try {
//            for (String filePath : filePaths) {
//                deleteFiles(filePath);
//            }
//        } catch (SdkClientException e) {
//            throw new SystemException(SystemError.FILE_NOT_FOUND, "can't delete file", 1220);
//        }
//        return FileServiceStatus.SUCCESS;
//
//    }
//
//    @Override
//    public String uploadImage(MultipartFile file) throws SystemException {
//        if (file == null || applicationProperties.getFileCrud().getTempFilePath() == null)
//            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, null,
//                    1202);
//        if (file.isEmpty()) {
//            throw new SystemException(SystemError.UPLOADED_FILE_CORRUPTED, "file", 1203);
//        }
//        try {
//            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
//
//            File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
//            file.transferTo(convertedFile);
//            String key = applicationProperties.getFileCrud().getTempFilePath() + fileName;
//            if (ParameterDictionary.FILE_SEPARATOR.equals(String.valueOf(key.charAt(0)))) {
//                key = key.replaceFirst(ParameterDictionary.FILE_SEPARATOR, "");
//            }
//
//            PutObjectRequest putObjectRequest = new PutObjectRequest(getBucketName(), key, convertedFile).withCannedAcl(CannedAccessControlList.PublicRead);
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setHttpExpiresDate(new DateTime().plusMinutes(1).toDate());
//            putObjectRequest.setMetadata(objectMetadata);
//            amazonS3.putObject(putObjectRequest);
//
//            return getBaseDownloadPath() + key;
//        } catch (IOException | SdkClientException e) {
//            StringWriter stringWriter = new StringWriter();
//            PrintWriter printWriter = new PrintWriter(stringWriter);
//            e.printStackTrace(printWriter);
//            String stackTrace = stringWriter.toString();
//            System.out.println(stackTrace);
//            throw new SystemException(SystemError.STORE_FILE_FAILED, "file" + file.getName(), 1204);
//        }
//    }
//
//    @Override
//    public Collection<String> upload(Collection<MultipartFile> files) throws SystemException {
//        List<String> list = new ArrayList<>();
//        for (MultipartFile file : files) {
//            String s = uploadImage(file);
//            list.add(s);
//        }
//        return list;
//    }
//
//    @Override
//    public String manipulateAttachments(String oldFilePaths, String newFilePaths, String containerName, String bucketName) throws SystemException {
//        List<String> finalPaths = new ArrayList<>();
//
//        if (StringUtils.isNotEmpty(newFilePaths)) {
//            String containerBucket = ParameterDictionary.FILE_SEPARATOR + containerName + ParameterDictionary.FILE_SEPARATOR + bucketName;
//            String[] paths = newFilePaths.split(",");
//            for (String eachPath : paths) {
//                if (!amazonS3.doesObjectExist(getBucketName(), eachPath.replace(getBaseDownloadPath(), ""))) {
//                    throw new SystemException(SystemError.FILE_NOT_FOUND, "file not found", 1209);
//                }
//                try {
//                    String tempPath = eachPath.replace("/temp", containerBucket);
//                    finalPaths.add(tempPath);
//                    if (!eachPath.equals(tempPath)) {
//                        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(getBucketName(), eachPath.replace(getBaseDownloadPath(), ""), getBucketName(), tempPath.replace(getBaseDownloadPath(), ""));
//                        copyObjectRequest.setCannedAccessControlList(CannedAccessControlList.PublicRead);
//                        amazonS3.copyObject(copyObjectRequest);
//                        amazonS3.deleteObject(getBucketName(), eachPath.replace(getBaseDownloadPath(), ""));
//                    }
//                } catch (SdkClientException e) {
//                    throw new SystemException(SystemError.FILE_NOT_FOUND, "can't upload file", 1221);
//
//                }
//            }
//        }
//        String newPaths = String.join(",", finalPaths);
//        if (StringUtils.isNotEmpty(oldFilePaths)) {
//            try {
//                String[] paths = oldFilePaths.split(",");
//                for (String eachPath : paths) {
//                    if (!newPaths.contains(eachPath)) {
//                        amazonS3.deleteObject(getBucketName(), eachPath.replace(getBaseDownloadPath(), ""));
//                    }
//                }
//            } catch (SdkClientException e) {
//                throw new SystemException(SystemError.FILE_NOT_FOUND, "can't delete file", 1222);
//            }
//        }
//        return newPaths;
//    }
//
//    @Override
//    public <T> void manipulateAttachments(T oldModel, T newModel) throws SystemException {
//        if (oldModel == null && newModel == null) {
//            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "model", 1208);
//        }
//
//        T model = oldModel != null ? oldModel : newModel;
//        for (Field field : model.getClass().getDeclaredFields()) {
//            Attachment attribute = field.getAnnotation(Attachment.class);
//            if (attribute != null) {
//                try {
//                    field.setAccessible(true);
//                    Object oldModelField = oldModel != null ? field.get(oldModel) : null;
//                    Object newModelField = newModel != null ? field.get(newModel) : null;
//                    if (oldModelField != null) {
//                        if (!oldModelField.equals(newModelField)) {
//                            String name = manipulateAttachments(oldModelField.toString(), newModelField != null ? newModelField.toString() : null, attribute.container(), attribute.bucket());
//                            if (newModel != null) {
//                                field.set(newModel, name);
//                            }
//                        }
//                    } else if (newModelField != null) {
//                        String name = manipulateAttachments(null, newModelField.toString(), attribute.container(), attribute.bucket());
//                        field.set(newModel, name);
//                    }
//
//                } catch (IllegalAccessException e) {
//                    throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "model", 1206);
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public DownloadResult download(String containerName, String bucketName, String fileName) throws SystemException {
//        return null;
//    }
//
//
//    public String getEndpoint() {
//        return applicationProperties.getFileCrud().getS3Config().getEndpoint();
////        return "https://s3.ir-thr-at1.arvanstorage.com";
//    }
//
//    public String getSecretKey() {
//        return applicationProperties.getFileCrud().getS3Config().getSecretKey();
////        return "690e9425a2875397aa31895f5e60201dcd960d314048f5e6e33bcaf57cece2b8";
//    }
//
//    public String getAccessKey() {
//        return applicationProperties.getFileCrud().getS3Config().getAccessKey();
////        return "a98bf2f0-6412-4879-b1a2-36df66108b44";
//    }
//
//    public String getBucketName() {
//        return applicationProperties.getFileCrud().getS3Config().getBucketName();
////        return "test-amadi77";
//    }
//
//    public String getBaseDownloadPath() {
//        return applicationProperties.getFileCrud().getS3Config().getBaseDownloadPath();
////        return "https://test-amadi77.s3.ir-thr-at1.arvanstorage.com/";
//    }
//
//}
