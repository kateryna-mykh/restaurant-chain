const initialState = {
    isLoading: false,
    isFailed: false,
    list: [],
    pages: 0,
};

export default function Reducer(state = initialState, action) {
    switch (action.type) {
        case 'REQUEST_RESTAURANTS': {
            return {
                ...state,
                isLoading: true,
            };
        }
        case 'RECEIVE_RESTAURANTS': {
            const {
                restaurants
            } = action;
            return {
                ...state,
                isLoading: false,
                list: restaurants.restaurants,
                pages: restaurants.totalPages,
            };
        }
        case 'DELETE_RESTAURANT': {
            const {id} = action;
            return {
                ...state,
                list: state.list.filter(restaurant => restaurant.id !== id)
            };
        }
        case 'ERROR_RECEIVE_RESTAURANTS': {
            return { isFailed: true, isLoading: false, };
        }
        case 'ERROR_DELETE_RESTAURANT': {
            return { isFailed: true };
        }
        default: { return state; }
    }
}