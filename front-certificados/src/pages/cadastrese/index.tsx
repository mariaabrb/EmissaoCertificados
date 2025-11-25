import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';
import { Alert, Spinner, Form } from 'react-bootstrap'; 
import api from '../../services/api'; 

interface UsuarioRequestDto {
  nome: string;
  email: string;
  senha: string;
  cpf: string; 
  nomeInstituicao: string; 
}

type FormControlElement = HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement;


function Cadastro() {
  const navigate = useNavigate();

  const [instituicaoOptions, setInstituicaoOptions] = useState<string[]>([]); 
  
  const [formData, setFormData] = useState<UsuarioRequestDto>({
    nome: '',
    email: '',
    senha: '',
    cpf: '',
    nomeInstituicao: '', 
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    const fetchInstituicoes = async () => {
        try {
            const response = await api.get<string[]>('api/usuarios/instituicoes/nomes'); 
            
            setInstituicaoOptions(response.data);

            if (response.data.length > 0) {
                setFormData(prev => ({ ...prev, nomeInstituicao: response.data[0] }));
            }
        } catch (err) {
            console.error("Erro ao carregar instituições:", err);
            setError('Não foi possível carregar a lista de instituições.');
        }
    };
    fetchInstituicoes();
  }, []); 

  const handleChange = (event: React.ChangeEvent<FormControlElement>) => {
    const { name, value } = event.target;
    setFormData(prevState => ({ ...prevState, [name]: value }));
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault(); 
    
    setIsLoading(true);
    setError('');
    setSuccess('');

    if (!/^\d{11}$/.test(formData.cpf.replace(/\D/g, ''))) {
        setError('CPF inválido. Digite apenas os 11 números.');
        setIsLoading(false);
        return;
    }
    if (!formData.nomeInstituicao) {
        setError('Por favor, selecione sua instituição.');
        setIsLoading(false);
        return;
    }

    try {
      await api.post('/api/usuarios', formData);
      
      setSuccess('Cadastro realizado com sucesso! Redirecionando para o login...');
      setTimeout(() => {
        navigate('/auth/login');
      }, 2000);
    } catch (err: any) {
      console.error("Erro no cadastro:", err);
      const backendError = err.response?.data?.message || err.response?.data || 'Não foi possível realizar o cadastro. Verifique os dados.';
      setError(backendError);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="container" style={{maxWidth: '500px'}}>
      <h2 className="mb-4 text-center">Cadastre-se</h2>
      <form onSubmit={handleSubmit}> 
        {error && <Alert variant="danger">{error}</Alert>}
        {success && <Alert variant="success">{success}</Alert>}
 
        <div className="mb-3">
          <Form.Label htmlFor="nome" className="form-label">Nome</Form.Label>
          <Form.Control type="text" id="nome" name="nome"
            value={formData.nome} onChange={handleChange} required disabled={isLoading}
          />
        </div>

        <div className="mb-3">
          <Form.Label htmlFor="cpf" className="form-label">CPF</Form.Label>
          <Form.Control type="text" id="cpf" name="cpf"
            value={formData.cpf} onChange={handleChange} placeholder="Digite os 11 números do seu CPF" 
            required maxLength={14} disabled={isLoading}
          />
        </div>

        <Form.Group className="mb-3" controlId="nomeInstituicao">
          <Form.Label>Instituição</Form.Label>
          <Form.Select 
            name="nomeInstituicao"
            value={formData.nomeInstituicao}
            onChange={handleChange} 
            required
            disabled={isLoading || instituicaoOptions.length === 0} 
            aria-label="Selecionar Instituição"
          >
            <option value="">
                {instituicaoOptions.length === 0 ? 'Nenhuma instituição disponível' : '-- Selecione sua instituição --'}
            </option>
            {instituicaoOptions.map(nome => (
                <option key={nome} value={nome}>{nome}</option>
            ))}
          </Form.Select>
        </Form.Group>
        
        <div className="mb-3">
          <Form.Label htmlFor="email" className="form-label">E-mail</Form.Label>
          <Form.Control type="email" id="email" name="email"
            value={formData.email} onChange={handleChange} required disabled={isLoading}
          />
        </div>

        <div className="mb-3">
          <Form.Label htmlFor="senha" className="form-label">Senha</Form.Label>
          <Form.Control type="password" id="senha" name="senha"
            value={formData.senha} onChange={handleChange} placeholder="Crie uma senha" 
            required disabled={isLoading}
          />
        </div>
        
        <div className="d-grid mb-3">
          <button type="submit" className="btn btn-primary" disabled={isLoading || instituicaoOptions.length === 0}>
            {isLoading ? <Spinner size="sm" /> : 'Cadastrar'}
          </button>
        </div>

        <p className="text-center">
          Já tem uma conta? <Link to="/auth/login">Faça login</Link>
        </p>
      </form>
    </div>
  );
}

export default Cadastro;