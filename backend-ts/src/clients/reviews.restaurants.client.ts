export const validateRestaurantId = async (restaurantId: number) : Promise<boolean>  => {
  const restaurantResponse = await fetch(
    `http://host.docker.internal:8081/api/restaurants/${restaurantId}`,
    {
      method: "GET", 
      headers: { 'Content-Type': 'application/json'},
    });
  if (!restaurantResponse.ok) {
    throw new Error(`Fetch response not successful.`); 
  }
  
  return restaurantResponse.ok;
};