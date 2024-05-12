import rquestURLs from 'constants/backendURLs';
import axios from 'misc/requests';

const MOCK_RESTAURANTS_LIST_RESPONSE = {
    "restaurants": [
        {
            "id": 1,
            "chainName": "Test",
            "locationAddress": "testAddress",
            "seetsCapacity": 10,
            "menuItems": ["item1", "item2", "item3"]
        },
        {
            "id": 2,
            "chainName": "Test",
            "locationAddress": "testAddress2",
            "seetsCapacity": 15,
            "menuItems": ["item1", "item2", "item3"]
        }
    ],
    "totalPages": 1
};

const receiveRestaurants = restaurants => ({
    restaurants,
    type: 'RECEIVE_RESTAURANTS',
});

const requestRestaurants = () => ({
    type: 'REQUEST_RESTAURANTS',
});

const errorReceiveRestaurants = () => ({
    type: 'ERROR_RECEIVE_RESTAURANTS',
});

const getRestaurants = () => {
    return axios.get(`${rquestURLs.restauratsEntityPath}`)
        .catch(() => MOCK_RESTAURANTS_LIST_RESPONSE)
};

const fetchRestaurants = () => (dispatch) => {
    dispatch(requestRestaurants());
    return getRestaurants().then(r =>
        dispatch(receiveRestaurants(r)))
        .catch(() => dispatch(errorReceiveRestaurants()));
};

export default { fetchRestaurants };