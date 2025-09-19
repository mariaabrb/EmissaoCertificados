import axios from "axios";
import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

interface LoginRequest {
  email: string;
  senha: string;
}

interface LoginResponse {
  token: string;
}

function Login() {
  const navigator = useNavigate();
  const API_URL = "http://localhost:8080/";

  const [formData, setFormData] = useState<LoginRequest>({
    email: "",
    senha: "",
  });

  const [error, setError] = useState("");

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

    try {
      const response = await axios.post<LoginResponse>(
        API_URL + "auth/login",
        formData
      );
      const token = response.data.token;
      
      if (token) {
        localStorage.setItem("authToken", token);
        navigator("/");
      }

      // EXEMPLO DE FETCH
      // const response = await fetch(API_URL + "auth/login", {
      //  method: "POST",
      //  headers: {
      //    "Content-Type": "application/json",
      //  },
      //  body: JSON.stringify(formData),
      // });

      //  if (!response.ok) {
      //    throw new Error("Erro ao efetuar login do usuário!");
      // }

      // const data: LoginResponse = await response.json();
      //console.log(data.token);
    } catch (err) {
      console.error(err);
      setError("E-mail ou senha inválidos. Tente novamente.");
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
        />
      </div>

      <button type="submit" className="btn btn-primary w-100">Entrar</button>

      <p className="mt-3 text-center">
        Não tem uma conta? <Link to="/auth/cadastro">Cadastre-se</Link>
      </p>
    </form>
  );
}

export default Login;