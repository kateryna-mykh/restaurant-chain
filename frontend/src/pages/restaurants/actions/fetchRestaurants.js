import rquestURLs from 'constants/backendURLs';
import axios from 'misc/requests';
import {
    RECEIVE_RESTAURANTS,
    REQUEST_RESTAURANTS,
    ERROR_RECEIVE_RESTAURANTS
} from '../constants/ActionTypes';

const MOCK_RESTAURANTS_LIST_RESPONSE = {
    "restaurants": [
        {
            "id": 1,
            "chainName": "Delicious Eats",
            "locationAddress": "123 Main Street",
            "seatsCapacity": 50,
            "menuItems": ["Burger", "Pizza", "Salad", "French Fries", "Soft Drink"]
        },
        {
            "id": 2,
            "chainName": "Delicious Eats",
            "locationAddress": "1516 Maple Drive",
            "seatsCapacity": 60,
            "menuItems": ["Burger", "Pizza", "Salad", "Cheese Fries", "Lemonade"]
        },
        {
            "id": 3,
            "chainName": "Asian Fusion",
            "locationAddress": "1718 Walnut Lane",
            "seatsCapacity": 60,
            "menuItems": ["Curry", "Noodles", "Spring Rolls", "Jasmine Rice", "Thai Iced Tea"]
        },
        {
            "id": 4,
            "chainName": "Asian Fusion",
            "locationAddress": "1920 Elmwood Avenue",
            "seatsCapacity": 65,
            "menuItems": ["Curry", "Noodles", "Spring Rolls", "Vegetable Tempura", "Green Tea"
            ]
        },
        {
            "id": 5,
            "chainName": "BEEF Meat & Wine",
            "locationAddress": "2122 Oakwood Boulevard",
            "seatsCapacity": 70,
            "menuItems": ["Borch", "Varenyki", "Zrazy", "Uzvar", "Сheesecakes"]
        },
        {
            "id": 6,
            "chainName": "Tasty Bites",
            "locationAddress": "456 Oak Avenue",
            "seatsCapacity": 45,
            "menuItems": ["Steak", "Pasta", "Sandwich", "Caesar Salad", "Iced Tea"]
        },
        {
            "id": 7,
            "chainName": "Tasty Bites",
            "locationAddress": "1314 Cedar Lane",
            "seatsCapacity": 55,
            "menuItems": ["Pizza", "Salad", "French Fries", "Soft Drink", "Brownie"]
        }
    ],
    "totalPages": 2
};

const MOCK_RESTAURANTS_SECOND_PAGE = {
    "restaurants": [],
    "totalPages": 2
}

const receiveRestaurants = restaurants => ({
    restaurants,
    type: RECEIVE_RESTAURANTS,
});

const requestRestaurants = request => ({
    request,
    type: REQUEST_RESTAURANTS,
});

const errorReceiveRestaurants = (err) => ({
    payload: err,
    type: ERROR_RECEIVE_RESTAURANTS,
});

const getRestaurants = (request) => {
    return axios.post(`${rquestURLs.restauratsEntityPath}`, request)
        .catch(() => {
            if (JSON.parse(request).page === 0) {
                return MOCK_RESTAURANTS_LIST_RESPONSE
            }
            else {
                return MOCK_RESTAURANTS_SECOND_PAGE
            }
        })
};

const fetchRestaurants = (request) => (dispatch) => {
    dispatch(requestRestaurants(request));
    return getRestaurants(request).then(r =>
        dispatch(receiveRestaurants(r)))
        .catch(err => dispatch(errorReceiveRestaurants(err.message)));
};

export default { fetchRestaurants };