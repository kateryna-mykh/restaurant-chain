import rquestURLs from 'constants/backendURLs';
import axios from 'misc/requests';

const deleteRestaurant = id => ({
    id,
    type: 'DELETE_RESTAURANT',
});

const errorDeleteRestaurant = () => ({
    type: 'ERROR_DELETE_RESTAURANT',
});

const deleteRestaurantRequest = (id) => (dispatch) => {
console.log(`${rquestURLs.restauratsEntityPath}/${id}`);
    return axios.delete(`${rquestURLs.restauratsEntityPath}/${id}`)
    .catch(() => dispatch(deleteRestaurant(id)))
    .catch(() => dispatch(errorDeleteRestaurant()));
};

export default { deleteRestaurantRequest };