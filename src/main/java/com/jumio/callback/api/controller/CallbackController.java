package com.jumio.callback.api.controller;
import com.jumio.callback.api.service.DocumentVerificationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class CallbackController {

    @Autowired
    @Qualifier("documentVerificationService")
    DocumentVerificationService documentVerificationService;

    @PostMapping(path = "/doc/verification/result", consumes =  MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiResponses(value = @ApiResponse(code = 204, message = "User Profile Verification Callback handler"))
    public void updateDocumentVerificationResult(@RequestBody MultiValueMap<String, String> docPayload) throws Exception {
        this.documentVerificationService.processDocument(docPayload);
    }
}