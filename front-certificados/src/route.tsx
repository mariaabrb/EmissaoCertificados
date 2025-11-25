import { Route, Routes, Navigate } from "react-router-dom";

import LayoutPrivado from "./components/layoutAdmin/index"; 
import LayoutLogin from "./components/layoutLogin/index";
import Home from "./pages/home/index"; 
import Login from "./pages/login";
import Cadastro from "./pages/cadastrese";
import { PrivateRoute } from "./components/private/index";
import { AdminRoute } from "./components/private/AdminRoute";
import EmitirCertificadoPage from "./pages/admin/emitirCertificados";
import GerenciarCursosPage from "./pages/admin/gerenciarCursos";
import GerenciarMatriculasPage from "./pages/admin/gerenciarMatriculas"; 
import GerenciarUsuariosPage from "./pages/admin/GerenciarUsuariosPage";
import ListarCertificadosPage from "./pages/certificados/listarUser";
import EsqueciSenhaPage from "./pages/auth/EsqueciSenhaPage";
import RedefinirSenhaPage from "./pages/auth/RedefinirSenhaPage";
import VisualizarCertificadoPage from "./pages/certificados/VisualizarCertificadoPage";

function AppRoutes() {
    return (
        <Routes>
            <Route path="/" element={<Home />} /> 
            <Route path="/validar-certificado/:codigo" element={<VisualizarCertificadoPage />} />

            <Route element={<LayoutLogin />}>
                <Route path="/auth/login" element={<Login />} />
                <Route path="/auth/cadastro" element={<Cadastro />} />
                <Route path="/auth/esqueci-senha" element={<EsqueciSenhaPage />} />
                <Route path="/auth/redefinir-senha" element={<RedefinirSenhaPage />} />
            </Route>

            <Route element={<PrivateRoute />}>
                <Route element={<LayoutPrivado />}>
                    <Route path="/meus-certificados" element={<ListarCertificadosPage />} />

                    <Route element={<AdminRoute />}>
                        <Route path="/admin/emitir-certificado" element={<EmitirCertificadoPage />} />
                        <Route path="/admin/cursos" element={<GerenciarCursosPage />} />
                        <Route path="/admin/matriculas" element={<GerenciarMatriculasPage />} />
                        <Route path="/admin/usuarios" element={<GerenciarUsuariosPage />} />
                    </Route>
                </Route>
            </Route>

            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    );
}

export default AppRoutes;