import rquestURLs from 'constants/backendURLs';
import axios from 'misc/requests';

const deleteRestaurant = id => ({
    id,
    type: 'DELETE_RESTAURANT',
});

const errorDeleteRestaurant = (err) => ({
    err,
    type: 'ERROR_DELETE_RESTAURANT',
});

const deleteRestaurantRequest = (id) => (dispatch) => {
    return axios.delete(`${rquestURLs.restauratsEntityPath}/${id}`)
    .catch(() => dispatch(deleteRestaurant(id)))
    .catch(err => dispatch(errorDeleteRestaurant(err.message)));
};

export default { deleteRestaurantRequest };