import { Schema, model } from 'mongoose';

export interface IReview {
    _id: string;
    date: Date;
    restaurantId: number;
    text: string;
    phoneNumber: string;
    name?: string;
    mark?: number;
}

const reviewSchema = new Schema<IReview>({
  date: { type: Date, required: true },
  restaurantId: { type: Number, required: true },
  text: { type: String, required: true },
  phoneNumber: { type: String, required: true },
  name: { type: String, required: false },
  mark: { type: Number, required: false },
});

const Review = model<IReview>('Review', reviewSchema);

export default Review;