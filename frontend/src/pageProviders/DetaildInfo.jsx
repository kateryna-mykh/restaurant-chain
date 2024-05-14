import DetailedInfoPage from 'pages/detaild_info';
import React from 'react';

import PageContainer from './components/PageContainer';

const DetailedInfo = (props) => {
  return (
    <PageContainer>
      <DetailedInfoPage {...props} />
    </PageContainer>
  );
};

export default DetailedInfo;
