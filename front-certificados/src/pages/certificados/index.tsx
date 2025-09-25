import React, { useState } from 'react';
import axios from 'axios';

interface CertificadoRequestDto {
  nomeAluno: string;
  nomeCurso: string;
}

function CadastroCertificadoPage() {
  const [formData, setFormData] = useState<CertificadoRequestDto>({
    nomeAluno: '',
    nomeCurso: '',
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsLoading(true);
    setError('');
    setSuccess('');

    try {
      const token = localStorage.getItem('authToken');
      if (!token) {
        throw new Error('usuário não autenticado.');
      }
      const config = {
        headers: { Authorization: `Bearer ${token}` }
      };
      await axios.post('http://localhost:8080/api/certificados', formData, config);
      setSuccess('certificado cadastrado com sucesso!');
      setFormData({ nomeAluno: '', nomeCurso: '' });
    } catch (err) {
      console.error('erro ao cadastrar certificado:', err);
      setError('nãoo foi possível cadastrar o certificado. Tente novamente.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div>
      <h1>Cadastro de Certificado</h1>
      <p>Preencha os dados abaixo para emitir um novo certificado.</p>
      <div className="card">
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            {error && <div className="alert alert-danger">{error}</div>}
            {success && <div className="alert alert-success">{success}</div>}

            <div className="mb-3">
              <label htmlFor="nomeAluno" className="form-label">Nome do Aluno</label>
              <input
                type="text"
                id="nomeAluno"
                name="nomeAluno"
                className="form-control"
                value={formData.nomeAluno}
                onChange={handleChange}
                placeholder="Digite o nome completo do aluno"
                required
              />
            </div>

            <div className="mb-3">
              <label htmlFor="nomeCurso" className="form-label">Nome do Curso</label>
              <input
                type="text"
                id="nomeCurso"
                name="nomeCurso"
                className="form-control"
                value={formData.nomeCurso}
                onChange={handleChange}
                placeholder="Digite o nome do curso"
                required
              />
            </div>

            <button type="submit" className="btn btn-primary" disabled={isLoading}>
              {isLoading ? (
                <>
                  <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                  <span className="ms-2">Salvando...</span>
                </>
              ) : (
                'Salvar Certificado'
              )}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CadastroCertificadoPage;