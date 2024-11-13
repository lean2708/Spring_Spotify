package spotify.spring_spotify.controller;

import spotify.spring_spotify.annotation.ApiMessage;
import spotify.spring_spotify.dto.response.UploadFileResponse;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class FileController {

    private final FileService fileService;

    @Value("${anb52.upload-file.base-uri}")
    private String baseURI;


    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<UploadFileResponse> upload(@RequestParam(name = "file", required = false) MultipartFile file,
                                                     @RequestParam("folder") String folder) throws URISyntaxException, IOException, SpotifyException {
        // skip validate
        if(file == null || file.isEmpty()){
            throw new SpotifyException("File đang trống. Vui lòng tải File lên");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx","mp4", "avi", "mov");
        boolean isValid = false;
        for (String extension : allowedExtensions) {
            // Kiểm tra xem tên file có kết thúc bằng phần mở rộng hợp lệ không
            if (fileName.toLowerCase().endsWith(extension)) {
                isValid = true;
                break;
            }
        }
        if(!isValid){
            throw new SpotifyException("Phần mở rộng của file không hợp lệ. Chỉ cho phép  " + allowedExtensions.toString());
        }

        //create a directory if not exist
        this.fileService.createDirectory(baseURI + folder);

        // store file
        String uploadFile = this.fileService.store(file,folder);
        UploadFileResponse res = new UploadFileResponse(uploadFile, Instant.now());

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/files")
    @ApiMessage("Download a file")
    public ResponseEntity<Resource> download(
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "folder", required = false) String folder)
            throws SpotifyException, URISyntaxException, FileNotFoundException {
        if (fileName == null || folder == null) {
            throw new SpotifyException("Thiếu tham số bắt buộc: (File hoặc folder) trong tham số truy vấn.");
        }

        // check file exist (and not a directory)
        long fileLength = this.fileService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw new SpotifyException("Không tìm thấy file có tên = " + fileName );
        }

        // download a file
        InputStreamResource resource = this.fileService.getResource(fileName, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
