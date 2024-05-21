import express from 'express';
import ping from 'src/ping.controller';
import reviews from 'src/reviews/reviews.router';

const router = express.Router();

router.get('/ping', ping);

router.use('/api/reviews', reviews);

export default router;
