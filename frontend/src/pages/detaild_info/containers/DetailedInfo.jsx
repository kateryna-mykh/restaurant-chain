import React from 'react';
import { useIntl } from 'react-intl'
import Typography from 'components/Typography';
import {useLocation, useNavigate, useParams} from 'react-router-dom';

const DetailedInfo = (props) => {
    const location = useLocation();
    const navigate = useNavigate();
    const params = useParams();
    console.log(location);
    console.log(navigate);
    console.log(params);
     return (
    <Typography>
    props.id = {params.id}  
      Just test text
    </Typography>
  );
}

export default DetailedInfo;