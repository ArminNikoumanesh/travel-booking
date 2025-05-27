package com.armin.utility.file.controller;

import com.armin.utility.file.bl.IFileService;
import com.armin.utility.file.statics.constants.FileRestApi;
import com.armin.utility.object.NoLogging;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "File Controller")
@Validated
@NoLogging
@RestController
@RequestMapping(value = {"${rest.public}"})
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
        return new ResponseEntity<>(fileService.uploadImage(files.get(0)), HttpStatus.OK);
    }

}
