import { HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  let token: string | null = null;

  if (typeof window !== 'undefined') {
    token = localStorage.getItem('token');
  }

  if (req.url.includes('/api/auth')) {
    return next(req);
  }

  const authReq = token
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      }) 
    : req;

  return next(authReq).pipe(
    catchError((error) => {

      console.error('HTTP Error:', error.status);

      
      if (error.status === 401) {
        localStorage.removeItem('token');
      }

      return throwError(() => error);
    })
  );
};