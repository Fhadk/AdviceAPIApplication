package com.descenedigital;

import com.descenedigital.controller.AdviceController;
import com.descenedigital.controller.AdviceControllerTests;
import com.descenedigital.controller.AuthController;
import com.descenedigital.model.Advice;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.service.AdviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AdviceApiApplicationTests {
		@Mock
		private AdviceRepo adviceRepository;

		@InjectMocks
		private AdviceService adviceService;

		@BeforeEach
		void setUp() {
			MockitoAnnotations.openMocks(AdviceControllerTests.class);
		}
}

