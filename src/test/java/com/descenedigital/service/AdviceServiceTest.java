package com.descenedigital.service;

import com.descenedigital.dto.AdviceRequest;
import com.descenedigital.dto.AdviceResponse;
import com.descenedigital.dto.RatingRequest;
import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.RatingRepository;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdviceServiceTest {

    @Mock
    private AdviceRepo adviceRepo;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdviceService adviceService;

    private User testUser;
    private Advice testAdvice;
    private AdviceRequest adviceRequest;
    private RatingRequest ratingRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        testAdvice = Advice.builder()
                .id(1L)
                .message("Test advice message")
                .category("Test Category")
                .author(testUser)
                .averageRating(4.5)
                .ratingCount(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        adviceRequest = new AdviceRequest();
        adviceRequest.setMessage("Test advice message");
        adviceRequest.setCategory("Test Category");

        ratingRequest = new RatingRequest();
        ratingRequest.setRating(5);
    }

    @Test
    void testGetAllAdvice() {
        List<Advice> adviceList = Arrays.asList(testAdvice);
        Page<Advice> advicePage = new PageImpl<>(adviceList);
        
        when(adviceRepo.findAll(any(Pageable.class))).thenReturn(advicePage);

        // Test with no filters
        Page<AdviceResponse> result = adviceService.getAllAdvice(0, 10, "createdAt", "desc", null, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test advice message", result.getContent().get(0).getMessage());
        
        verify(adviceRepo).findAll(any(Pageable.class));
        verify(adviceRepo, never()).findByFilters(any(), any(), any(Pageable.class));
    }

    @Test
    void testGetAllAdviceWithFilters() {
        List<Advice> adviceList = Arrays.asList(testAdvice);
        Page<Advice> advicePage = new PageImpl<>(adviceList);
        
        // Mock the findByFilters method with the exact parameters we'll pass
        when(adviceRepo.findByFilters(eq("Test Category"), eq("test message"), any(Pageable.class)))
                .thenReturn(advicePage);

        // Call with both category and message filters
        Page<AdviceResponse> result = adviceService.getAllAdvice(0, 10, "createdAt", "desc", "Test Category", "test message");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test advice message", result.getContent().get(0).getMessage());
        verify(adviceRepo).findByFilters(eq("Test Category"), eq("test message"), any(Pageable.class));
    }

    @Test
    void testGetAllAdviceWithCategoryFilter() {
        List<Advice> adviceList = Arrays.asList(testAdvice);
        Page<Advice> advicePage = new PageImpl<>(adviceList);
        
        // Test with only category filter
        when(adviceRepo.findByFilters(eq("Test Category"), eq(null), any(Pageable.class)))
                .thenReturn(advicePage);

        Page<AdviceResponse> result = adviceService.getAllAdvice(0, 10, "createdAt", "desc", "Test Category", null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(adviceRepo).findByFilters(eq("Test Category"), eq(null), any(Pageable.class));
    }

    @Test
    void testGetAllAdviceWithMessageFilter() {
        List<Advice> adviceList = Arrays.asList(testAdvice);
        Page<Advice> advicePage = new PageImpl<>(adviceList);
        
        // Test with only message filter
        when(adviceRepo.findByFilters(eq(null), eq("test message"), any(Pageable.class)))
                .thenReturn(advicePage);

        Page<AdviceResponse> result = adviceService.getAllAdvice(0, 10, "createdAt", "desc", null, "test message");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(adviceRepo).findByFilters(eq(null), eq("test message"), any(Pageable.class));
    }

    @Test
    void testGetAdviceById() {
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(testAdvice));

        AdviceResponse result = adviceService.getAdviceById(1L);

        assertNotNull(result);
        assertEquals("Test advice message", result.getMessage());
        assertEquals("testuser", result.getAuthorUsername());
    }

    @Test
    void testGetAdviceById_NotFound() {
        when(adviceRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adviceService.getAdviceById(1L);
        });

        assertEquals("Advice not found with id: 1", exception.getMessage());
    }

    @Test
    void testCreateAdvice() {
        when(userService.getCurrentUser("testuser")).thenReturn(testUser);
        when(adviceRepo.save(any(Advice.class))).thenReturn(testAdvice);

        AdviceResponse result = adviceService.createAdvice(adviceRequest, "testuser");

        assertNotNull(result);
        assertEquals("Test advice message", result.getMessage());
        assertEquals("Test Category", result.getCategory());
        verify(adviceRepo).save(any(Advice.class));
    }

    @Test
    void testUpdateAdvice() {
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(testAdvice));
        when(adviceRepo.save(any(Advice.class))).thenReturn(testAdvice);

        AdviceRequest updateRequest = new AdviceRequest();
        updateRequest.setMessage("Updated message");
        updateRequest.setCategory("Updated Category");

        AdviceResponse result = adviceService.updateAdvice(1L, updateRequest, "testuser");

        assertNotNull(result);
        verify(adviceRepo).save(any(Advice.class));
    }

    @Test
    void testDeleteAdvice() {
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(testAdvice));

        assertDoesNotThrow(() -> {
            adviceService.deleteAdvice(1L, "testuser");
        });

        verify(adviceRepo).delete(testAdvice);
    }

    @Test
    void testRateAdvice_NewRating() {
        when(userService.getCurrentUser("testuser")).thenReturn(testUser);
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(testAdvice));
        when(ratingRepository.findByUserAndAdvice(testUser, testAdvice)).thenReturn(Optional.empty());
        when(ratingRepository.save(any(Rating.class))).thenReturn(new Rating());
        when(ratingRepository.calculateAverageRating(testAdvice)).thenReturn(4.5);
        when(ratingRepository.countRatingsByAdvice(testAdvice)).thenReturn(3);

        assertDoesNotThrow(() -> {
            adviceService.rateAdvice(1L, ratingRequest, "testuser");
        });

        verify(ratingRepository).save(any(Rating.class));
        verify(adviceRepo).save(testAdvice);
    }

    @Test
    void testRateAdvice_UpdateExistingRating() {
        Rating existingRating = Rating.builder()
                .id(1L)
                .user(testUser)
                .advice(testAdvice)
                .rating(3)
                .build();

        when(userService.getCurrentUser("testuser")).thenReturn(testUser);
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(testAdvice));
        when(ratingRepository.findByUserAndAdvice(testUser, testAdvice)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(existingRating);
        when(ratingRepository.calculateAverageRating(testAdvice)).thenReturn(4.0);
        when(ratingRepository.countRatingsByAdvice(testAdvice)).thenReturn(2);

        assertDoesNotThrow(() -> {
            adviceService.rateAdvice(1L, ratingRequest, "testuser");
        });

        assertEquals(5, existingRating.getRating());
        verify(ratingRepository).save(existingRating);
    }

    @Test
    void testGetTopRatedAdvice() {
        List<Advice> adviceList = Arrays.asList(testAdvice);
        Page<Advice> advicePage = new PageImpl<>(adviceList);
        
        when(adviceRepo.findTopRated(any(Pageable.class))).thenReturn(advicePage);

        Page<AdviceResponse> result = adviceService.getTopRatedAdvice(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(adviceRepo).findTopRated(any(Pageable.class));
    }

    @Test
    void testGetAdviceByAuthor() {
        List<Advice> adviceList = Arrays.asList(testAdvice);
        Page<Advice> advicePage = new PageImpl<>(adviceList);
        
        when(userService.getCurrentUser("testuser")).thenReturn(testUser);
        when(adviceRepo.findByAuthor(eq(testUser), any(Pageable.class))).thenReturn(advicePage);

        Page<AdviceResponse> result = adviceService.getAdviceByAuthor("testuser", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testuser", result.getContent().get(0).getAuthorUsername());
    }

    @Test
    void testIsOwner_True() {
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(testAdvice));

        boolean result = adviceService.isOwner(1L, "testuser");

        assertTrue(result);
    }

    @Test
    void testIsOwner_False() {
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(testAdvice));

        boolean result = adviceService.isOwner(1L, "otheruser");

        assertFalse(result);
    }

    @Test
    void testIsOwner_AdviceNotFound() {
        when(adviceRepo.findById(1L)).thenReturn(Optional.empty());

        boolean result = adviceService.isOwner(1L, "testuser");

        assertFalse(result);
    }
} 