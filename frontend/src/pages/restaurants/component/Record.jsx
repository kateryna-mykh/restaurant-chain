import React, { useState, useEffect } from 'react';
import DeleteIcon from "../component/DeleteIcon";
import Notification from "../component/Notification";
import IconButton from "components/IconButton";
import Button from "components/Button";
import Dialog from "components/Dialog";
import actionsDeleteRestaurant from '../actions/deleteRestauraunt';
import { useDispatch } from 'react-redux';

/*const restaurant = {
    "id": 0,
    "chainName": "",
    "locationAddress": "",
    "seetsCapacity": 0,
    "menuItems": ["", "", ""]
}*/

const Record = ({ restaurant, isFailed }) => {
    const [showDeleteIcon, setShowDeleteIcon] = useState(false);
    const [open, setOpen] = useState(false);
    const [timer, setTimer] = useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
        setShowDeleteIcon(false);
    };

    const dispatch = useDispatch();
    return (
        <div onMouseOver={() => { setShowDeleteIcon(true) }}
            onMouseOut={() => { setShowDeleteIcon(false) }}
            onClick={() => { }}
            style={{
                height: '40px', borderRadius: '4px',
                boxShadow: '2px 2px 4px 1px rgba(136, 150, 171, .5)',
                alignContent: "center", alignItems: 'center',
            }}>
            {restaurant.id}
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
                onClose={handleClose}
                maxWidth='xs'
                sx>
                <span>Are you sure you want to delete this entity?</span>
                {isFailed && <span color='red'>Deleation was failed.</span>}
                <span>
                    <Button colorVariant='header' onClick={() => {
                        dispatch(actionsDeleteRestaurant.deleteRestaurantRequest(restaurant.id));
                        handleClose();
                        setTimer(true);
                        setTimeout(() => { setTimer(false) }, 3000);
                    }
                    }>Agree</Button>
                </span>
            </Dialog>
            {
                timer && (<Notification msg='Item was deleted.' />)
            }
        </div>
    )
};

export default Record;