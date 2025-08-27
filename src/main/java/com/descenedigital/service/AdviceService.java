package com.descenedigital.service;
import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepository;
import com.descenedigital.repo.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdviceService {

    private final AdviceRepository adviceRepository;
    private final RatingRepository ratingRepository;

    public AdviceService(AdviceRepository adviceRepository, RatingRepository ratingRepository) {
		super();
		this.adviceRepository = adviceRepository;
		this.ratingRepository = ratingRepository;
	}

	public Advice save(Advice advice) {
        return adviceRepository.save(advice);
    }

    public Advice update(Long id, Advice newAdvice) {
        Advice advice = adviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advice not found"));
        advice.setMessage(newAdvice.getMessage());
        return adviceRepository.save(advice);
    }

    public void delete(Long id) {
        adviceRepository.deleteById(id);
    }

    public List<Advice> findAll() {
        return adviceRepository.findAll();
    }

    public Advice findById(Long id) {
        return adviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advice not found"));
    }

    public Advice rateAdvice(Long adviceId, User user, int score) {
        Advice advice = findById(adviceId);
        Rating rating = ratingRepository.findByUserAndAdvice(user, advice)
                .orElse(new Rating());
        rating.setUser(user);
        rating.setAdvice(advice);
        rating.setScore(score);
        ratingRepository.save(rating);

        return advice;
    }
    public List<Advice> getTopRatedAdvices() {
        return adviceRepository.findTopRatedAdvices();
    }

    public List<Advice> getLowRatedAdvices() {
        return adviceRepository.findLowRatedAdvices();
    }

}
