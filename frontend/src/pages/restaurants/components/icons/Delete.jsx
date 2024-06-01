import React from 'react';
import SvgIcon from 'components/SvgIcon';
import useTheme from 'misc/hooks/useTheme';

const Delete = ({
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
            <path d="M4 7H20" fill={actualColor}/>
            <path d="M6 10L7.70141 19.3578C7.87432 20.3088 8.70258 21 9.66915 21H14.3308C15.2974 21 16.1257 20.3087 16.2986 19.3578L18 10" fill={actualColor}/>
            <path d="M9 5C9 3.89543 9.89543 3 11 3H13C14.1046 3 15 3.89543 15 5V7H9V5Z" fill={actualColor}/>
        </SvgIcon>
    );
};

export default Delete;