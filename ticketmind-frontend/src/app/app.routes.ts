import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/authentication/login/login')
        .then(m => m.Login)
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/authentication/register/register')
        .then(m => m.Register)
  },
  {
    path: '',
    loadComponent: () =>
      import('./layouts/main-layout/main-layout')
        .then(m => m.MainLayout),
    children: [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/dashboard')
        .then(m => m.Dashboard)
  },
  {
    path: 'tickets/create',
    loadComponent: () =>
      import('./features/tickets/create-ticket/create-ticket')
        .then(m => m.CreateTicket)
  },
  {
    path: 'tickets',
    loadComponent: () =>
      import('./features/tickets/my-tickets/my-tickets')
        .then(m => m.MyTickets)
  },
  {
    path: 'tickets/:id',
    loadComponent: () =>
        import('./features/tickets/ticket-details/ticket-details')
        .then(m => m.TicketDetails)
}
]
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];