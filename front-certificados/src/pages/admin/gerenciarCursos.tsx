import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Table, Button, Modal, Form, Spinner, Alert, Card, Container } from 'react-bootstrap';


interface Curso {
  id: number;
  nome: string;
}

function GerenciarCursosPage() {
  const [cursos, setCursos] = useState<Curso[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [novoCursoNome, setNovoCursoNome] = useState('');
  const [isSaving, setIsSaving] = useState(false);

  const fetchCursos = async () => {
    setIsLoading(true);
    try {
      const token = localStorage.getItem('authToken');
      const config = { headers: { Authorization: `Bearer ${token}` } };
      const response = await axios.get('http://localhost:8080/api/cursos', config);
      setCursos(response.data);
    } catch (err) {
      setError('Não foi possível carregar os cursos');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCursos();
  }, []);

  const handleSalvar = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!novoCursoNome.trim()) {
      alert('o nome do curso não pode ser vazio');
      return;
    }
    setIsSaving(true);
    try {
      const token = localStorage.getItem('authToken');
      const config = { headers: { Authorization: `Bearer ${token}` } };
      const body = { nome: novoCursoNome };
      const response = await axios.post('http://localhost:8080/api/cursos', body, config);
      
      setCursos([...cursos, response.data]);
      
      //limpa e fecha o  modal
      setNovoCursoNome('');
      setShowModal(false);
    } catch (err) {
      alert('Erro ao salvar o curso');
    } finally {
      setIsSaving(false);
    }
  };

  const handleDeletar = async (id: number) => {
    if (window.confirm('deseja deletar este curso?')) {
      try {
        const token = localStorage.getItem('authToken');
        const config = { headers: { Authorization: `Bearer ${token}` } };
        await axios.delete(`http://localhost:8080/api/cursos/${id}`, config);
        
        setCursos(cursos.filter(curso => curso.id !== id));
      } catch (err) {
        alert('Erro ao deletar o curso.');
      }
    }
  };

  const renderContent = () => {
    if (isLoading) {
      return <div className="text-center"><Spinner animation="border" /> Carregando...</div>;
    }
    if (error) {
      return <Alert variant="danger">{error}</Alert>;
    }
    return (
      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome do Curso</th>
            <th className="text-center">Ações</th>
          </tr>
        </thead>
        <tbody>
          {cursos.length > 0 ? (
            cursos.map(curso => (
              <tr key={curso.id}>
                <td>{curso.id}</td>
                <td>{curso.nome}</td>
                <td className="text-center">
                  <Button variant="danger" size="sm" onClick={() => handleDeletar(curso.id)}>
                    Deletar
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={3} className="text-center">Nenhum curso cadastrado.</td>
            </tr>
          )}
        </tbody>
      </Table>
    );
  };

  return (
    <Container>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Gerenciar Cursos</h1>
        <Button variant="primary" onClick={() => setShowModal(true)}>
          Adicionar Novo Curso
        </Button>
      </div>
      <Card>
        <Card.Body>
          {renderContent()}
        </Card.Body>
      </Card>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Adicionar Novo Curso</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSalvar}>
          <Modal.Body>
            <Form.Group>
              <Form.Label>Nome do Curso</Form.Label>
              <Form.Control
                type="text"
                placeholder="Ex: Engenharia de Software"
                value={novoCursoNome}
                onChange={(e) => setNovoCursoNome(e.target.value)}
                required
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Cancelar
            </Button>
            <Button variant="primary" type="submit" disabled={isSaving}>
              {isSaving ? <Spinner as="span" animation="border" size="sm" /> : 'Salvar'}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </Container>
  );
}

export default GerenciarCursosPage;