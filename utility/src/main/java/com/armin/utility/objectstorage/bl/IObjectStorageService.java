//package com.armin.utility.objectstorage.bl;
//
//import com.armin.utility.object.SystemException;
//import io.minio.ObjectWriteResponse;
//import io.minio.Result;
//import io.minio.messages.DeleteError;
//import io.minio.messages.Item;
//
//import java.io.InputStream;
//
//public interface IObjectStorageService {
//    ObjectWriteResponse copyObject(String sourceObject, String targetObject) throws SystemException;
//
//    void downloadObject(String object, String filePath) throws SystemException;
//
//    InputStream getObject(String object) throws SystemException;
//
//    Iterable<Result<Item>> listObjects(String preFix, boolean recursive, Integer maxKey);
//
//    ObjectWriteResponse putObject(String object, InputStream inputStream, long objectSize, long partSize) throws SystemException;
//
//    void removeObject(String object) throws SystemException;
//
//    Iterable<Result<DeleteError>> removeObjects(String... objects);
//
//    ObjectWriteResponse uploadObject(String object, String filePath) throws SystemException;
//}
