import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useDispatch } from 'react-redux';
import { jwtDecode } from 'jwt-decode';
import { login, type LoginRequest } from '../../services/authService';
import { loginSucesso } from '../../redux/authSlice';


interface DecodedToken {
  sub: string; //email
  roles: string[];
  nome?: string;
  exp: number;//expiracao
}


interface Usuario {
  email: string;
  nome: string; 
}

function Login() {
  const navigator = useNavigate();
  const dispatch = useDispatch();

  const [formData, setFormData] = useState<LoginRequest>({
    email: "",
    senha: "",
  });

  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError("");
    setIsLoading(true);

    try {
      const responseData = await login(formData);
      const token = responseData.token;

      if (token) {
        localStorage.setItem("authToken", token);

        try {
          const decoded = jwtDecode<DecodedToken>(token);

          if (decoded.exp * 1000 < Date.now()) {
            throw new Error("Token expirado recebido do login.");
          }

          const usuarioParaRedux: Usuario = {
              email: decoded.sub,
              nome: decoded.nome || decoded.sub,
          };
          const payload = {
              usuario: usuarioParaRedux,
              token: token
          };

          dispatch(loginSucesso(payload));
          if (decoded.roles.includes('ROLE_ADMIN')) {
            navigator("/admin/emitir-certificado"); 
          } else {
            navigator("/meus-certificados"); 
          }


        } catch (decodeError) {
          console.error("Erro ao decodificar token ou token inválido:", decodeError);
          setError("Erro ao processar login. Token inválido.");
          localStorage.removeItem('authToken');
          setIsLoading(false);

        }
      } else {
        setError("Erro ao obter token. Tente novamente.");
        setIsLoading(false);
      }
    } catch (err: any) {
      console.error("Erro no login:", err);
      const errorMessage = err.response?.data?.message || err.response?.data || "E-mail ou senha inválidos. Tente novamente.";
      setError(errorMessage);
      setIsLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2 className="mb-4 text-center">Entrar</h2>

      {error && <div className="alert alert-danger">{error}</div>}

      <div className="mb-3">
        <label htmlFor="email" className="form-label">E-mail</label>
        <input
          type="email"
          name="email"
          className="form-control"
          id="email"
          value={formData.email}
          onChange={handleChange}
          placeholder="Digite seu e-mail"
          required
          disabled={isLoading}
        />
      </div>

      <div className="mb-3">
        <label htmlFor="senha" className="form-label">Senha</label>
        <input
          type="password"
          className="form-control"
          id="senha"
          name="senha"
          value={formData.senha}
          onChange={handleChange}
          placeholder="Digite sua senha"
          required
          disabled={isLoading}
        />
      </div>
      <button type="submit" className="btn btn-primary w-100" disabled={isLoading}>
        {isLoading ? 'Entrando...' : 'Entrar'}
      </button>
      <p className="mt-3 text-center">
        Não tem uma conta? <Link to="/auth/cadastro">Cadastre-se</Link>
      </p>
       <p className="mt-2 text-center">
         <Link to="/auth/esqueci-senha">Esqueci minha senha</Link>
       </p>
    </form>
  );
}

export default Login;