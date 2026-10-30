package com.tatame.connect.feature.professor.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tatame.connect.exception.base.BadRequestException;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.auth.repository.entity.UserEntity;
import com.tatame.connect.feature.auth.service.AuthService;
import com.tatame.connect.feature.professor.exception.ProfessorNotFoundException;
import com.tatame.connect.feature.professor.model.domain.DiaSemanaEnum;
import com.tatame.connect.feature.professor.model.domain.EspecialidadeEnum;
import com.tatame.connect.feature.professor.model.domain.FaixaEnum;
import com.tatame.connect.feature.professor.model.domain.PeriodoEnum;
import com.tatame.connect.feature.professor.model.domain.TipoCobrancaEnum;
import com.tatame.connect.feature.professor.model.dto.ProfessorResponseDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorSearchFilter;
import com.tatame.connect.feature.professor.model.dto.ProfessorSignupRequestDto;
import com.tatame.connect.feature.professor.model.dto.ProfessorUpdateRequestDto;
import com.tatame.connect.feature.professor.repository.ProfessorProfileRepository;
import com.tatame.connect.feature.professor.repository.entity.ProfessorProfileEntity;
import com.tatame.connect.feature.storage.StorageService;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceImplTest {

    @Mock
    private ProfessorProfileRepository professorProfileRepository;

    @Mock
    private AuthService authService;

    @Mock
    private StorageService storageService;

    private ProfessorServiceImpl professorService;

    @BeforeEach
    void setUp() {
        professorService = new ProfessorServiceImpl(professorProfileRepository, authService, storageService);
    }

    private ProfessorSignupRequestDto buildSignupRequest() {
        ProfessorSignupRequestDto dto = new ProfessorSignupRequestDto();
        dto.setEmail("joao@email.com");
        dto.setSenha("senha12345");
        dto.setNomeCompleto("Joao Gabriel");
        dto.setTelefoneWhatsapp("(11) 99999-9999");
        dto.setCidade("Sao Paulo");
        dto.setBairro("Vila Mariana");
        dto.setFaixa(FaixaEnum.PRETA);
        dto.setTempoExperienciaAnos(8);
        dto.setEspecialidades(Set.of(EspecialidadeEnum.ADULTO));
        dto.setDiasDisponiveis(Set.of(DiaSemanaEnum.SEGUNDA));
        dto.setPeriodosDisponiveis(Set.of(PeriodoEnum.NOITE));
        dto.setTipoCobranca(TipoCobrancaEnum.MENSAL);
        dto.setValorPretendido(new BigDecimal("3000.00"));
        dto.setAceiteTermos(true);
        return dto;
    }

    @Test
    void signup_criaUsuarioEPerfilAssociado() {
        UUID userId = UUID.randomUUID();
        UserEntity user = UserEntity.builder().id(userId).email("joao@email.com").role(RoleEnum.PROFESSOR).build();
        AuthResponseDto expectedAuthResponse = AuthResponseDto.builder().token("jwt").userId(userId).build();

        when(authService.registerUser("joao@email.com", "senha12345", RoleEnum.PROFESSOR)).thenReturn(user);
        when(authService.buildAuthResponse(user)).thenReturn(expectedAuthResponse);

        AuthResponseDto result = professorService.signup(buildSignupRequest());

        ArgumentCaptor<ProfessorProfileEntity> captor = ArgumentCaptor.forClass(ProfessorProfileEntity.class);
        verify(professorProfileRepository).save(captor.capture());

        assertThat(captor.getValue().getUserId()).isEqualTo(userId);
        assertThat(captor.getValue().getNomeCompleto()).isEqualTo("Joao Gabriel");
        assertThat(result).isSameAs(expectedAuthResponse);
    }

    @Test
    void getMyProfile_perfilExiste_retornaDto() {
        UUID userId = UUID.randomUUID();
        ProfessorProfileEntity entity = ProfessorProfileEntity.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .nomeCompleto("Joao Gabriel")
            .faixa(FaixaEnum.PRETA)
            .tipoCobranca(TipoCobrancaEnum.MENSAL)
            .valorPretendido(BigDecimal.TEN)
            .build();

        when(professorProfileRepository.findByUserId(userId)).thenReturn(Optional.of(entity));

        ProfessorResponseDto result = professorService.getMyProfile(userId);

        assertThat(result.getNomeCompleto()).isEqualTo("Joao Gabriel");
    }

    @Test
    void getMyProfile_perfilNaoExiste_lancaProfessorNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(professorProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.getMyProfile(userId)).isInstanceOf(ProfessorNotFoundException.class);
    }

    @Test
    void updateMyProfile_atualizaCamposESalva() {
        UUID userId = UUID.randomUUID();
        ProfessorProfileEntity entity = ProfessorProfileEntity.builder().id(UUID.randomUUID()).userId(userId).build();
        when(professorProfileRepository.findByUserId(userId)).thenReturn(Optional.of(entity));

        ProfessorUpdateRequestDto update = new ProfessorUpdateRequestDto();
        update.setNomeCompleto("Novo Nome");
        update.setTelefoneWhatsapp("(11) 98888-7777");
        update.setCidade("Sao Paulo");
        update.setBairro("Moema");
        update.setFaixa(FaixaEnum.MARROM);
        update.setTempoExperienciaAnos(5);
        update.setEspecialidades(Set.of(EspecialidadeEnum.NO_GI));
        update.setDiasDisponiveis(Set.of(DiaSemanaEnum.TERCA));
        update.setPeriodosDisponiveis(Set.of(PeriodoEnum.MANHA));
        update.setTipoCobranca(TipoCobrancaEnum.POR_AULA);
        update.setValorPretendido(new BigDecimal("150.00"));

        ProfessorResponseDto result = professorService.updateMyProfile(userId, update);

        assertThat(result.getNomeCompleto()).isEqualTo("Novo Nome");
        assertThat(result.getFaixa()).isEqualTo(FaixaEnum.MARROM);
        verify(professorProfileRepository).save(entity);
    }

    @Test
    void updateMyPhoto_arquivoVazio_lancaBadRequestException() {
        UUID userId = UUID.randomUUID();
        MockMultipartFile arquivoVazio = new MockMultipartFile("foto", new byte[0]);

        assertThatThrownBy(() -> professorService.updateMyPhoto(userId, arquivoVazio)).isInstanceOf(BadRequestException.class);
        verify(professorProfileRepository, never()).findByUserId(any());
    }

    @Test
    void updateMyPhoto_tipoDeArquivoInvalido_lancaBadRequestException() {
        UUID userId = UUID.randomUUID();
        MockMultipartFile pdf = new MockMultipartFile("foto", "doc.pdf", "application/pdf", "conteudo".getBytes());

        assertThatThrownBy(() -> professorService.updateMyPhoto(userId, pdf)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void updateMyPhoto_arquivoValido_enviaParaStorageEAtualizaFotoUrl() {
        UUID userId = UUID.randomUUID();
        UUID professorId = UUID.randomUUID();
        ProfessorProfileEntity entity = ProfessorProfileEntity.builder().id(professorId).userId(userId).build();
        MockMultipartFile foto = new MockMultipartFile("foto", "foto.jpg", "image/jpeg", "bytes-da-imagem".getBytes());

        when(professorProfileRepository.findByUserId(userId)).thenReturn(Optional.of(entity));
        when(storageService.uploadFile(eq("professor/" + professorId + "/foto-perfil"), any(), eq((long) foto.getSize()), eq("image/jpeg")))
            .thenReturn("http://localhost:9000/tatame-photos/professor/" + professorId + "/foto-perfil");

        ProfessorResponseDto result = professorService.updateMyPhoto(userId, foto);

        assertThat(result.getFotoUrl()).isEqualTo("http://localhost:9000/tatame-photos/professor/" + professorId + "/foto-perfil");
        verify(professorProfileRepository).save(entity);
    }

    @Test
    void updateMyCertificado_arquivoVazio_lancaBadRequestException() {
        UUID userId = UUID.randomUUID();
        MockMultipartFile arquivoVazio = new MockMultipartFile("certificado", new byte[0]);

        assertThatThrownBy(() -> professorService.updateMyCertificado(userId, arquivoVazio)).isInstanceOf(BadRequestException.class);
        verify(professorProfileRepository, never()).findByUserId(any());
    }

    @Test
    void updateMyCertificado_tipoDeArquivoInvalido_lancaBadRequestException() {
        UUID userId = UUID.randomUUID();
        MockMultipartFile video = new MockMultipartFile("certificado", "video.mp4", "video/mp4", "conteudo".getBytes());

        assertThatThrownBy(() -> professorService.updateMyCertificado(userId, video)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void updateMyCertificado_arquivoPdfValido_enviaParaStorageEAtualizaCertificadoUrl() {
        UUID userId = UUID.randomUUID();
        UUID professorId = UUID.randomUUID();
        ProfessorProfileEntity entity = ProfessorProfileEntity.builder().id(professorId).userId(userId).build();
        MockMultipartFile certificado = new MockMultipartFile("certificado", "faixa-preta.pdf", "application/pdf", "bytes-do-pdf".getBytes());
        String expectedUrl = "http://localhost:9000/tatame-photos/professor/" + professorId + "/certificado";

        when(professorProfileRepository.findByUserId(userId)).thenReturn(Optional.of(entity));
        when(storageService.uploadFile(
            eq("professor/" + professorId + "/certificado"), any(), eq((long) certificado.getSize()), eq("application/pdf")
        )).thenReturn(expectedUrl);

        ProfessorResponseDto result = professorService.updateMyCertificado(userId, certificado);

        assertThat(result.getCertificadoUrl()).isEqualTo(expectedUrl);
        verify(professorProfileRepository).save(entity);
    }

    @Test
    void getById_professorNaoExiste_lancaProfessorNotFoundException() {
        UUID professorId = UUID.randomUUID();
        when(professorProfileRepository.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.getById(professorId)).isInstanceOf(ProfessorNotFoundException.class);
    }

    @Test
    void search_delegaParaRepositorioComSpecificationEMapeiaResultado() {
        ProfessorProfileEntity entity = ProfessorProfileEntity.builder()
            .id(UUID.randomUUID())
            .nomeCompleto("Joao Gabriel")
            .faixa(FaixaEnum.PRETA)
            .tipoCobranca(TipoCobrancaEnum.MENSAL)
            .valorPretendido(BigDecimal.TEN)
            .build();
        Page<ProfessorProfileEntity> page = new PageImpl<>(java.util.List.of(entity));
        ProfessorSearchFilter filter = new ProfessorSearchFilter("Sao Paulo", null, null, null, null, null, null);
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 20);

        when(professorProfileRepository.findAll(org.mockito.ArgumentMatchers.<Specification<ProfessorProfileEntity>>any(), eq(pageable)))
            .thenReturn(page);

        Page<ProfessorResponseDto> result = professorService.search(filter, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getNomeCompleto()).isEqualTo("Joao Gabriel");
    }
}
