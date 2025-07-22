package com.books.utility.file.service;

import com.books.utility.file.statics.FileServiceStatus;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

    public FileServiceStatus createDirectory(Path path) throws SystemException {
        if (path.toFile().exists()) {
            return FileServiceStatus.ALREADY_EXISTS;
        } else {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);
                throw new SystemException(SystemError.PATH_CREATION_FAILED, stringWriter, 1105);
            }
            return FileServiceStatus.SUCCESS;
        }
    }

}
