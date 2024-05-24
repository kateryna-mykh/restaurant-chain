import Review, {IReview} from 'src/reviews/reviews.model';
import { ReviewSaveDto } from 'src/reviews/dto/reviewSaveDto';
import { ReviewInfoDto } from 'src/reviews/dto/reviewInfoDto';
import { validateRestaurantId } from 'src/clients/reviews.restaurants.client';

export const createReview = async ( reviewDto: ReviewSaveDto): Promise<string> => {
  await validateReviwParams(reviewDto);
  const review = await new Review(reviewDto).save();
  return review._id;
};

export const findListReviews = async (restaurantId: number, from: number, maxsSize: number): Promise<ReviewInfoDto[]> => {
  const reviews = await Review
    .find({...(restaurantId && {restaurantId})})
    .sort({date: -1})
    .skip(from)
    .limit(maxsSize);
  return reviews.map(review => toInfoDto(review));
};

export const countByRestaurantId = async (listRestaurantIds: number[]): Promise<{ [key: number]: number }> => {
  const validIds: number[] = [...Object.values(listRestaurantIds)];
  
  const counts = await Review.aggregate([
    {$match : {restaurantId: { $in: validIds}}},
    {$group: { _id: '$restaurantId', count: { $sum: 1 }}},
  ]);
  
  const result: { [key: number]: number } = {};  
  validIds.forEach(id => {
    const count = counts.find(c => c._id === id)?.count || 0;
    result[id] = count;
  });
   
  return result;
};

export const validateReviwParams = async (reviewDto: ReviewSaveDto) => {
  await validateRestaurantId(reviewDto.restaurantId ?? 0);
  if (!reviewDto.phoneNumber?.match(/^\d{3}[ -]?\d{3}[ -]?\d{4}$/)){
    throw new Error(`Phone number is incorrect.`); 
  }
  if(reviewDto.text){
    if (reviewDto.text.replace(/\s/g, "").length < 3) {
      throw new Error(`Text of review shouldn't be empty and longer then 2 characters.`); 
    }
  }
  if (reviewDto.mark !== undefined){
    const mark = reviewDto.mark;
    if (mark > 10 || mark < 1) {
      throw new Error(`Mark should be from 1 to 10.`);  
    }
  }
};

const toInfoDto = (review: IReview): ReviewInfoDto => {
  return({
    _id: review._id,
    date: review.date,
    text: review.text,
    mark: review.mark ?? 0,
  });
};
