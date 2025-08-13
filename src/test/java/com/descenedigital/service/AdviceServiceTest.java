package com.descenedigital.service;

import com.descenedigital.model.Advice;
import com.descenedigital.repo.AdviceRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdviceServiceTest {

    @Mock
    private AdviceRepo adviceRepo;

    @InjectMocks
    private AdviceService adviceService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void list_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Advice a1 = new Advice(1L, "message one");
        Page<Advice> page = new PageImpl<>(List.of(a1));
        when(adviceRepo.findAll(pageable)).thenReturn(page);

        Page<Advice> result = adviceService.list(pageable);
        assertEquals(1, result.getTotalElements());
        verify(adviceRepo).findAll(pageable);
    }

    @Test
    void getById_found() {
        Advice a1 = new Advice(1L, "hello");
        when(adviceRepo.findById(1L)).thenReturn(Optional.of(a1));
        Optional<Advice> found = adviceService.getById(1L);
        assertTrue(found.isPresent());
        assertEquals("hello", found.get().getMessage());
    }

    @Test
    void create_saves() {
        Advice toCreate = new Advice(null, "hi");
        Advice saved = new Advice(5L, "hi");
        when(adviceRepo.save(any())).thenReturn(saved);
        Advice result = adviceService.create(toCreate);
        assertNotNull(result.getId());
        assertEquals(5L, result.getId());
    }

    @Test
    void update_updatesWhenPresent() {
        Advice existing = new Advice(2L, "old");
        Advice updated = new Advice(null, "new");
        when(adviceRepo.findById(2L)).thenReturn(Optional.of(existing));
        when(adviceRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Optional<Advice> res = adviceService.update(2L, updated);
        assertTrue(res.isPresent());
        assertEquals("new", res.get().getMessage());
    }

    @Test
    void delete_deletes() {
        adviceService.delete(9L);
        verify(adviceRepo).deleteById(9L);
    }
}
