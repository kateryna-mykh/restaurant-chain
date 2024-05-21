export class RestaurantListIds {
  ids: number[];
    
  constructor(data: RestaurantListIds) {
    this.ids = [...(data.ids || [])];
  }
}