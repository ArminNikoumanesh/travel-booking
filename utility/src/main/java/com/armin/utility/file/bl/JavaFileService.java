package com.armin.utility.file.bl;

import com.armin.utility.file.statics.enums.FileServiceStatus;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Exceptions error code range: 1111-1120
 */

@Service
public class JavaFileService {

    public File readFile(String path) throws SystemException {
        File file = new File(path);
        if (!file.exists()) {
            throw new SystemException(SystemError.FILE_NOT_FOUND, "path:" + path, 1101);
        }
        return file;
    }

    public byte[] readFile(Path path) throws SystemException {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new SystemException(SystemError.FILE_NOT_FOUND, "path:" + path, 1102);
        }
    }

    public FileServiceStatus createDirectory(Path path) {
        if (path.toFile().exists()) {
            return FileServiceStatus.FAILURE;
        } else {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                return FileServiceStatus.FAILURE;
            }
            return FileServiceStatus.SUCCESS;
        }
    }

}
