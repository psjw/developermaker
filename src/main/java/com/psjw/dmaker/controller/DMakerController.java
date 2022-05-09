package com.psjw.dmaker.controller;


import com.psjw.dmaker.dto.CreateDeveloper;
import com.psjw.dmaker.dto.DeveloperDetailDto;
import com.psjw.dmaker.dto.DeveloperDto;
import com.psjw.dmaker.dto.EditDeveloper;
import com.psjw.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;


    @GetMapping("/developers")

    public List<DeveloperDto> getAllDevelopers() {
        // GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");

//        return Arrays.asList("snow", "elsa", "Olaf");
        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(@PathVariable String memberId){
        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDeveloper(@Valid @RequestBody CreateDeveloper.Request request) {
        // GET /developers HTTP/1.1
        log.info("request : {}", request);
        log.info("GET /create-developer HTTP/1.1");

//        return Collections.singletonList("Olaf");
        return dMakerService.createDeveloper(request);
    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable String memberId,
            @Valid @RequestBody EditDeveloper.Request request
        ){
        log.info("PUT /edit-developer HTTP/1.1");

        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(@PathVariable String memberId){
        return dMakerService.deleteDeveloper(memberId);
    }

//    @ResponseStatus(value = HttpStatus.CONFLICT)
//    @ExceptionHandler(DMakerException.class)
//    public DMakerErrorResponse hanlderException(DMakerException e, HttpServletRequest request){
//        log.error("errorCode: {}, url: {}, message: {}",e.getDMakerErrorCode(),request.getRequestURI(), e.getDetailMessage());
//        return DMakerErrorResponse.builder()
//                .errorCode(e.getDMakerErrorCode())
//                .errorMessage(e.getDetailMessage())
//                .build();
//    }
}
