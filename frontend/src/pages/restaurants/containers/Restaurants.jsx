import { useIntl } from 'react-intl'
import Record from '../components/Record';
import Button from "components/Button";
import FilterIcon from '../components/icons/Filter';
import Pagination from '../components/Pagination';
import Storage from 'misc/storage/index';
import pagesURLs from 'constants/pagesURLs';
import { useNavigate } from 'react-router-dom';

import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import actionsRestaurant from '../actions/fetchRestaurants';

const Restaurants = (props) => {
    const {
        list: restaurantsList,
        isLoading: isLoadingRestaurants,
        isFailed,
        pages,
    } = useSelector((store) => store);

    const { formatMessage } = useIntl();
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [currentPage, setCurrentPage] = useState(1);
    const [baseQuery, setBaseQuery] = useState({
        "page": 0,
        "size": 10
    });

    const handlePageChange = (event, value) => {
        setCurrentPage(value);
        Storage.setItem("currentPage", JSON.stringify(value));
        setBaseQuery({
            ...baseQuery,
            page: value - 1,
        });
    };

    useEffect(() => {
        const savedPage = Storage.getItem("currentPage");
        const parsedPage = JSON.parse(savedPage);
        setCurrentPage(parsedPage);
        dispatch(actionsRestaurant.fetchRestaurants(JSON.stringify(baseQuery)));
    }, [currentPage]);

    return (
        <div>
            <div style={{ padding: '0px 0px 0px calc(100% - 143px)' }}>
                <Button colorVariant='header' onClick={() => navigate(`${pagesURLs.restaurants}/0?mode=edit`)}>
                    + {formatMessage({ id: 'button.add' })}
                </Button>
            </div>
            <div style={{ display: 'flex', alignItems: 'flex' }}>
                <div style={{ display: 'flex', flexDirection: 'column', width: '80%', margin: '0px auto' }}>
                    {restaurantsList.map((r, i) =>
                        <Record key={i} restaurant={r} isFailed={isFailed}
                            onClick={() => navigate(`${pagesURLs.restaurants}/${r.id}?mode=view`)}
                        />)}
                </div>
                <div style={{ position: 'relative' }}>
                    <Button colorVariant='secondary' onClick={() => { }}>
                        <FilterIcon /> {formatMessage({ id: 'button.filter' })}
                    </Button>
                    <div style={{ border: '1px solid black', width: '140px' }}>Here will be filter creterias</div>
                </div>
            </div>
            <div style={{ display: 'flex', width: '80%', justifyContent: 'center', margin: '20px' }}>
                <Pagination count={pages} page={currentPage} onChange={handlePageChange} />
            </div>
        </div>
    );
}
//navigate( `${pagesURLs.restaurants}/${r.id}`)
export default Restaurants;