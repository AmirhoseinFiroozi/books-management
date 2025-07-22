package com.books.utility.file.controller;

import com.books.utility.file.service.IFileService;
import com.books.utility.file.statics.constants.FileRestApi;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

//@Api(tags = {"File"})
@RestController
@RequestMapping(value = {"${rest.public}"})
@Validated
public class FileController {

    private final IFileService fileService;

    @Autowired
    public FileController(IFileService fileService) {
        this.fileService = fileService;
    }

    //    @ApiOperation(value = "Upload temp files", response = String.class, responseContainer = "List")
    @PostMapping(path = FileRestApi.FILE_TEMP, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Collection<String>> uploadFiles(@RequestBody List<MultipartFile> files, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(fileService.upload(files), HttpStatus.OK);
    }

    //    @ApiOperation(value = "Upload an image", response = String.class)
    @PostMapping(path = FileRestApi.FILE_IMAGE_TEMP, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@Valid @NotNull @RequestBody List<MultipartFile> files, HttpServletRequest request) throws SystemException {
        if (files.size() != 1) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "files size", 1245);
        }
        return new ResponseEntity<>(fileService.uploadImage(files.getFirst()), HttpStatus.OK);
    }

}
