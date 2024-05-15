import { useIntl } from 'react-intl'
import { useParams } from 'react-router-dom';
import useLocationSearch from 'misc/hooks/useLocationSearch';
import IconButton from "components/IconButton";
import EditIcon from "../components/icons/Edit";
import Button from "components/Button";
import pagesURLs from 'constants/pagesURLs';
import { useNavigate } from 'react-router-dom';

import React, { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';

const DetailedInfo = (props) => {
    const { id } = useParams();
    const { mode } = useLocationSearch();
    const [currentMode, seCurrentMode] = useState(mode);

    const { formatMessage } = useIntl();
    const dispatch = useDispatch();
    const navigate = useNavigate();

    console.log(mode);
    console.log(id);
    console.log('curent', currentMode);
    //mode === 'view'
    //if id === 0 && mode === 'edit => create new
    return (
        <div>
            <div style={{ textAlign: 'right' }}>
                <IconButton onClick={() => { }}><EditIcon /></IconButton>
                <Button colorVariant='primary' onClick={() => navigate(`${pagesURLs.restaurants}`)}>
                    {formatMessage({ id: 'button.back' })}
                </Button>
            </div>
            <div style={{ display: 'block' }}>
                <div style={{ display: 'block' }}>
                    <label>{formatMessage({ id: 'restaurant.info.location' })}</label>
                    <input></input>
                </div>
                <div>
                    <label>{formatMessage({ id: 'restaurant.info.manager' })}</label>
                    <input></input>
                </div>
                <div>
                    <label>{formatMessage({ id: 'restaurant.info.seets' })}</label>
                    <input></input>
                </div>
                <div>
                    <label>{formatMessage({ id: 'restaurant.info.emploees' })}</label>
                    <input></input>
                </div>
                <div>
                    <label>{formatMessage({ id: 'restaurant.info.chainId' })}</label>
                    <input></input>
                </div>
                <div>
                    <label>{formatMessage({ id: 'restaurant.info.menu' })}</label>
                    <input></input>
                </div>
            </div>

            <div>
                <Button colorVariant='header' onClick={() => { }}>
                    {formatMessage(Object.is(id, '0') ? { id: 'button.create' } : { id: 'button.save' })}
                </Button>
                <Button onClick={() => { }}>{formatMessage({ id: 'button.cancel' })}</Button>
            </div>
        </div>
    );
}

export default DetailedInfo;