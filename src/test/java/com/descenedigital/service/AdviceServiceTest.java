package com.descenedigital.service;

import com.descenedigital.dto.advice.AdviceRequest;
import com.descenedigital.dto.advice.AdviceResponse;
import com.descenedigital.mapper.AdviceMapper;
import com.descenedigital.model.Advice;
import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdviceServiceTest {

    @Mock
    private AdviceRepo adviceRepo;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdviceMapper adviceMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AdviceService adviceService;

    private User testUser;
    private Advice testAdvice;
    private AdviceRequest testRequest;
    private AdviceResponse testResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .roles(Set.of(Role.USER))
                .build();

        testAdvice = Advice.builder()
                .id(1L)
                .title("Test Advice")
                .message("Test message")
                .description("Test description")
                .author(testUser)
                .createdAt(LocalDateTime.now())
                .averageRating(4.5)
                .ratingCount(2)
                .build();

        testRequest = new AdviceRequest("Test Advice", "Test message", "Test description");

        testResponse = AdviceResponse.builder()
                .id(1L)
                .title("Test Advice")
                .message("Test message")
                .description("Test description")
                .authorUsername("testuser")
                .averageRating(4.5)
                .ratingCount(2)
                .build();

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void getAllAdvice_ShouldReturnPageOfAdviceResponses() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Advice> advicePage = new PageImpl<>(List.of(testAdvice));
        when(adviceRepo.findAll(pageable)).thenReturn(advicePage);
        when(adviceMapper.toResponse(testAdvice)).thenReturn(testResponse);

        // When
        Page<AdviceResponse> result = adviceService.getAllAdvice(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testResponse, result.getContent().get(0));
        verify(adviceRepo).findAll(pageable);
        verify(adviceMapper).toResponse(testAdvice);
    }

    @Test
    void getAdviceById_ShouldReturnAdviceResponse_WhenAdviceExists() {
        // Given
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(testAdvice));
        when(adviceMapper.toResponse(testAdvice)).thenReturn(testResponse);

        // When
        AdviceResponse result = adviceService.getAdviceById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testResponse, result);
        verify(adviceRepo).findById(1L);
        verify(adviceMapper).toResponse(testAdvice);
    }

    @Test
    void getAdviceById_ShouldThrowException_WhenAdviceNotFound() {
        // Given
        when(adviceRepo.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> adviceService.getAdviceById(1L));
        assertEquals("Advice not found with id: 1", exception.getMessage());
        verify(adviceRepo).findById(1L);
        verify(adviceMapper, never()).toResponse(any());
    }

    @Test
    void createAdvice_ShouldReturnAdviceResponse() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(adviceMapper.toEntity(testRequest)).thenReturn(testAdvice);
        when(adviceRepo.save(any(Advice.class))).thenReturn(testAdvice);
        when(adviceMapper.toResponse(testAdvice)).thenReturn(testResponse);

        // When
        AdviceResponse result = adviceService.createAdvice(testRequest);

        // Then
        assertNotNull(result);
        assertEquals(testResponse, result);
        verify(userRepository).findByUsername("testuser");
        verify(adviceMapper).toEntity(testRequest);
        verify(adviceRepo).save(any(Advice.class));
        verify(adviceMapper).toResponse(testAdvice);
    }

    @Test
    void updateAdvice_ShouldReturnUpdatedAdviceResponse_WhenUserIsOwner() {
        // Given
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(testAdvice));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(adviceRepo.save(testAdvice)).thenReturn(testAdvice);
        when(adviceMapper.toResponse(testAdvice)).thenReturn(testResponse);

        // When
        AdviceResponse result = adviceService.updateAdvice(1L, testRequest);

        // Then
        assertNotNull(result);
        assertEquals(testResponse, result);
        verify(adviceRepo).findById(1L);
        verify(userRepository).findByUsername("testuser");
        verify(adviceMapper).updateEntity(testRequest, testAdvice);
        verify(adviceRepo).save(testAdvice);
        verify(adviceMapper).toResponse(testAdvice);
    }

    @Test
    void deleteAdvice_ShouldDeleteAdvice_WhenUserIsOwner() {
        // Given
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(testAdvice));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        adviceService.deleteAdvice(1L);

        // Then
        verify(adviceRepo).findById(1L);
        verify(userRepository).findByUsername("testuser");
        verify(adviceRepo).delete(testAdvice);
    }

    @Test
    void searchAdvice_ShouldReturnPageOfAdviceResponses() {
        // Given
        String searchTerm = "test";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Advice> advicePage = new PageImpl<>(List.of(testAdvice));
        when(adviceRepo.findBySearchTerm(searchTerm, pageable)).thenReturn(advicePage);
        when(adviceMapper.toResponse(testAdvice)).thenReturn(testResponse);

        // When
        Page<AdviceResponse> result = adviceService.searchAdvice(searchTerm, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testResponse, result.getContent().get(0));
        verify(adviceRepo).findBySearchTerm(searchTerm, pageable);
        verify(adviceMapper).toResponse(testAdvice);
    }
}
