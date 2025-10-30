import React from 'react';
import { Outlet } from 'react-router-dom';
import Footer from "../footer/index";
import Header from "../header/index"; 
import Sidebar from "../sidebar/index";
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
  sub: string;
  roles: string[];
  exp: number;
}

const checkIsAdmin = (): boolean => {
  const token = localStorage.getItem('authToken');
  if (!token) return false;
  try {
    const decoded = jwtDecode<DecodedToken>(token);
    if (decoded.exp * 1000 < Date.now()) return false; // Expirado
    return decoded.roles.includes('ROLE_ADMIN'); // É admin?
  } catch (error) {
    console.error("Erro ao verificar token no LayoutAdmin:", error);
    return false;
  }
};
// --- Fim da lógica ---


function LayoutAdmin() {
  // Executa a verificação para saber se mostra a sidebar
  const isAdmin = checkIsAdmin();

  return (
    // Usa classes flexbox do Bootstrap para estrutura
    // Adiciona 'admin-layout' ou 'user-layout' para possível estilização CSS
    <div className={`d-flex flex-column vh-100 ${isAdmin ? 'admin-layout' : 'user-layout'}`}>
      <Header /> {/* Header sempre visível para usuários logados */}

      <div className="d-flex flex-grow-1 overflow-hidden">
        
        {/* === RENDERIZAÇÃO CONDICIONAL DA SIDEBAR === */}
        {isAdmin && <Sidebar />} 
        {/* A Sidebar só aparece se 'isAdmin' for true */}
        {/* === FIM DA CONDIÇÃO === */}

        {/* Conteúdo principal da página */}
        <main className="flex-grow-1 p-4 overflow-auto">
          {/* Outlet renderiza o componente da rota atual (ex: MeusCertificados, GerenciarCursos) */}
          <Outlet /> 
        </main>
      </div>

      {/* Footer pode ser opcional dentro do layout logado */}
      {/* <Footer /> */} 
    </div>
  );
}

export default LayoutAdmin;