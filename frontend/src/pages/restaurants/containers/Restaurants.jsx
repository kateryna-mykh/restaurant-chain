import { useIntl } from 'react-intl'
import Record from '../component/Record';

import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import actionsRestaurant from '../actions/fetchRestaurants';

const Restaurants = (props) => {
    const {
        list: restaurantsList,
        isLoading: isLoadingRestaurants,
        isFailed,
        pages,
    } = useSelector((store) => store);

    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(actionsRestaurant.fetchRestaurants());
    }, []);

    return (
        <div>
            <div style={{ display: 'flex', flexDirection: 'column', width: '80%', margin: '40px auto' }}>
                {restaurantsList.map((r, i) =>
                    <Record key={i} restaurant={r} isFailed={isFailed} />)}
            </div>
        </div>
    );
}

export default Restaurants;