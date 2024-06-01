import React from 'react';
import SvgIcon from 'components/SvgIcon';
import useTheme from 'misc/hooks/useTheme';

const Edit = ({
    color = 'header', // default | header | error | success | warning | info | <string>
    size = 24,
}) => {
    const { theme } = useTheme();
    const actualColor = theme.icon.color[color] || color;
    const strokeColor = theme.header.color.background;
    return (
        <SvgIcon
            style={{
                height: `${size}px`,
                width: `${size}px`,
            }}
            viewBox="0 0 24 24"
            strokeWidth='2'
            stroke={strokeColor}
        >
            <path d="M8.56078 20.2501L20.5608 8.25011L15.7501 3.43945L3.75012 15.4395V20.2501H8.56078ZM15.7501 5.56077L18.4395 8.25011L16.5001 10.1895L13.8108 7.50013L15.7501 5.56077ZM12.7501 8.56079L15.4395 11.2501L7.93946 18.7501H5.25012L5.25012 16.0608L12.7501 8.56079Z"
                fill={actualColor} />
        </SvgIcon>
    );
};

export default Edit;