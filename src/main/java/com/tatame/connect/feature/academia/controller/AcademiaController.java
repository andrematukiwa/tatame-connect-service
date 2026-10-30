package com.tatame.connect.feature.academia.controller;

import com.tatame.connect.feature.academia.model.dto.AcademiaResponseDto;
import com.tatame.connect.feature.academia.model.dto.AcademiaSignupRequestDto;
import com.tatame.connect.feature.academia.model.dto.AcademiaUpdateRequestDto;
import com.tatame.connect.feature.academia.service.AcademiaService;
import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/academias")
public class AcademiaController {

    private final AcademiaService academiaService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody @Valid AcademiaSignupRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academiaService.signup(request));
    }

    @PreAuthorize("hasRole('ACADEMIA')")
    @GetMapping("/me")
    public ResponseEntity<AcademiaResponseDto> getMyProfile(@AuthenticationPrincipal UUID userId) {
        return ResponseEntity.ok(academiaService.getMyProfile(userId));
    }

    @PreAuthorize("hasRole('ACADEMIA')")
    @PutMapping("/me")
    public ResponseEntity<AcademiaResponseDto> updateMyProfile(
        @AuthenticationPrincipal UUID userId,
        @RequestBody @Valid AcademiaUpdateRequestDto request
    ) {
        return ResponseEntity.ok(academiaService.updateMyProfile(userId, request));
    }

    @PreAuthorize("hasRole('ACADEMIA')")
    @PostMapping(value = "/me/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AcademiaResponseDto> updateMyPhoto(
        @AuthenticationPrincipal UUID userId,
        @RequestPart("foto") MultipartFile foto
    ) {
        return ResponseEntity.ok(academiaService.updateMyPhoto(userId, foto));
    }
}
