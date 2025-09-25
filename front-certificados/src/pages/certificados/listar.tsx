import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { Table, Spinner, Alert, Button } from 'react-bootstrap';

interface Certificado {
  id: number;
  nomeAluno: string;
  nomeCurso: string;
  codValidacao: string;
}

function ListarCertificadosPage() {
  const [certificados, setCertificados] = useState<Certificado[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const buscarCertificados = async () => {
      try {
        const token = localStorage.getItem('authToken');
        if (!token) {
          throw new Error('Usuário não autenticado. Faça o login novamente.');
        }
        const config = {
          headers: { Authorization: `Bearer ${token}` }
        };
        const response = await axios.get('http://localhost:8080/api/certificados', config);
        setCertificados(response.data);
      } catch (err) {
        console.error("Erro ao buscar certificados:", err);
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
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Meus Certificados</h1>
        <Link to="/certificados/novo">
          <Button variant="primary">Novo Certificado</Button>
        </Link>
      </div>

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>Nome do Aluno</th>
            <th>Nome do Curso</th>
          </tr>
        </thead>
        <tbody>
          {certificados.length > 0 ? (
            certificados.map(cert => (
              <tr key={cert.id}>
                <td>{cert.nomeAluno}</td>
                <td>{cert.nomeCurso}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={3} className="text-center">Você ainda não cadastrou nenhum certificado :(</td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
}

export default ListarCertificadosPage;