//package com.armin.utility.objectstorage.bl;
//
//import com.armin.utility.config.cloud.BaseApplicationProperties;
//import com.armin.utility.object.SystemError;
//import com.armin.utility.object.SystemException;
//import io.minio.*;
//import io.minio.errors.*;
//import io.minio.messages.DeleteError;
//import io.minio.messages.DeleteObject;
//import io.minio.messages.Item;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import jakarta.annotation.Nullable;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ObjectStorageService implements IObjectStorageService {
//    private final MinioClient minioClient;
//    private final BaseApplicationProperties applicationProperties;
//
//    @Autowired
//    public ObjectStorageService(MinioClient minioClient, BaseApplicationProperties applicationProperties) {
//        this.minioClient = minioClient;
//        this.applicationProperties = applicationProperties;
//    }
//
//    @Override
//    public ObjectWriteResponse copyObject(String sourceObject, String targetObject) throws SystemException {
//        try {
//            return minioClient.copyObject(
//                    CopyObjectArgs.builder()
//                            .bucket(applicationProperties.getMinioConfig().getBucket())
//                            .object(targetObject)
//                            .source(CopySource.builder()
//                                    .bucket(applicationProperties.getMinioConfig().getBucket())
//                                    .object(sourceObject)
//                                    .build())
//                            .build());
//        } catch (ErrorResponseException | IOException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed To Copy The Object", 6001);
//        } catch (InsufficientDataException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed In Reading the InputStream", 6002);
//        } catch (InternalException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6003);
//        } catch (InvalidKeyException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "SomeThing Went Wrong With Cryptographic Key", 6004);
//        } catch (NoSuchAlgorithmException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Cryptographic Algorithm Failed", 6005);
//        } catch (InvalidResponseException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Non-Xml Response From Server", 6006);
//        } catch (XmlParserException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Parsing Xml Response Failed", 6007);
//        } catch (ServerException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6008);
//        }
//    }
//
//    @Override
//    public void downloadObject(String object, String filePath) throws SystemException {
//        try {
//            minioClient.downloadObject(
//                    DownloadObjectArgs.builder()
//                            .bucket(applicationProperties.getMinioConfig().getBucket())
//                            .object(object)
//                            .filename(filePath)
//                            .build());
//        } catch (ErrorResponseException | IOException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed To Download The Object", 6009);
//        } catch (InsufficientDataException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed In Reading the InputStream", 6010);
//        } catch (InternalException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6011);
//        } catch (InvalidKeyException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "SomeThing Went Wrong With Cryptographic Key", 6012);
//        } catch (NoSuchAlgorithmException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Cryptographic Algorithm Failed", 6013);
//        } catch (InvalidResponseException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Non-Xml Response From Server", 6014);
//        } catch (XmlParserException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Parsing Xml Response Failed", 6015);
//        } catch (ServerException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6016);
//        }
//    }
//
//    @Override
//    public InputStream getObject(String object) throws SystemException {
//        try {
//            return minioClient.getObject(
//                    GetObjectArgs.builder()
//                            .bucket(applicationProperties.getMinioConfig().getBucket())
//                            .object(object)
//                            .build());
//        } catch (ErrorResponseException | IOException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed To Get The Object", 6017);
//        } catch (InsufficientDataException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed In Reading the InputStream", 6018);
//        } catch (InternalException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6019);
//        } catch (InvalidKeyException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "SomeThing Went Wrong With Cryptographic Key", 6020);
//        } catch (NoSuchAlgorithmException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Cryptographic Algorithm Failed", 6021);
//        } catch (InvalidResponseException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Non-Xml Response From Server", 6022);
//        } catch (XmlParserException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Parsing Xml Response Failed", 6023);
//        } catch (ServerException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6024);
//        }
//    }
//
//    @Override
//    public Iterable<Result<Item>> listObjects(@Nullable String preFix, boolean recursive, @Nullable Integer maxKey) {
//        if (preFix != null) {
//            if (maxKey != null) {
//                return minioClient.listObjects(
//                        ListObjectsArgs.builder()
//                                .bucket(applicationProperties.getMinioConfig().getBucket())
//                                .prefix(preFix)
//                                .recursive(recursive)
//                                .maxKeys(maxKey)
//                                .build());
//            } else {
//                return minioClient.listObjects(
//                        ListObjectsArgs.builder()
//                                .bucket(applicationProperties.getMinioConfig().getBucket())
//                                .prefix(preFix)
//                                .recursive(recursive)
//                                .build());
//            }
//        } else {
//            if (maxKey != null) {
//                return minioClient.listObjects(
//                        ListObjectsArgs.builder()
//                                .bucket(applicationProperties.getMinioConfig().getBucket())
//                                .recursive(recursive)
//                                .maxKeys(maxKey)
//                                .build());
//            } else {
//                return minioClient.listObjects(
//                        ListObjectsArgs.builder()
//                                .bucket(applicationProperties.getMinioConfig().getBucket())
//                                .recursive(recursive)
//                                .build());
//            }
//        }
//    }
//
//    @Override
//    public ObjectWriteResponse putObject(String object, InputStream inputStream, long objectSize, long partSize) throws SystemException {
//
//        try {
//            return minioClient.putObject(
//                    PutObjectArgs.builder()
//                            .bucket(applicationProperties.getMinioConfig().getBucket())
//                            .object(object)
//                            .stream(inputStream, objectSize, partSize)
//                            .build());
//        } catch (ErrorResponseException | IOException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed To Put The Object", 6025);
//        } catch (InsufficientDataException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed In Reading the InputStream", 6026);
//        } catch (InternalException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6027);
//        } catch (InvalidKeyException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "SomeThing Went Wrong With Cryptographic Key", 6028);
//        } catch (NoSuchAlgorithmException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Cryptographic Algorithm Failed", 6029);
//        } catch (InvalidResponseException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Non-Xml Response From Server", 6030);
//        } catch (XmlParserException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Parsing Xml Response Failed", 6031);
//        } catch (ServerException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6032);
//        }
//    }
//
//    @Override
//    public void removeObject(String object) throws SystemException {
//        try {
//            minioClient.removeObject(
//                    RemoveObjectArgs.builder()
//                            .bucket(applicationProperties.getMinioConfig().getBucket())
//                            .object(object)
//                            .build());
//        } catch (ErrorResponseException | IOException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed To Remove The Object", 6033);
//        } catch (InsufficientDataException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed In Reading the InputStream", 6034);
//        } catch (InternalException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6035);
//        } catch (InvalidKeyException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "SomeThing Went Wrong With Cryptographic Key", 6036);
//        } catch (NoSuchAlgorithmException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Cryptographic Algorithm Failed", 6037);
//        } catch (InvalidResponseException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Non-Xml Response From Server", 6038);
//        } catch (XmlParserException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Parsing Xml Response Failed", 6039);
//        } catch (ServerException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6040);
//        }
//    }
//
//    @Override
//    public Iterable<Result<DeleteError>> removeObjects(String... objects) {
//        List<DeleteObject> deleteObjects = new ArrayList<>();
//        for (String object : objects) {
//            deleteObjects.add(new DeleteObject(object));
//        }
//        return minioClient.removeObjects(
//                RemoveObjectsArgs.builder()
//                        .bucket(applicationProperties.getMinioConfig().getBucket())
//                        .objects(deleteObjects)
//                        .build());
//    }
//
//    @Override
//    public ObjectWriteResponse uploadObject(String object, String filePath) throws SystemException {
//        try {
//            return minioClient.uploadObject(
//                    UploadObjectArgs.builder()
//                            .bucket(applicationProperties.getMinioConfig().getBucket())
//                            .object(object)
//                            .filename(filePath)
//                            .build());
//        } catch (ErrorResponseException | IOException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed To Upload The Object", 6041);
//        } catch (InsufficientDataException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Failed In Reading the InputStream", 6042);
//        } catch (InternalException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6043);
//        } catch (InvalidKeyException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "SomeThing Went Wrong With Cryptographic Key", 6044);
//        } catch (NoSuchAlgorithmException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Cryptographic Algorithm Failed", 6045);
//        } catch (InvalidResponseException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Non-Xml Response From Server", 6046);
//        } catch (XmlParserException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, "Parsing Xml Response Failed", 6047);
//        } catch (ServerException e) {
//            throw new SystemException(SystemError.SERVER_ERROR, e.getMessage(), 6048);
//        }
//    }
//}
