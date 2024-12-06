package spotify.spring_spotify.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spotify.spring_spotify.dto.response.ApiResponse;
import spotify.spring_spotify.dto.response.FileResponse;
import spotify.spring_spotify.exception.FileException;
import spotify.spring_spotify.service.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/v1/file")
@Validated
public class FileController {

    private final FileService fileService;


    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("fileName") @NotBlank @NotNull String fileName) throws IOException, FileException {
        Object response = fileService.downloadFile(fileName);
        if (response != null){
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(response);
        } else {
            ApiResponse apiResponse = ApiResponse.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Không thể tải file xuống")
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("fileName") @NotBlank @NotNull String fileName){
        boolean isDeleted = fileService.delete(fileName);
        if (isDeleted){
            ApiResponse apiResponse = ApiResponse.builder()
                    .code(HttpStatus.OK.value())
                    .message("Xóa thành công file")
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            ApiResponse apiResponse = ApiResponse.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("File không tồn tại")
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }


}
