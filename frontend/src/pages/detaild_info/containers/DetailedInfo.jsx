import { useIntl } from 'react-intl'
import { useParams, useNavigate } from 'react-router-dom';
import IconButton from "components/IconButton";
import EditIcon from "../components/icons/Edit";
import Button from "components/Button";
import pagesURLs from 'constants/pagesURLs';
import actionRestaurant from '../actions/restaurant';
import Notification from "components/Notification";

import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { MODE_EDIT, MODE_VIEW } from 'pages/restaurants/constants/modes';

const mockedIds = Array.from(['1', '2', '3', '4', '5'].values());

const DetailedInfo = (props) => {
    const params = useParams();
    const [currentMode, setCurrentMode] = useState(() => Object.is(params.id, '0') ? MODE_EDIT : MODE_VIEW);
    const [editState, setEditState] = useState({
        locationAddress: '',
        manager: '',
        seatsCapacity: '',
        employeesNumber: '',
        restaurantChainId: '1',
        menuItems: [],
    });
    const isFailed = useSelector((store) => store.isFailed);
    const restaurant = useSelector((store) => store.obj);

    const [timer, setTimer] = useState(false);
    const [creationFailed, setCreationFailed] = useState(false);

    const { formatMessage } = useIntl();
    const dispatch = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        if (currentMode === MODE_VIEW) {
            dispatch(actionRestaurant.fetchRestaurant(JSON.stringify(params.id)));
        }
    }, []);

    const handleSaveAndCreate = (isCreationFailed, event) => {
        // dispatch(actionRestaurant.addNewRestaurant(JSON.stringify(editState)));
        if (!isCreationFailed) {
            setCurrentMode(MODE_VIEW);
            setTimer(true);
            setTimeout(() => { setTimer(false) }, 3000);
        } else {
            setCurrentMode(MODE_EDIT);
        }
    };
    return (
        <div>
            {Object.is(currentMode, MODE_VIEW) && <div style={{ textAlign: 'right', padding: '0 0 0 20px' }}>
                <IconButton onClick={() => { setCurrentMode(MODE_EDIT) }}><EditIcon /></IconButton>
                <Button colorVariant='primary' onClick={() => navigate(`${pagesURLs.restaurants}`)}>
                    {formatMessage({ id: 'button.back' })}
                </Button>
            </div>
            }
            {Object.is(currentMode, MODE_VIEW) &&
                <div style={{ display: 'grid' }} >
                    <label>{formatMessage({ id: 'restaurant.info.location' })}</label>
                    <label>{formatMessage({ id: 'restaurant.info.manager' })}</label>
                    <label>{formatMessage({ id: 'restaurant.info.seats' })}</label>
                    <label>{formatMessage({ id: 'restaurant.info.emploees' })}</label>
                    <label>{formatMessage({ id: 'restaurant.info.chainId' })}</label>
                    <label>{formatMessage({ id: 'restaurant.info.menu' })}</label>
                </div>
            }
            {Object.is(currentMode, MODE_EDIT) &&
                <div>
                    {creationFailed && <span style={{ color: 'red' }}>{formatMessage({ id: 'delete.display.notification' })}</span>}
                    <div style={{ display: 'grid' }} >

                        <label>{formatMessage({ id: 'restaurant.info.location' })}</label>
                        <input defaultValue={editState.locationAddress}
                            on={e => setEditState({ ...editState, locationAddress: e.target.value })}></input>

                        <label>{formatMessage({ id: 'restaurant.info.manager' })}</label>
                        <input value={editState.manager}
                            onChange={e => setEditState({ ...editState, manager: e.target.value })}></input>

                        <label>{formatMessage({ id: 'restaurant.info.seats' })}</label>
                        <input type='number' min={1} max={150000} value={editState.seatsCapacity}
                            onChange={e => setEditState({ ...editState, seatsCapacity: e.target.value })}></input>

                        <label>{formatMessage({ id: 'restaurant.info.emploees' })}</label>
                        <input type='number' min={1} max={Number.MAX_SAFE_INTEGER} value={editState.employeesNumber}
                            onChange={e => setEditState({ ...editState, employeesNumber: e.target.value })}></input>

                        <label>{formatMessage({ id: 'restaurant.info.chainId' })}</label>
                        <select value={editState.restaurantChainId}
                            onChange={e => setEditState({ ...editState, crestaurantChainId: e.target.value })}>{
                                mockedIds.map(i => <option key={i} value={i}>{i}</option>)}
                        </select>

                        <label>{formatMessage({ id: 'restaurant.info.menu' })}</label>
                        <input value={editState.menuItems} onChange={e => setEditState({ ...editState, menuItems: e.target.value })}></input>
                    </div>

                    <div style={{ padding: '20px 0' }} >
                        <Button colorVariant='header' onClick={(e) => handleSaveAndCreate(e)} >
                            {formatMessage(Object.is(params.id, '0') ? { id: 'button.create' } : { id: 'button.save' })}
                        </Button>
                        <Button onClick={() => { navigate(`${pagesURLs.restaurants}`) }}>{formatMessage({ id: 'button.cancel' })}</Button>
                    </div>
                </div>
            }
            {
                timer && !creationFailed && (<Notification msg={formatMessage({ id: 'delete.display.notification' })} />)
            }
        </div >
    );
}

/*
                    <label>{formatMessage({ id: 'restaurant.info.location' })}</label>
                    {restaurant.locationAddress}
                    <label>{formatMessage({ id: 'restaurant.info.manager' })}</label>
                    {restaurant.manager}
                    <label>{formatMessage({ id: 'restaurant.info.seats' })}</label>
                    {restaurant.seatsCapacity}
                    <label>{formatMessage({ id: 'restaurant.info.emploees' })}</label>
                    {restaurant.employeesNumber}
                    <label>{formatMessage({ id: 'restaurant.info.chainId' })}</label>
                    {restaurant.restaurantChainId}
                    <label>{formatMessage({ id: 'restaurant.info.menu' })}</label>
                    {restaurant.menuItems}
*/
export default DetailedInfo;