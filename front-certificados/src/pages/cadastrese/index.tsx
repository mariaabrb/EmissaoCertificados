import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { Alert, Spinner } from 'react-bootstrap';

interface UsuarioRequestDto {
  nome: string;
  email: string;
  senha: string;
}

function Cadastro() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<UsuarioRequestDto>({
    nome: '',
    email: '',
    senha: '',
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData(prevState => ({ ...prevState, [name]: value }));
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault(); 
    
    setIsLoading(true);
    setError('');
    setSuccess('');

    try {
      await axios.post('http://localhost:8080/usuarios', formData);
      setSuccess('Cadastro realizado com sucesso! Redirecionando para o login...');
      setTimeout(() => {
        navigate('/auth/login');
      }, 2000);
    } catch (err: any) {
      console.error("Erro no cadastro:", err);
      setError('Não foi possível realizar o cadastro. Verifique os dados.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <h2 className="mb-4 text-center">Cadastre-se</h2>
      <form onSubmit={handleSubmit}> 
        {error && <Alert variant="danger">{error}</Alert>}
        {success && <Alert variant="success">{success}</Alert>}
        
        <div className="mb-3">
          <label htmlFor="nome" className="form-label">Nome</label>
          <input 
            type="text" 
            className="form-control" 
            id="nome" 
            name="nome"
            value={formData.nome}
            onChange={handleChange}
            placeholder="Seu nome completo" 
            required 
          />
        </div>
        
        <div className="mb-3">
          <label htmlFor="email" className="form-label">E-mail</label>
          <input 
            type="email" 
            className="form-control" 
            id="email" 
            name="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="seu@email.com" 
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
            placeholder="Crie uma senha" 
            required 
          />
        </div>
        
        <div className="d-grid">
          <button type="submit" className="btn btn-primary" disabled={isLoading}>
            {isLoading ? 'Cadastrando...' : 'Cadastrar'}
          </button>
        </div>
      </form>
    </>
  );
}

export default Cadastro;