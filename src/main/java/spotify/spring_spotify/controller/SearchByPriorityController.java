package spotify.spring_spotify.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spotify.spring_spotify.dto.basic.PlaylistBasic;
import spotify.spring_spotify.dto.response.ApiResponse;
import spotify.spring_spotify.dto.response.PageResponse;
import spotify.spring_spotify.dto.response.SearchResponse;
import spotify.spring_spotify.service.SearchByPriorityService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/search-by-priority")
public class SearchByPriorityController {
    private final SearchByPriorityService searchByPriorityService;
    @GetMapping()
    public ApiResponse<PageResponse<SearchResponse>> searchByPriority(@RequestParam @NotBlank String name,
                                                                         @RequestParam int pageNo,
                                                                         @RequestParam int pageSize) {
        return ApiResponse.<PageResponse<SearchResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(searchByPriorityService.searchAll(name, pageNo, pageSize))
                .message("Fetch Playlist Save")
                .build();
    }
}
