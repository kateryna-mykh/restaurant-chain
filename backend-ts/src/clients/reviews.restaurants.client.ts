export const validateRestaurantId = async (restaurantId: number) : Promise<boolean>  => {
  const isRestaurantExist: boolean = await fetch(`${process.env.RESTAURANT_PATH}${restaurantId}`)
    .then(response => response.json())
    .then(r => !!r.data.id);
  if (isRestaurantExist === false) {
    throw new Error(`Restaurant id = ${restaurantId} doesn't exist`);
  }
  return true;
};