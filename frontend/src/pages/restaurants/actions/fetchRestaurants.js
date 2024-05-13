import rquestURLs from 'constants/backendURLs';
import axios from 'misc/requests';

const MOCK_RESTAURANTS_LIST_RESPONSE = {
    "restaurants": [
        {
            "id": 1,
            "chainName": "Delicious Eats",
            "locationAddress": "123 Main Street",
            "seetsCapacity": 50,
            "menuItems": ["Burger","Pizza","Salad","French Fries","Soft Drink"]
        },
        {
            "id": 2,
            "chainName": "Delicious Eats",
            "locationAddress": "1516 Maple Drive",
            "seetsCapacity": 60,
            "menuItems": ["Burger","Pizza","Salad","Cheese Fries","Lemonade"]
        },
        {
            "id": 3,
            "chainName": "Asian Fusion",
            "locationAddress": "1718 Walnut Lane",
            "seetsCapacity": 60,
            "menuItems": ["Curry","Noodles","Spring Rolls","Jasmine Rice","Thai Iced Tea"]
        },
        {
            "id": 4,
            "chainName": "Asian Fusion",
            "locationAddress": "1920 Elmwood Avenue",
            "seetsCapacity": 65,
            "menuItems": ["Curry","Noodles","Spring Rolls","Vegetable Tempura","Green Tea"
            ]
        },
        {
            "id": 5,
            "chainName": "BEEF Meat & Wine",
            "locationAddress": "2122 Oakwood Boulevard",
            "seetsCapacity": 70,
            "menuItems": ["Borch","Varenyki","Zrazy","Uzvar","Сheesecakes"]
        },
        {
            "id": 6,
            "chainName": "Tasty Bites",
            "locationAddress": "456 Oak Avenue",
            "seetsCapacity": 45,
            "menuItems": ["Steak","Pasta","Sandwich","Caesar Salad","Iced Tea"]
        },
        {
            "id": 7,
            "chainName": "Tasty Bites",
            "locationAddress": "1314 Cedar Lane",
            "seetsCapacity": 55,
            "menuItems": ["Pizza","Salad","French Fries","Soft Drink","Brownie"]
        }
    ],
    "totalPages": 2
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