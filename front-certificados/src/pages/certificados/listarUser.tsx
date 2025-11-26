import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { Link } from 'react-router-dom';
import { Table, Spinner, Alert, Button, Badge } from 'react-bootstrap';
import { useDispatch } from 'react-redux';
import { setPageTitle } from '../../redux/uiSlice';

interface Certificado {
  id: number;
  nomeAluno: string;
  nomeCurso: string;
  nomeInstituicao: string;
  codValidacao: string;
  dataEmissao: string;
}

function ListarCertificadosPage() {
   const dispatch = useDispatch();
  useEffect(() => {
        dispatch(setPageTitle("Meus Certificados"));
    }, [dispatch]);
  const [certificados, setCertificados] = useState<Certificado[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const buscarCertificados = async () => {
      setIsLoading(true);
      setError('');
      try {
        const response = await api.get('/api/certificados');

        if (Array.isArray(response.data)) {
          setCertificados(response.data);
        } else {
          setCertificados([]);
        }
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
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Meus Certificados</h1>
      </div>

      <div className="shadow-sm p-3 mb-5 bg-white rounded">
        <Table striped bordered hover responsive>
          <thead className="bg-light">
            <tr>
              <th>Curso</th>
              <th>Instituição</th>
              <th>Aluno</th>
              <th>Código de Validação</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {certificados.length > 0 ? (
              certificados.map(cert => (
                <tr key={cert.id}>
                  <td className="fw-bold">{cert.nomeCurso}</td>
                  <td>{cert.nomeInstituicao}</td>
                  <td>{cert.nomeAluno}</td>
                  <td>
                    <Badge bg="secondary" className="p-2" style={{ fontFamily: 'monospace' }}>
                      {cert.codValidacao}
                    </Badge>
                  </td>
                  <td>
                    <Link to={`/validar-certificado/${cert.codValidacao}`} target="_blank">
                      <Button variant="outline-primary" size="sm">Ver</Button>
                    </Link>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={5} className="text-center py-5 text-muted">
                  Você ainda não possui certificados emitidos.
                </td>
              </tr>
            )}
          </tbody>
        </Table>
      </div>
    </div>
  );
}

export default ListarCertificadosPage;