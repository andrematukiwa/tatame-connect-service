package com.tatame.connect.feature.academia.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tatame.connect.exception.base.BadRequestException;
import com.tatame.connect.feature.academia.exception.AcademiaNotFoundException;
import com.tatame.connect.feature.academia.model.domain.TipoEspacoEnum;
import com.tatame.connect.feature.academia.model.dto.AcademiaResponseDto;
import com.tatame.connect.feature.academia.model.dto.AcademiaSignupRequestDto;
import com.tatame.connect.feature.academia.model.dto.AcademiaUpdateRequestDto;
import com.tatame.connect.feature.academia.repository.AcademiaProfileRepository;
import com.tatame.connect.feature.academia.repository.entity.AcademiaProfileEntity;
import com.tatame.connect.feature.auth.model.domain.RoleEnum;
import com.tatame.connect.feature.auth.model.dto.AuthResponseDto;
import com.tatame.connect.feature.auth.repository.entity.UserEntity;
import com.tatame.connect.feature.auth.service.AuthService;
import com.tatame.connect.feature.storage.StorageService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class AcademiaServiceImplTest {

    @Mock
    private AcademiaProfileRepository academiaProfileRepository;

    @Mock
    private AuthService authService;

    @Mock
    private StorageService storageService;

    private AcademiaServiceImpl academiaService;

    @BeforeEach
    void setUp() {
        academiaService = new AcademiaServiceImpl(academiaProfileRepository, authService, storageService);
    }

    private AcademiaSignupRequestDto buildSignupRequest() {
        AcademiaSignupRequestDto dto = new AcademiaSignupRequestDto();
        dto.setEmail("contato@academia.com");
        dto.setSenha("senha12345");
        dto.setNomeAcademia("Gracie Barra Mooca");
        dto.setResponsavel("Carlos Gracie");
        dto.setTelefoneWhatsapp("(11) 99999-0000");
        dto.setCidade("Sao Paulo");
        dto.setBairro("Mooca");
        dto.setTipoEspaco(TipoEspacoEnum.ACADEMIA);
        dto.setAceiteTermos(true);
        return dto;
    }

    @Test
    void signup_criaUsuarioEPerfilDeAcademia() {
        UUID userId = UUID.randomUUID();
        UserEntity user = UserEntity.builder().id(userId).email("contato@academia.com").role(RoleEnum.ACADEMIA).build();
        AuthResponseDto expected = AuthResponseDto.builder().token("jwt").userId(userId).build();

        when(authService.registerUser("contato@academia.com", "senha12345", RoleEnum.ACADEMIA)).thenReturn(user);
        when(authService.buildAuthResponse(user)).thenReturn(expected);

        AuthResponseDto result = academiaService.signup(buildSignupRequest());

        ArgumentCaptor<AcademiaProfileEntity> captor = ArgumentCaptor.forClass(AcademiaProfileEntity.class);
        verify(academiaProfileRepository).save(captor.capture());
        assertThat(captor.getValue().getNomeAcademia()).isEqualTo("Gracie Barra Mooca");
        assertThat(captor.getValue().getUserId()).isEqualTo(userId);
        assertThat(result).isSameAs(expected);
    }

    @Test
    void getMyProfile_perfilNaoExiste_lancaAcademiaNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(academiaProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> academiaService.getMyProfile(userId)).isInstanceOf(AcademiaNotFoundException.class);
    }

    @Test
    void updateMyProfile_atualizaESalva() {
        UUID userId = UUID.randomUUID();
        AcademiaProfileEntity entity = AcademiaProfileEntity.builder().id(UUID.randomUUID()).userId(userId).build();
        when(academiaProfileRepository.findByUserId(userId)).thenReturn(Optional.of(entity));

        AcademiaUpdateRequestDto update = new AcademiaUpdateRequestDto();
        update.setNomeAcademia("Nova Academia");
        update.setResponsavel("Novo Responsavel");
        update.setTelefoneWhatsapp("(11) 98888-0000");
        update.setCidade("Sao Paulo");
        update.setBairro("Moema");
        update.setTipoEspaco(TipoEspacoEnum.CENTRO_TREINAMENTO);

        AcademiaResponseDto result = academiaService.updateMyProfile(userId, update);

        assertThat(result.getNomeAcademia()).isEqualTo("Nova Academia");
        verify(academiaProfileRepository).save(entity);
    }

    @Test
    void updateMyPhoto_tipoInvalido_lancaBadRequestException() {
        UUID userId = UUID.randomUUID();
        MockMultipartFile arquivo = new MockMultipartFile("foto", "video.mp4", "video/mp4", "conteudo".getBytes());

        assertThatThrownBy(() -> academiaService.updateMyPhoto(userId, arquivo)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void updateMyPhoto_valido_atualizaFotoCapaUrl() {
        UUID userId = UUID.randomUUID();
        UUID academiaId = UUID.randomUUID();
        AcademiaProfileEntity entity = AcademiaProfileEntity.builder().id(academiaId).userId(userId).build();
        MockMultipartFile foto = new MockMultipartFile("foto", "capa.png", "image/png", "bytes".getBytes());
        String expectedUrl = "http://localhost:9000/tatame-photos/academia/" + academiaId + "/foto-capa";

        when(academiaProfileRepository.findByUserId(userId)).thenReturn(Optional.of(entity));
        when(storageService.uploadFile(eq("academia/" + academiaId + "/foto-capa"), any(), eq((long) foto.getSize()), eq("image/png")))
            .thenReturn(expectedUrl);

        AcademiaResponseDto result = academiaService.updateMyPhoto(userId, foto);

        assertThat(result.getFotoCapaUrl()).isEqualTo(expectedUrl);
    }
}
