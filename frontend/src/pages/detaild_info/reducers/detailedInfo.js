const initialState = {
    isLoading: false,
    isFailed: false,
    obj: {},
    errorMessage: '',
};

/*/*const restaurant = {
    "id": 0,
    "chainName": "",
    "locationAddress": "",
    "seetsCapacity": 0,
    "menuItems": ["", "", ""]
}*/

export default function Reducer(state = initialState, action) {
    switch (action.type) {
        case 'ADD_RESTAURANT': {
            return {
                ...state,
                isLoading: false,
            };
        }
        case 'ERROR_ADD_RESTAURANT': {
            return { isFailed: true, isLoading: false, errorMessage: action.payload };
        }
        default: { return state; }
    }
}