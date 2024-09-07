import { Routes } from '@angular/router';
import { RouterConfig } from './config/app.constants';
import { AuthGuard } from './main/guards/auth-guard.service';

export const routes: Routes = [
    {
        path: RouterConfig.LOGIN.path,
        loadChildren: () =>
            import('./pages/login/login.route').then((m) => m.loginRoutes),
        title: RouterConfig.LOGIN.title,
        data: RouterConfig.LOGIN.data,
    },
    {
        path: '',
        canActivate: [AuthGuard],
        children: [
            {
                path: RouterConfig.HOME.path,
                loadChildren: () =>
                    import('./pages/home/home.route').then((m) => m.homeRoutes),
                title: "Home Page",
                data: { header: true },
            }
        ],
    },
];