import React, { useState } from 'react';
import { useIntl } from 'react-intl'
import DeleteIcon from "../components/icons/Delete";
import Notification from "../components/Notification";
import IconButton from "components/IconButton";
import Button from "components/Button";
import Dialog from "components/Dialog";
import actionsRestaurant from '../actions/deleteRestaurant';
import { useDispatch } from 'react-redux';

const Record = ({ restaurant, isFailed, onClick }) => {
    const [showDeleteIcon, setShowDeleteIcon] = useState(false);
    const [open, setOpen] = useState(false);
    const [timer, setTimer] = useState(false);
    const [deletionFailed, setDeletionFailed] = useState(isFailed);
    const { formatMessage } = useIntl();

    const handleClickOpen = (event) => {
        event.stopPropagation();
        setOpen(true);
    };
    const handleClose = (isDeleationFailed, event) => {
        event.stopPropagation();
        if (!isDeleationFailed) {
            setOpen(false);
            setTimer(true);
            setTimeout(() => { setTimer(false) }, 3000);
            setShowDeleteIcon(false);
        } else {
            handleClickOpen();
        }
    };

    const dispatch = useDispatch();
    return (
        <div onMouseOver={() => { setShowDeleteIcon(true) }}
            onMouseOut={() => { setShowDeleteIcon(false) }}
            onClick={onClick}
            style={{
                height: '40px', borderRadius: '4px',
                boxShadow: '2px 2px 4px 1px rgba(136, 150, 171, .5)',
                alignContent: "center", alignItems: 'center'
            }}>
            {restaurant.chainName} |
            {restaurant.locationAddress} |
            {restaurant.seetsCapacity}
            {
                showDeleteIcon && (
                    <IconButton onClick={handleClickOpen}>
                        <DeleteIcon />
                    </IconButton>
                )
            }
            <Dialog
                open={open}
                maxWidth='xs'
                sx>
                <span>{formatMessage({ id: 'delete.display.text' })}</span>
                <span>
                    <Button colorVariant='header' onClick={(e) => {
                        dispatch(actionsRestaurant.deleteRestaurantRequest(restaurant.id));
                        handleClose(deletionFailed, e);
                    }
                    }> {formatMessage({ id: 'button.agree' })}</Button>
                </span>
                {deletionFailed && <span style={{ color: 'red' }}>{formatMessage({ id: 'delete.display.error' })}</span>}
            </Dialog>
            {
                timer && !deletionFailed && (<Notification msg={formatMessage({ id: 'delete.display.notification' })} />)
            }
        </div >
    )
};

export default Record;