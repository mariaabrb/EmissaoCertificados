import React, { useState, useEffect } from 'react';
import api from '../../services/api'; 
import { Link } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { setPageTitle } from '../../redux/uiSlice';
import { Table, Spinner, Alert, Button } from 'react-bootstrap';


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
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Meus Certificados</h1>
      </div>

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>Nome do Aluno</th>
            <th>Nome do Curso</th>
            <th>Instituição</th>
            <th>Código</th>
          </tr>
        </thead>
        <tbody>
          {certificados.length > 0 ? (
            certificados.map(cert => (
              <tr key={cert.id}>
                <td>{cert.nomeAluno}</td>
                <td>{cert.nomeCurso}</td>
                <td>{cert.nomeInstituicao}</td>
                <td>{cert.codValidacao}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={4} className="text-center">Você ainda não possui certificados :(</td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
}

export default ListarCertificadosPage;