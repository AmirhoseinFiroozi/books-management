package com.books.utility.file.service;

import com.books.utility.file.dto.BucketInfo;
import com.books.utility.file.dto.ContainerInfo;
import com.books.utility.file.dto.DownloadResult;
import com.books.utility.file.dto.FileInfo;
import com.books.utility.file.statics.FileServiceStatus;
import com.books.utility.system.exception.SystemException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface IFileService {
    Collection<ContainerInfo> getContainers() throws SystemException;

    ContainerInfo getContainer(String containerName) throws SystemException;

    BucketInfo getBucket(String containerName, String bucketName) throws SystemException;

    Collection<FileInfo> getFiles(String containerName, String bucketName) throws SystemException;

    FileServiceStatus createContainer(String containerName) throws SystemException;

    FileServiceStatus createBucket(String containerName, String bucketName) throws SystemException;

    FileServiceStatus deleteContainer(String containerName);

    FileServiceStatus deleteBucket(String containerName, String bucketName);

    FileServiceStatus deleteFiles(String filePaths) throws SystemException;

    FileServiceStatus deleteFilesByBasePath(String filePaths) throws SystemException;

    FileServiceStatus deleteTempFiles(String filePaths);

    FileServiceStatus deleteFiles(Collection<String> filePaths) throws SystemException;

    String uploadImage(MultipartFile file) throws SystemException;

    void checkExtension(String extension) throws SystemException;

    Collection<String> upload(Collection<MultipartFile> files) throws SystemException;

    String manipulateAttachments(String oldFilePaths, String newFilePaths, String containerName, String bucketName, String customParentPath, int maximumCount) throws SystemException;

    <T> void manipulateAttachments(T oldModel, T newModel) throws SystemException;

    <T> void manipulateAttachments(T oldModel, T newModel, String customParentPath) throws SystemException;

    <T, U> void transfer(T sourceModel, U targetModel, String sourceField, String targetField) throws SystemException;

    <T, U> void transfer(T sourceModel, U targetModel, String sourceField, String targetField, String targetCustomParentPath) throws SystemException;

    DownloadResult download(String containerName, String bucketName, String fileName) throws SystemException;
}
