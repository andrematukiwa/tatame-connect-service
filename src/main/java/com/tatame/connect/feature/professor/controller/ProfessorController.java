package com.tatame.connect.feature.professor.controller;

import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.professor.model.domain.DiaSemanaEnum;
import com.tatame.connect.feature.professor.model.domain.EspecialidadeEnum;
import com.tatame.connect.feature.professor.model.domain.FaixaEnum;
import com.tatame.connect.feature.professor.model.domain.PeriodoEnum;
import com.tatame.connect.feature.professor.model.dto.ProfessorResponseDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorSearchFilter;
import com.tatame.connect.feature.professor.model.dto.ProfessorSignupRequestDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorUpdateRequestDto;
import com.tatame.connect.feature.professor.service.ProfessorService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody @Valid ProfessorSignupRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.signup(request));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/me")
    public ResponseEntity<ProfessorResponseDto> getMyProfile(@AuthenticationPrincipal UUID userId) {
        return ResponseEntity.ok(professorService.getMyProfile(userId));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("/me")
    public ResponseEntity<ProfessorResponseDto> updateMyProfile(
        @AuthenticationPrincipal UUID userId,
        @RequestBody @Valid ProfessorUpdateRequestDto request
    ) {
        return ResponseEntity.ok(professorService.updateMyProfile(userId, request));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping(value = "/me/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfessorResponseDto> updateMyPhoto(
        @AuthenticationPrincipal UUID userId,
        @RequestPart("foto") MultipartFile foto
    ) {
        return ResponseEntity.ok(professorService.updateMyPhoto(userId, foto));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping(value = "/me/certificado", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfessorResponseDto> updateMyCertificado(
        @AuthenticationPrincipal UUID userId,
        @RequestPart("certificado") MultipartFile certificado
    ) {
        return ResponseEntity.ok(professorService.updateMyCertificado(userId, certificado));
    }

    @PreAuthorize("hasRole('ACADEMIA')")
    @GetMapping
    public ResponseEntity<Page<ProfessorResponseDto>> search(
        @RequestParam(required = false) String cidade,
        @RequestParam(required = false) FaixaEnum faixa,
        @RequestParam(required = false) EspecialidadeEnum especialidade,
        @RequestParam(required = false) DiaSemanaEnum diaSemana,
        @RequestParam(required = false) PeriodoEnum periodo,
        @RequestParam(required = false) BigDecimal valorMin,
        @RequestParam(required = false) BigDecimal valorMax,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        var filter = new ProfessorSearchFilter(cidade, faixa, especialidade, diaSemana, periodo, valorMin, valorMax);
        return ResponseEntity.ok(professorService.search(filter, pageable));
    }

    @PreAuthorize("hasRole('ACADEMIA')")
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(professorService.getById(id));
    }
}
