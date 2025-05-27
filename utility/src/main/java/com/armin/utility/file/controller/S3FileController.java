package com.armin.utility.file.controller;//package com.armin.utility.file.controller;
//
//import com.armin.utility.file.bl.IFileService;
//import com.armin.utility.file.statics.constants.FileRestApi;
//import com.armin.utility.model.object.NoLogging;
//import com.armin.utility.model.object.SystemError;
//import com.armin.utility.model.object.SystemException;
//import com.armin.utility.statics.constants.RestApi;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotNull;
//import java.util.Collection;
//import java.util.List;
//
//import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
//
//@Api(tags = {"File"})
//@RestController
//@RequestMapping(value = RestApi.REST_PUBLIC)
//@Validated
//@NoLogging
//public class S3FileController {
//
//    private final IFileService fileService;
//
//    @Autowired
//    public S3FileController(@Qualifier("s3") IFileService fileService) {
//        this.fileService = fileService;
//    }
//
//    @ApiOperation(value = "Upload temp files", response = String.class, responseContainer = "List")
//    @PostMapping(path = FileRestApi.FILE_TEMP_S3, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Collection<String>> uploadFiles(@RequestBody List<MultipartFile> files, HttpServletRequest request) throws SystemException {
//        return new ResponseEntity<>(fileService.upload(files), HttpStatus.OK);
//    }
//
//    @ApiOperation(value = "Upload an image", response = String.class)
//    @PostMapping(path = FileRestApi.FILE_IMAGE_TEMP_S3, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> uploadImage(@Valid @NotNull @RequestBody List<MultipartFile> file, HttpServletRequest request) throws SystemException {
//        if (file.size() != 1) {
//            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "files size", 1245);
//        }
//        return new ResponseEntity<>(fileService.uploadImage(file.get(0)), HttpStatus.OK);
//    }
//
//    @ApiOperation(value = "Upload an image", response = String.class)
//    @GetMapping(path = FileRestApi.FILE_IMAGE_TEMP_S3, produces = MediaType.APPLICATION_JSON_VALUE)
//    public void test(HttpServletRequest request) throws SystemException {
//        fileService.getContainers();
//    }
//
//}
