import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Dashboard } from './features/dashboard/dashboard/dashboard';
import { Profile } from './features/profile/profile';

export const routes: Routes = [

  { path: '', component: Login },

  { path: 'dashboard', component: Dashboard },

 
  { path: 'profile/:id', component: Profile },

  
  { path: 'profile', component: Profile },

  {
    path: 'network',
    loadComponent: () =>
      import('./features/network/network')
        .then(m => m.Network)
  },

 {
  path: 'analytics',
  loadComponent: () =>
    import('./features/analytics/analytics')
      .then(m => m.Analytics)
} , //madu

{
  path: 'forgot-security',
  loadComponent: () =>
    import('./features/auth/forgot-security/forgot-security')
      .then(m => m.ForgotSecurity)
},

 {
    path: 'business-analytics',
    loadComponent: () =>
      import('./features/business-analytics/business-analytics')
        .then(m => m.BusinessAnalytics)
  }

];