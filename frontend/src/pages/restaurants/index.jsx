import React, { useMemo } from 'react';
import IntlProvider from 'misc/providers/IntlProvider';
import useLocationSearch from 'misc/hooks/useLocationSearch';

import getMessages from './intl';
import { Provider } from 'react-redux';
import rootReducer from './reducers/restaurants';
import Restaurants from './containers/Restaurants';
import configureStore from 'misc/redux/configureStore';

const store = configureStore(rootReducer);

function Index(props) {
    const {
        lang,
    } = useLocationSearch();
    const messages = useMemo(() => getMessages(lang), [lang]);
    return (
        <Provider store={store}>
            <IntlProvider messages={messages}>
                <Restaurants {...props} />
            </IntlProvider>
        </Provider>
    );
}

export default Index;