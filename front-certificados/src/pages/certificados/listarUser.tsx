import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Table, Spinner, Alert } from 'react-bootstrap';

interface Certificado {
  id: number;
  nomeAluno: string;
  curso: {
    id: number;
    nome: string;
  };
  codValidacao: string;
}

function ListarCertificadosPage() {
  const [certificados, setCertificados] = useState<Certificado[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const buscarCertificados = async () => {
      try {
        const token = localStorage.getItem('authToken');
        if (!token) throw new Error('Usuário não autenticado.');
        
        const config = { headers: { Authorization: `Bearer ${token}` } };
        const response = await axios.get('http://localhost:8080/api/certificados', config);
        setCertificados(response.data);
      } catch (err) {
        setError("Não foi possível carregar os certificados.");
      } finally {
        setIsLoading(false);
      }
    };
    buscarCertificados();
  }, []);

  if (isLoading) {
    return <div className="text-center mt-5"><Spinner animation="border" /> Carregando...</div>;
  }

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  return (
    <div>
      <h1 className="mb-4">Meus Certificados</h1>

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>Nome do Aluno</th>
            <th>Curso</th>
            <th>Código de Validação</th>
          </tr>
        </thead>
        <tbody>
          {certificados.length > 0 ? (
            certificados.map(cert => (
              <tr key={cert.id}>
                <td>{cert.nomeAluno}</td>
                <td>{cert.curso.nome}</td>
                <td>{cert.codValidacao}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={3} className="text-center">Você ainda não possui certificados :(</td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
}

export default ListarCertificadosPage;