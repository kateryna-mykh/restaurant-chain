import express from 'express';
import {
  saveReview,
  listReviewsSortDesc,
  reviewCountByRestaurantId,
} from 'src/reviews/reviews.controller';

const router = express.Router();

router.get('/:id', listReviewsSortDesc);
router.post('', saveReview);
router.post('/_counts', reviewCountByRestaurantId);

export default router;