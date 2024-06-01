import RestaurantsPage from 'pages/restaurants';
import React from 'react';

import PageContainer from './components/PageContainer';

const Restaurants = (props) => {
  return (
    <PageContainer>
      <RestaurantsPage {...props} />
    </PageContainer>
  );
};

export default Restaurants;
