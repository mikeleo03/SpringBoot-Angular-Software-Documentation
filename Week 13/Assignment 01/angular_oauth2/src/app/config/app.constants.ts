export const AppConstants = {
    APPLICATION_NAME: 'Authentication using OAuth2',
    BASE_API_URL: '/my-api',
    LOG_OFF_ICON: 'sign-out',
};
  
export interface RouteLink {
    path: string;
    link: string;
}
  
export const RouterConfig = {
    HOME: { path: '', link: '/', title: 'Home' },
    LOGIN: {
        path: 'login',
        link: '/login',
        title: 'Login',
        data: { header: true },
    },
    NOT_FOUND: { path: '**', link: null, title: 'Page Not Found' },
};