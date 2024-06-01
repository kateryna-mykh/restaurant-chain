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
import actionsRestaurants from '../actions/fetchRestaurants';
import { MODE_EDIT, MODE_VIEW } from '../constants/modes';

const mockedChainNames = [
    { id: 1, name: "Delicious Eats" },
    { id: 2, name: "Asian Fusion" },
    { id: 3, name: "Tasty Bites" },
    { id: 4, name: "Barvy" },
    { id: 5, name: "BEEF Meat & Wine" },
];

const Restaurants = (props) => {
    const restaurantsList = useSelector((store) => store.list);
    const isFailed = useSelector((store) => store.isFailed);
    const pages = useSelector((store) => store.pages);

    const { formatMessage } = useIntl();
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [showCriteria, setShowCritera] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
    const [filterQuery, setFilterQuery] = useState({
        address: '',
        chainId: '',
        page: 0,
        size: 10,
    });

    const handlePageChange = (event, value) => {
        setCurrentPage(value);
        Storage.setItem("currentPage", JSON.stringify(value));
        setFilterQuery({
            ...filterQuery,
            page: value - 1,
        });
    };

    const handleFilterCriteria = () => {
        Storage.setItem("filterCriteria", JSON.stringify(filterQuery));
    };

    const searchRequest = () => {
        setShowCritera(JSON.parse(Storage.getItem("showFilter")));
        let parsedPage = JSON.parse(Storage.getItem("currentPage"));
        setCurrentPage(parsedPage);

        let filerCriteria = JSON.parse(Storage.getItem("filterCriteria"));
        if (filerCriteria !== null) {
            setFilterQuery({
                ...filterQuery,
                address: filerCriteria.address,
                chainId: filerCriteria.chainId,
                page: parsedPage - 1,
            });
        } else {
            setFilterQuery({
                ...filterQuery,
                address: '',
                chainId: '',
                page: parsedPage - 1,
            });
        }

        dispatch(actionsRestaurants.fetchRestaurants(JSON.stringify(filterQuery)));
    };
    useEffect(() => {
        searchRequest();
    }, [currentPage]);

    const handleShowCriteria = () => {
        const v = !showCriteria;
        setShowCritera(v);
        Storage.setItem("showFilter", JSON.stringify(v));
    };

    return (
        <div>
            <div style={{ textAlign: 'right', width: '140px', padding: '0px 0px 8px calc(100% - 140px)' }}>
                <Button fullWidth={true} colorVariant='header' onClick={() => navigate(`${pagesURLs.restaurants}/0?mode=${MODE_EDIT}`)}>
                    + {formatMessage({ id: 'button.add' })}
                </Button>
            </div>
            <div style={{ display: 'flex', alignItems: 'flex' }}>
                <div style={{ display: 'flex', flexDirection: 'column', width: '80%', margin: '0px auto' }}>
                    {restaurantsList.map((r, i) =>
                        <Record key={i} restaurant={r} isFailed={isFailed}
                            onClick={() => navigate(`${pagesURLs.restaurants}/${r.id}?mode=${MODE_VIEW}`)}
                        />)}
                </div>
                <div style={{ width: '140px' }}>
                    <Button colorVariant='secondary' fullWidth={true} onClick={() => handleShowCriteria()}>
                        <FilterIcon /> {formatMessage({ id: 'button.filter' })}
                    </Button>
                    {showCriteria && <div style={{ border: '1px solid black', display: 'inline-list-item' }}>
                        <span>{formatMessage({ id: 'filter.location.lable' })}: </span>
                        <span style={{ display: 'flex' }}>
                            <input placeholder='address..' value={filterQuery.address}
                                onChange={(e) => setFilterQuery({
                                    ...filterQuery,
                                    address: e.target.value,
                                })} style={{ width: '100%' }}></input>
                        </span>
                        <span>{formatMessage({ id: 'filter.chainName.lable' })}:</span>
                        {
                            mockedChainNames.map(chain =>
                                <Button colorVariant='secondary' fullWidth={true} key={chain.id}
                                    onClick={() => setFilterQuery({
                                        ...filterQuery,
                                        chainId: chain.id,
                                    })}>{chain.name}</Button>)
                        }
                        <Button colorVariant='header' fullWidth={true}
                            onClick={() => { handleFilterCriteria(); searchRequest(); }}>
                            {formatMessage({ id: 'button.apply' })}
                        </Button>
                    </div>
                    }
                </div>
            </div>
            <div style={{ display: 'flex', width: '80%', justifyContent: 'center', margin: '20px' }}>
                <Pagination count={pages} page={currentPage} onChange={handlePageChange} />
            </div>
        </div>
    );
}

export default Restaurants;