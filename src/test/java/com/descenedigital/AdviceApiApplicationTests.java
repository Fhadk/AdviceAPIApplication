package com.descenedigital;

import com.descenedigital.model.Advice;
import com.descenedigital.repo.AdviceRepository;
import com.descenedigital.service.AdviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AdviceApiApplicationTests {
		@Mock
		private AdviceRepository adviceRepository;

		@InjectMocks
		private AdviceService adviceService;

		@BeforeEach
		void setUp() {
			MockitoAnnotations.openMocks(this);
		}
}

