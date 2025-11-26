import { Navigate, Outlet } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
  sub: string;
  roles: string[];
  exp: number;
  iat: number;
}

const useAuthAdmin = (): boolean => {
  const token = localStorage.getItem('authToken');
  if (!token) {
    return false;
  }

  try {
    const decodedToken = jwtDecode<DecodedToken>(token);
    
    if (decodedToken.exp * 1000 < Date.now()) {
      return false;
    }

    return decodedToken.roles.includes('ROLE_ADMIN');
  } catch (error) {
    console.error(error);
    return false;
  }
};

export function AdminRoute() {
  const isAdmin = useAuthAdmin();
  return isAdmin ? <Outlet /> : <Navigate to="/" />;
}