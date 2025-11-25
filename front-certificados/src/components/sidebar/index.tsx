import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import './sidebar.css';
import { FaPlusCircle, FaBook, FaUsers, FaSignOutAlt, FaUserGraduate, FaUserCircle, FaHome } from 'react-icons/fa'; 
import { useDispatch } from 'react-redux';
import { logout } from '../../redux/authSlice';

interface DecodedToken {
  roles: string[];
}

function Sidebar() {
  const navigate = useNavigate();
  const dispatch = useDispatch(); 
  const token = localStorage.getItem('authToken');
  let isAdmin = false;

  if (token) {
    try {
      const decodedToken = jwtDecode<DecodedToken>(token);
      isAdmin = decodedToken.roles.includes('ROLE_ADMIN');
    } catch (error) {
      console.error("Token inválido na Sidebar:", error);
    }
  }

  const handleLogout = () => {
    dispatch(logout()); 
    navigate('/'); 
  };

  if (!isAdmin) {
      return null;
  }

  return (
    <div className="sidebar">
        
      <nav className="sidebar-nav list-unstyled flex-grow-1">
        
        <li className="nav-item">
          <NavLink to="/meus-certificados" className="nav-link">
            <FaHome /> Home (Meus Certificados)
          </NavLink>
        </li>
        <hr className="my-2" /> 
        
        <li className="nav-item">
          <NavLink to="/admin/emitir-certificado" className="nav-link">
            <FaPlusCircle /> Emitir Certificado
          </NavLink>
        </li>
        
        <li className="nav-item">
          <NavLink to="/admin/cursos" className="nav-link">
            <FaBook /> Gerenciar Cursos
          </NavLink>
        </li>

        <li className="nav-item">
          <NavLink to="/admin/matriculas" className="nav-link">
            <FaUserGraduate /> Gerenciar Matrículas
          </NavLink>
        </li>
   
        <li className="nav-item">
          <NavLink to="/admin/usuarios" className="nav-link">
            <FaUsers /> Gerenciar Usuários
          </NavLink>
        </li>
      </nav>

      <div className="mt-auto pt-3 border-top">
          <li className="nav-item">
              <NavLink to="/perfil" className="nav-link">
                <FaUserCircle /> Meu Perfil
              </NavLink>
          </li>

          <button onClick={handleLogout} className="nav-link text-danger w-100 text-start">
            <FaSignOutAlt /> Sair
          </button>
      </div>
    </div>
  );
}

export default Sidebar;