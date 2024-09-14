package com.jabguru.reviewms.review;

import com.jabguru.reviewms.review.messaging.ReviewMessageProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMessageProducer reviewMessageProducer;

    public ReviewController(ReviewService reviewService, ReviewMessageProducer reviewMessageProducer) {
        this.reviewService = reviewService;
        this.reviewMessageProducer = reviewMessageProducer;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId) {
        return ResponseEntity.ok(reviewService.getAllReviews(companyId));
    }

    @PostMapping
    public ResponseEntity<String> addReview(@RequestParam Long companyId, @RequestBody Review review) {
        boolean reviewAdded = reviewService.addReview(companyId, review);
        if (reviewAdded) {
            reviewMessageProducer.sendMessage(review);
            return new ResponseEntity<>("Review Added Successfully!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Review not saved!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId, @RequestBody Review review) {
        boolean isReviewUpdated = reviewService.updateReview(reviewId, review);
        if(isReviewUpdated)
            return ResponseEntity.ok("Review updated successfully!");   
        return new ResponseEntity<>("Review not updated!", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        boolean isReviewDeleted = reviewService.deleteReview(reviewId);
        if(isReviewDeleted)
            return ResponseEntity.ok("Review deleted successfully!");
        return new ResponseEntity<>("Review not deleted!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/averageRating")
    public Double getAverageReview(@RequestParam Long companyId) {
        List<Review> reviews = reviewService.getAllReviews(companyId);
        return reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }
}
