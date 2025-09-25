import { Route, Routes } from "react-router-dom";
import LayoutAdmin from "./components/layoutAdmin/index";
import Home from "./pages/home/index";
import Login from "./components/login";
import Usuario from "./pages/usuario";
import LayoutLogin from "./components/layoutLogin/index";
import Cadastro from "./pages/cadastrese";
import { PrivateRoute } from "./components/private/index";
import CadastroCertificadoPage from "./pages/certificados";
import ListarCertificadosPage from "./pages/certificados/listar";

function AppRoutes() {
    return (
        <Routes>
            <Route path="/auth" element={<LayoutLogin />}>
                <Route path="login" element={<Login />} />
                <Route path="cadastro" element={<Cadastro />} />
            </Route>
            <Route element={<PrivateRoute />}>
                <Route element={<LayoutAdmin />}>
                    <Route path="/" element={<Home />} />
                    <Route path="/usuario" element={<Usuario />} />
                    <Route path="/certificados/novo" element={<CadastroCertificadoPage />} />
                    <Route path="/certificados" element={<ListarCertificadosPage />} />
                </Route>
            </Route>
        </Routes>
    );
}

export default AppRoutes;