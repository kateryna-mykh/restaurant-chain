export class ReviewSaveDto {
  date: Date = new Date();
  restaurantId?: number;
  text?: string;
  phoneNumber?: string;
  name?: string;
  mark?: number;

  constructor(data: Partial<ReviewSaveDto>) {
    this.restaurantId = data.restaurantId;
    this.text = data.text;
    this.phoneNumber = data.phoneNumber;
    this.name = data.name;
    this.mark = data.mark;
  }
}