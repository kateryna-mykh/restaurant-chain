import {
    REQUEST_RESTAURANT,
    RECEIVE_RESTAURANT,
    ERROR_RECEIVE_RESTAURANT,
    ADD_RESTAURANT,
    UPDATE_RESTAURANT
} from "../constants/ActionTypes";

const initialState = {
    isLoading: false,
    isFailed: false,
    list: [],
    obj: null,
    errorMessage: '',
};

export default function Reducer(state = initialState, action) {
    switch (action.type) {
        case REQUEST_RESTAURANT: {
            return {
                ...state,
                isLoading: true,
            };
        }
        case UPDATE_RESTAURANT:
        case ADD_RESTAURANT: {
            return {
                ...state,
                isLoading: false,
            };
        }
        case RECEIVE_RESTAURANT: {
            return {
                ...state,
                isLoading: false,
                obj: action.restaurant,
            };
        }
        case ERROR_RECEIVE_RESTAURANT: {
            return { ...state, isFailed: true, isLoading: false, errorMessage: action.payload };
        }
        default: { return state; }
    }
}