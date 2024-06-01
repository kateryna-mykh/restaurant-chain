import * as React from "react";
import { createUseStyles } from 'react-jss';
import useTheme from 'misc/hooks/useTheme';

const getClasses = createUseStyles((theme) => ({
    notification: {
        zIndex: '100',
        position: 'fixed',
        bottom: '1rem',
        right: '20px',
        height: '60px',

        color: 'black',
        background: '#F5F5F5',
        
        display: 'flex',
        justifyContent: 'flex-start',
        alignItems: 'center',
        padding: '0.5rem',
        borderRadius: '1rem',
        borderBottom: '1rem solid #8896AB',
        fontSize: '20px',
        fontWeight: '400'
    },
}));


const Notification = ({
    msg
}) => {
    const { theme } = useTheme();
    const classes = getClasses({ theme });
    return <div className={classes.notification}>{msg}</div>;
}

export default Notification;