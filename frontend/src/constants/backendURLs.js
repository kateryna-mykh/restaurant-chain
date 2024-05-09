import config from 'config';

const backendURLs = {
    'restauratsEntityPath': `${config.BACKEND_SERVICE}/api/restaurants`,
    'restauratsQueryPath': `${config.BACKEND_SERVICE}/api/restaurants/_list`,
    'restauratsReportPath': `${config.BACKEND_SERVICE}/api/restaurants/_report`,
    'restauratsUploadPath': `${config.BACKEND_SERVICE}/api/restaurants/upload`,
};

export default backendURLs;