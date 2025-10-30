import React from 'react';
import { Route, Routes, Navigate } from "react-router-dom";

// --- LAYOUTS ---
import LayoutPrivado from "./components/layoutAdmin/index"; 
import LayoutLogin from "./components/layoutLogin/index";

// --- PÁGINAS ---
import Home from "./pages/home/index"; 
import Login from "./pages/login";
import Cadastro from "./pages/cadastrese";

// --- COMPONENTES DE ROTA ---
import { PrivateRoute } from "./components/private/index";
import { AdminRoute } from "./components/private/AdminRoute";

// --- PÁGINAS DO PAINEL ---
import EmitirCertificadoPage from "./pages/admin/emitirCertificados";
import GerenciarCursosPage from "./pages/admin/gerenciarCursos";
import ListarCertificadosPage from "./pages/certificados/listarUser";

function AppRoutes() {
    return (
        <Routes>
            {/* === ÁREA PÚBLICA (SEM SIDEBAR) === */}
            <Route path="/" element={<Home />} /> 
            
            <Route element={<LayoutLogin />}>
                <Route path="/auth/login" element={<Login />} />
                <Route path="/auth/cadastro" element={<Cadastro />} />
            </Route>

            {/* === ÁREA PRIVADA (COM SIDEBAR) === */}
            <Route element={<PrivateRoute />}>
                <Route element={<LayoutPrivado />}>
                    {/* A ROTA /dashboard FOI REMOVIDA */}
                    <Route path="/meus-certificados" element={<ListarCertificadosPage />} />

                    <Route element={<AdminRoute />}>
                        <Route path="/admin/emitir-certificado" element={<EmitirCertificadoPage />} />
                        <Route path="/admin/cursos" element={<GerenciarCursosPage />} />
                    </Route>
                </Route>
            </Route>

            {/* Rota para qualquer URL não encontrada */}
            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    );
}

export default AppRoutes;