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
        "manager": "John Doe",
        "seatsCapacity": 50,
        "employeesNumber": 10,
        "chainShortInfo": {"id": 1, "name": "Delicious Eats", "cuisine": ""},
        "menuItems": ["Burger", "Pizza", "Salad", "French Fries", "Soft Drink"]
    },
    {
        "id": 2,
        "chainName": "Delicious Eats",
        "locationAddress": "1516 Maple Drive",
        "manager": "Jessica Brown",
        "seatsCapacity": 60,
        "employeesNumber": 12,
        "chainShortInfo": {"id": 1, "name": "Delicious Eats", "cuisine": ""},
        "menuItems": ["Burger", "Pizza", "Salad", "Cheese Fries", "Lemonade"]
    },
    {
        "id": 3,
        "chainName": "Asian Fusion",
        "locationAddress": "1718 Walnut Lane",
        "manager": "David Martinez",
        "seatsCapacity": 60,
        "employeesNumber": 12,
        "chainShortInfo": {"id": 2, "name": "Asian Fusion", "cuisine": ""},
        "menuItems": ["Curry", "Noodles", "Spring Rolls", "Jasmine Rice", "Thai Iced Tea"]
    },
    {
        "id": 4,
        "chainName": "Asian Fusion",
        "locationAddress": "1920 Elmwood Avenue",
        "manager": "Amanda Lee",
        "seatsCapacity": 65,
        "employeesNumber": 15,
        "chainShortInfo": {"id": 2, "name": "Asian Fusion", "cuisine": ""},
        "menuItems": ["Curry", "Noodles", "Spring Rolls", "Vegetable Tempura", "Green Tea"
        ]
    },
    {
        "id": 5,
        "chainName": "BEEF Meat & Wine",
        "locationAddress": "2122 Oakwood Boulevard",
        "manager": "Ryan Taylor",
        "seatsCapacity": 70,
        "employeesNumber": 14,
        "chainShortInfo": {"id": 5, "name": "BEEF Meat & Wine", "cuisine": ""},
        "menuItems": ["Borch", "Varenyki", "Zrazy", "Uzvar", "Сheesecakes"]
    },
    {
        "id": 6,
        "chainName": "Tasty Bites",
        "locationAddress": "456 Oak Avenue",
        "manager": "Sarah Anderson",
        "seatsCapacity": 45,
        "employeesNumber": 15,
        "chainShortInfo": {"id": 3, "name": "Tasty Bites", "cuisine": ""},
        "menuItems": ["Steak", "Pasta", "Sandwich", "Caesar Salad", "Iced Tea"]
    },
    {
        "id": 7,
        "chainName": "Tasty Bites",
        "locationAddress": "1314 Cedar Lane",
        "manager": "Jane Smith",
        "seatsCapacity": 55,
        "employeesNumber": 9,
        "chainShortInfo": {"id": 3, "name": "Tasty Bites", "cuisine": ""},
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

const getRestaurant = (id) => {
    return axios.get(`${rquestURLs.restauratsEntityPath}/${id}`)
        .then(response => response.data)
        .catch(() => {
            return MOCK_RESTAURANTS_DATA.find(r => r.id === parseInt(id, 10));
        });
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