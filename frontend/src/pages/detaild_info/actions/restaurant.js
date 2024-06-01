import rquestURLs from 'constants/backendURLs';
import axios from 'misc/requests';
import {
    ADD_RESTAURANT,
    ERROR_RECEIVE_RESTAURANT,
    RECEIVE_RESTAURANT,
    REQUEST_RESTAURANT,
    UPDATE_RESTAURANT
} from '../constants/ActionTypes';

const MOCK_RESTAURANTS_DATA = [
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
    },
];

const receiveRestaurant = restaurant => ({
    restaurant,
    type: RECEIVE_RESTAURANT,
});

const requestRestaurant = (id) => ({
    id,
    type: REQUEST_RESTAURANT,
});

const errorReceiveRestaurant = (err) => ({
    payload: err,
    type: ERROR_RECEIVE_RESTAURANT,
});

const addRestaurant = () => ({
    type: ADD_RESTAURANT,
});

const updateRestaurant = () => ({
    type: UPDATE_RESTAURANT,
});

const getRestaurant = ({ id }) => {
    return axios.get(`${rquestURLs.restauratsEntityPath}/${id}`)
        .catch(() => { return MOCK_RESTAURANTS_DATA.restaurants.find(r => r.id === id) });
};

const editRestaurant = (id, restaurant) => (dispatch) => {
    dispatch(requestRestaurant());
    return axios.put(`${rquestURLs.restauratsEntityPath}/${id}`, restaurant)
        .catch(r => dispatch(addRestaurant()))
        .catch(err => dispatch(errorReceiveRestaurant(err.message)));
};

const updateCurrRestaurant = (id, restaurant) => (dispatch) => {
    dispatch(requestRestaurant());
    return axios.put(`${rquestURLs.restauratsEntityPath}/${id}`, restaurant)
        .catch(r => dispatch(updateRestaurant))
        .catch(err => dispatch(errorReceiveRestaurant(err.message)));
};

const addNewRestaurant = (restaurant) => (dispatch) => {
    dispatch(requestRestaurant());
    return axios.post(`${rquestURLs.restauratsEntityPath}`, restaurant)
        .catch(r => dispatch(addRestaurant()))
        .catch(err => dispatch(errorReceiveRestaurant(err.message)));
};

const fetchRestaurant = (id) => (dispatch) => {
    dispatch(requestRestaurant(id));
    return getRestaurant(id)
        .then(r => dispatch(receiveRestaurant(r)))
        .catch(err => dispatch(errorReceiveRestaurant(err.message)));
};

export default { addNewRestaurant, fetchRestaurant, editRestaurant, updateCurrRestaurant };