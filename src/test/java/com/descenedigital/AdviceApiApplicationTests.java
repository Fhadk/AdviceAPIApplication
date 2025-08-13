package com.descenedigital;

import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.service.AdviceService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;


class AdviceApiApplicationTests {
		@Mock
		private AdviceRepo adviceRepository;

		@InjectMocks
		private AdviceService adviceService;

		@BeforeEach
		void setUp() {
			MockitoAnnotations.openMocks(this);
		}
}

