package com.books.utility.file.service;

import com.books.utility.file.dto.BucketInfo;
import com.books.utility.file.dto.ContainerInfo;
import com.books.utility.file.dto.DownloadResult;
import com.books.utility.file.statics.FileServiceStatus;
import com.books.utility.commons.annotations.Attachment;
import com.books.utility.commons.statics.constants.ParameterDictionary;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.file.dto.FileInfo;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
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
import java.util.stream.Collectors;

/**
 * Exceptions error code range: 1201-1250
 */
@Slf4j
@Primary
@Service
public class FileSystemFileService implements IFileService {

    private final JavaFileService javaFileService;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public FileSystemFileService(JavaFileService javaFileService, ApplicationProperties applicationProperties) {
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
    public FileServiceStatus createContainer(String containerName) throws SystemException {
        Path containerPath = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + containerName);
        return javaFileService.createDirectory(containerPath);
    }

    public FileServiceStatus createContainer(String containerName, String customParentPath) throws SystemException {
        Path containerPath = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + customParentPath + ParameterDictionary.FILE_SEPARATOR + containerName);
        return javaFileService.createDirectory(containerPath);
    }

    @Override
    public FileServiceStatus createBucket(String containerName, String bucketName) throws SystemException {
        Path bucketPath = Paths.get(applicationProperties.getFileCrud().getBaseFilePath() + containerName
                + ParameterDictionary.FILE_SEPARATOR + bucketName);
        return javaFileService.createDirectory(bucketPath);
    }

    public FileServiceStatus createBucket(String containerName, String bucketName, String customParentPath) throws SystemException {
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
        for (MultipartFile multipartFile : files) {
            if (multipartFile == null || applicationProperties.getFileCrud().getTempFilePath() == null)
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, null, 1202);

            if (multipartFile.isEmpty())
                throw new SystemException(SystemError.UPLOADED_FILE_CORRUPTED, "file", 1203);

            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            if (StringUtils.isBlank(extension)) {
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "invalid file extension", 1224);
            }
            if (applicationProperties.getFileCrud().getAllowedExtensions() != null &&
                    !applicationProperties.getFileCrud().getAllowedExtensions().stream().map(String::toLowerCase)
                            .collect(Collectors.toSet()).contains(extension.toLowerCase())) {
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "file extension not supported", 1223);
            }
        }
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
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
    public String manipulateAttachments(String oldFilePath, String newFilePath, String containerName, String bucketName, String customParentPath, int maximumCount) throws SystemException {
        if (customParentPath != null) {
            if (!Files.exists(Paths.get(applicationProperties.getFileCrud().getBaseFilePath() +
                    customParentPath + ParameterDictionary.FILE_SEPARATOR + containerName + ParameterDictionary.FILE_SEPARATOR + bucketName))) {
                createContainer(containerName, customParentPath);
                createBucket(containerName, bucketName, customParentPath);
            }
            return manipulate(oldFilePath, newFilePath, customParentPath + ParameterDictionary.FILE_SEPARATOR + containerName, bucketName, maximumCount);
        } else {
            return manipulate(oldFilePath, newFilePath, containerName, bucketName, maximumCount);
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

    /**
     * <p>
     * This method compares the oldFilePath and newFilePath and based on the values in them take below actions:
     * </p>
     * <ul>
     *     <li>
     *         <b>If both oldFilePath and newFilePath are null:</b>Nothing to be done
     *     </li>
     *     <li>
     *         <b>If oldFilePath is null:</b> All the images in the newFilePath are considered new
     *         (<i>new images are always considered to be in the temp directory</i>) and are moved from
     *         temp directory to the specified "baseFilePath/container/bucket" location
     *     </li>
     *     <li>
     *         <b>If newFilePath is null:</b> All the oldImages (<i>located in "baseFilePath/container/bucket" directory</i>) are deleted
     *     </li>
     *     <li>
     *         <b>If both parameters are provided:</b>Compare the oldFilePath images with the newFilePath images
     *         and detect images in three categories: totallyNewPics (are moved from temp directory to "baseFilePath/container/bucket"),
     *         sharedPics (the images that exists in both, there is nothing to do with them), and mustDeletePaths
     *         (the images that exist in oldFilePath but not in the newFilePath, these ones should be deleted from "baseFilePath/container/bucket")
     *      </li>
     * </ul>
     *
     * @param oldFilePath   Path to old images separated by ","
     * @param newFilePath   Path to new images separated by ","
     * @param containerName
     * @param bucketName
     * @param maximumCount  MaximumCount of allowed images
     * @return String containing the path to images in the order that they were inserted; images are separated by ","
     * @throws SystemException
     */
    public String manipulate(String oldFilePath, String newFilePath, String containerName, String bucketName, int maximumCount) throws SystemException {
        List<String> pics = new ArrayList<>();
        List<String> newPics = new ArrayList<>();
        List<String> mustExistPaths = new ArrayList<>();
        List<String> mustDeletePaths = new ArrayList<>();
        if (newFilePath != null) {
            newPics = Arrays.stream(newFilePath.split(",")).map(String::trim).toList();
        }
        if (newPics.size() > 0 && maximumCount > -1 && newPics.size() > maximumCount) {
            throw new SystemException(SystemError.COUNT_OF_FILE_EXCEEDED, "count of file is more than expected", 1205);
        }
        if (oldFilePath != null) {
            List<String> totallyNewPics = new ArrayList<>(newPics);
            List<String> sharedPics = new ArrayList<>();
            pics = Arrays.stream(oldFilePath.split(",")).map(String::trim).toList();
            boolean mustDelete;
            for (String eachExistPic : pics) {
                mustDelete = true;
                for (String eachNewPic : newPics) {
                    if (eachNewPic.equals(eachExistPic)) {
                        sharedPics.add(eachNewPic);
                        totallyNewPics.remove(eachNewPic);
                        mustDelete = false;
                        break;
                    }
                }
                if (mustDelete) {
                    String existingFileName = eachExistPic.substring(eachExistPic.lastIndexOf(ParameterDictionary.FILE_SEPARATOR) + 1);
                    mustDeletePaths.add(createFilePath(containerName, bucketName, existingFileName));
                }
            }
            sharedPics.forEach(file -> mustExistPaths.add(createFilePath(containerName, bucketName,
                    file.substring(file.lastIndexOf(ParameterDictionary.FILE_SEPARATOR) + 1))));
            totallyNewPics.forEach(file -> mustExistPaths.add(applicationProperties.getFileCrud().getTempFilePath() +
                    file.substring(file.lastIndexOf(ParameterDictionary.FILE_SEPARATOR) + 1)));
        } else if (newFilePath != null && newFilePath.length() > 1) {
            newPics.forEach(file -> mustExistPaths.add(applicationProperties.getFileCrud().getTempFilePath() +
                    file.substring(file.lastIndexOf(ParameterDictionary.FILE_SEPARATOR) + 1)));
        }
        for (String path : mustExistPaths) {
            if (!Files.exists(Paths.get(path))) {
                throw new SystemException(SystemError.FILE_NOT_FOUND, "path not found: " + path, 1209);
            }
        }
        for (String path : mustDeletePaths) {
            deleteFiles(path);
        }
        if (newFilePath != null && newFilePath.length() > 1) {
            List<String> fileNames = new ArrayList<>();
            boolean mustAdd;
            for (String eachNewPic : newPics) {
                mustAdd = true;
                if (!pics.isEmpty() && pics.contains(eachNewPic)) {
                    fileNames.add(eachNewPic);
                    mustAdd = false;
                }
                if (mustAdd && eachNewPic.length() > 1) {
                    String extension = FilenameUtils.getExtension(eachNewPic);
                    String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
                    String existingFileName = eachNewPic.substring(eachNewPic.lastIndexOf(ParameterDictionary.FILE_SEPARATOR) + 1);
                    try {
                        Files.move(Paths.get(applicationProperties.getFileCrud().getTempFilePath() + existingFileName), Paths.get(createFilePath(containerName, bucketName, fileName)));
                    } catch (IOException ignored) {
                        log.error("[FILESYSTEM-ERROR] Couldn't Move File " +
                                applicationProperties.getFileCrud().getTempFilePath() + existingFileName +
                                " To " + createFilePath(containerName, bucketName, fileName));
                    }
                    fileNames.add(containerName + ParameterDictionary.FILE_SEPARATOR + bucketName + ParameterDictionary.FILE_SEPARATOR + fileName);
                }
            }
            return !fileNames.isEmpty() ? String.join(",", fileNames) : null;
        } else {
            return null;
        }
    }

    /**
     * <p>
     * This class is used to resolve the issue of storing images
     * related to fields annotated with <code>@Attachment</code> in entity classes.
     * It works by getting two objects of the same type,
     * with different image paths and inserting the new images
     * (<i>new images are always considered to be in the temp directory</i>) in the specified container and bucket.
     * </p>
     * <p>
     * Delete the images that aren't needed (present in oldModel but not newModel).
     * </p>
     * <p>
     * Preserving the images which hasn't changed (present in both oldModel and newModel)
     * </p>
     *
     * @param oldModel Class that contains the old images
     * @param newModel Class that contains the new images
     * @param <T>      The type of the class(oldModel and newModel are of the same type)
     * @throws SystemException
     */
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
                            String name = manipulateAttachments(oldModelField.toString(), newModelField != null ? newModelField.toString() : null, attribute.container(), attribute.bucket(), null, attribute.maximumCount());
                            if (newModel != null) {
                                field.set(newModel, name);
                            }
                        }
                    } else if (newModelField != null) {
                        String name = manipulateAttachments(null, newModelField.toString(), attribute.container(), attribute.bucket(), null, attribute.maximumCount());
                        field.set(newModel, name);
                    }

                } catch (IllegalAccessException e) {
                    throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "model", 1206);
                }
            }
        }
    }

    /**
     * <p>This class is used to resolve the issue of storing images
     * related to fields annotated with <code>@Attachment</code> in entity classes.
     * It works by getting two objects of the same type,
     * with different image paths and inserting the new images
     * (<i>new images are always considered to be in the temp directory</i>) in the specified container and bucket.
     * </p>
     * <p>
     * Delete the images that aren't needed (present in oldModel but not newModel).
     * </p>
     * <p>
     * Preserving the images which hasn't changed (present in both oldModel and newModel)
     * </p>
     *
     * @param oldModel         Class that contains the old images
     * @param newModel         Class that contains the new images
     * @param customParentPath The path at which container and bucket are located in
     * @param <T>              The type of the class(oldModel and newModel are of the same type)
     * @throws SystemException
     */
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
                            String name = manipulateAttachments(oldModelField.toString(), newModelField != null ? newModelField.toString() : null, attribute.container(), attribute.bucket(), customParentPath, attribute.maximumCount());
                            if (newModel != null) {
                                field.set(newModel, name);
                            }
                        }
                    } else if (newModelField != null) {
                        String name = manipulateAttachments(null, newModelField.toString(), attribute.container(), attribute.bucket(), customParentPath, attribute.maximumCount());
                        field.set(newModel, name);
                    }

                } catch (IllegalAccessException e) {
                    throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "model", 1206);
                }
            }
        }
    }

    @Override
    public <T, U> void transfer(T sourceModel, U targetModel, String sourceField, String targetField) throws SystemException {
        transferFromSourceToTarget(Hibernate.unproxy(sourceModel), Hibernate.unproxy(targetModel), sourceField, targetField, "");
    }

    @Override
    public <T, U> void transfer(T sourceModel, U targetModel, String sourceField, String targetField, String targetCustomParentPath) throws SystemException {
        transferFromSourceToTarget(Hibernate.unproxy(sourceModel), Hibernate.unproxy(targetModel), sourceField, targetField, targetCustomParentPath + ParameterDictionary.FILE_SEPARATOR);
    }

    private <T, U> void transferFromSourceToTarget(T sourceModel, U targetModel, String sourceField, String targetField, String targetCustomParentPath) throws SystemException {
        if (sourceModel == null || targetModel == null) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "model", 1211);
        }
        try {
            Field source = sourceModel.getClass().getDeclaredField(sourceField);
            Field target = targetModel.getClass().getDeclaredField(targetField);
            Attachment sourceAnnotation = source.getAnnotation(Attachment.class);
            Attachment targetAnnotation = target.getAnnotation(Attachment.class);
            if (sourceAnnotation == null || targetAnnotation == null || sourceAnnotation.container() == null ||
                    targetAnnotation.container() == null || sourceAnnotation.bucket() == null || targetAnnotation.bucket() == null) {
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "fields doesn't contain attachment or necessary attributes", 1214);
            }
            source.setAccessible(true);
            target.setAccessible(true);
            Object sourcePath = source.get(sourceModel);
            if (sourcePath != null) {
                List<String> mustExistPaths = new ArrayList<>();
                List<String> mustDeletePaths = new ArrayList<>();
                List<String> targetPaths = new ArrayList<>();
                List<String> sourceImages = Arrays.stream(sourcePath.toString().split(",")).map(String::trim).toList();
                sourceImages.forEach(file -> {
                    String substring = file.substring(file.lastIndexOf(ParameterDictionary.FILE_SEPARATOR) + 1);
                    mustExistPaths.add(applicationProperties.getFileCrud().getBaseFilePath() + file);
                    targetPaths.add(createFilePath(targetCustomParentPath +
                                    targetAnnotation.container(), targetAnnotation.bucket(),
                            substring));
                });
                for (String path : mustExistPaths) {
                    if (!Files.exists(Paths.get(path))) {
                        throw new SystemException(SystemError.FILE_NOT_FOUND, "source path not found: " + path, 1215);
                    }
                }
                Object oldPath = target.get(targetModel);
                if (oldPath != null) {
                    List<String> targetImages = Arrays.stream(oldPath.toString().split(",")).map(String::trim).toList();
                    targetImages.forEach(file -> mustDeletePaths.add(applicationProperties.getFileCrud().getBaseFilePath() + file));
                }
                for (String path : mustDeletePaths) {
                    if (Files.exists(Paths.get(path))) {
                        deleteFiles(path);
                    }
                }
                createContainer(targetCustomParentPath + targetAnnotation.container());
                createBucket(targetCustomParentPath + targetAnnotation.container(), targetAnnotation.bucket());
                List<String> fileNames = new ArrayList<>();
                for (int i = 0; i < mustExistPaths.size(); i++) {
                    Files.copy(Paths.get(mustExistPaths.get(i)), Paths.get(targetPaths.get(i)), StandardCopyOption.REPLACE_EXISTING);
                    fileNames.add(targetCustomParentPath + targetAnnotation.container() + ParameterDictionary.FILE_SEPARATOR +
                            targetAnnotation.bucket() + ParameterDictionary.FILE_SEPARATOR +
                            mustExistPaths.get(i).substring(mustExistPaths.get(i).lastIndexOf(ParameterDictionary.FILE_SEPARATOR) + 1));
                }
                target.set(targetModel, !fileNames.isEmpty() ? String.join(",", fileNames) : null);
            }
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, stringWriter, 1213);
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
