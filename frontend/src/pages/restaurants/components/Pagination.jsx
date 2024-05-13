import * as React from 'react';
import PaginationMUI from '@mui/material/Pagination';

const Pagination = ({
    count,
    page,
    onChange,
}) => {
    return (
        <PaginationMUI count={count} shape={'rounded'} variant={'outlined'} onChange={onChange} page={page} />
    );
}

export default Pagination;