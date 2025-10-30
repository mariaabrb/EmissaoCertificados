import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import './sidebar.css';
import { FaPlusCircle, FaBook, FaUsers, FaSignOutAlt } from 'react-icons/fa';

interface DecodedToken {
  roles: string[];
}

function Sidebar() {
  const navigate = useNavigate();
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
    localStorage.removeItem('authToken');

    navigate('/');
  };

  if (!isAdmin) {
     return null;
  }

  return (
    <div className="sidebar">
      <nav className="sidebar-nav list-unstyled flex-grow-1">
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
          <NavLink to="/admin/usuarios" className="nav-link">
            <FaUsers /> Gerenciar Usuários
          </NavLink>
        </li>
      </nav>
      <div className="mt-auto">
        <button onClick={handleLogout} className="nav-link text-danger w-100 text-start">
          <FaSignOutAlt /> Sair
        </button>
      </div>
    </div>
  );
}

export default Sidebar;