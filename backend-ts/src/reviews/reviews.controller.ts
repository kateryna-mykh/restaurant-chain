import log4js from 'log4js';
import httpStatus from 'http-status';
import { Request, Response } from 'express';
import {
  createReview as createReviewApi,
  findListReviews as findListReviewsApi,
  countByRestaurantId as countByRestaurantIdApi,
} from 'src/reviews/reviews.service';
import { ReviewSaveDto } from 'src/reviews/dto/reviewSaveDto';
import { RestaurantListIds } from 'src/reviews/dto/restaurantListIds';
import { InternalError } from 'src/system/internalError';

export const saveReview = async (req: Request, res: Response) => {
  try {
    const review = new ReviewSaveDto(req.body);
    const id = await createReviewApi({
      ...review,
    });
    res.status(httpStatus.CREATED).send({id});
  } catch (err) {
    const { message, status } = new InternalError(err);
    log4js.getLogger().error('Error in creating review.', err);
    res.status(status).send({ message });
  }
};

export const listReviewsSortDesc = async (req: Request, res: Response) => {
  try {
    const result = await findListReviewsApi(
      parseInt(req.params.id as string), 
      parseInt(req.query.from as string) || 0,
      parseInt(req.query.size as string) || 10);
    if(!result) {
      res.status(httpStatus.NOT_FOUND).send();
    } else {
      res.send({result});
    }
  } catch (err) {
    const { message, status } = new InternalError(err);
    log4js.getLogger().error(`Error in getting reviews.`, err);
    res.status(status).send({ message });
  }
};


export const reviewCountByRestaurantId = async (req: Request, res: Response) => {
  try {
    const {ids} = new RestaurantListIds(req.body);
    const result = await countByRestaurantIdApi({...ids});
    res.send(result);
  } catch (err) {
    const { message, status } = new InternalError(err);
    log4js.getLogger().error(`Error in receiving reviews by restaurant ids.`, err);
    res.status(status).send({ message });
  }
};
