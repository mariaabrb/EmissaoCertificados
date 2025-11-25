import React, { useState, useEffect } from 'react';
import api from '../../services/api'; 
import { Table, Button, Modal, Form, Spinner, Alert, Card, Container } from 'react-bootstrap';
import { FaEdit, FaTrash } from 'react-icons/fa'; // Importe os ícones

interface Curso {
  id: number;
  nome: string;
}

function GerenciarCursosPage() {
  const [cursos, setCursos] = useState<Curso[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  const [showModal, setShowModal] = useState(false);
  const [nomeCurso, setNomeCurso] = useState('');
  
  // Estado para saber se estamos editando (se null, é criação)
  const [editingCursoId, setEditingCursoId] = useState<number | null>(null); 
  
  const [isSaving, setIsSaving] = useState(false);

  const fetchCursos = async () => {
    setIsLoading(true);
    setError(null); 
    try {
      const response = await api.get('/api/cursos');
      setCursos(response.data);
    } catch (err) {
      console.error(err);
      setError('Não foi possível carregar os cursos.');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCursos();
  }, []);

  // --- PREPARAR PARA CRIAR ---
  const handleOpenCreate = () => {
      setEditingCursoId(null); // Limpa o ID (Modo Criação)
      setNomeCurso('');
      setShowModal(true);
  }

  // --- PREPARAR PARA EDITAR ---
  const handleOpenEdit = (curso: Curso) => {
      setEditingCursoId(curso.id); // Seta o ID (Modo Edição)
      setNomeCurso(curso.nome); // Preenche o campo
      setShowModal(true);
  }

  const handleSalvar = async (event: React.FormEvent) => {
    event.preventDefault();
    if (!nomeCurso.trim()) {
      alert('O nome do curso não pode ser vazio.');
      return;
    }
    setIsSaving(true);
    
    try {
      if (editingCursoId) {
          // --- MODO EDIÇÃO (PUT) ---
          await api.put(`/api/cursos/${editingCursoId}`, { nome: nomeCurso });
          
          // Atualiza a lista localmente
          setCursos(cursos.map(c => c.id === editingCursoId ? { ...c, nome: nomeCurso } : c));
      } else {
          // --- MODO CRIAÇÃO (POST) ---
          const response = await api.post('/api/cursos', { nome: nomeCurso });
          setCursos([...cursos, response.data]);
      }
      
      setShowModal(false);
      setNomeCurso('');
      setEditingCursoId(null);
    } catch (err) {
      console.error(err);
      alert('Erro ao salvar o curso.');
    } finally {
      setIsSaving(false);
    }
  };

  const handleDeletar = async (id: number) => {
    if (window.confirm('Deseja realmente deletar este curso?')) {
      try {
        await api.delete(`/api/cursos/${id}`);
        setCursos(cursos.filter(curso => curso.id !== id));
      } catch (err) {
        alert('Erro ao deletar. Verifique se há alunos matriculados.');
      }
    }
  };

  const renderContent = () => {
    if (isLoading) return <div className="text-center py-5"><Spinner animation="border" /></div>;
    if (error) return <Alert variant="danger">{error}</Alert>;
    
    return (
      <Table striped bordered hover responsive className="mb-0">
        <thead className="bg-light">
          <tr>
            <th>ID</th>
            <th>Nome do Curso</th>
            <th className="text-center" style={{ width: '180px' }}>Ações</th>
          </tr>
        </thead>
        <tbody>
          {cursos.length > 0 ? (
            cursos.map(curso => (
              <tr key={curso.id} className="align-middle">
                <td>{curso.id}</td>
                <td>{curso.nome}</td>
                <td className="text-center">
                  {/* BOTÃO EDITAR */}
                  <Button variant="outline-primary" size="sm" className="me-2" onClick={() => handleOpenEdit(curso)}>
                    <FaEdit />
                  </Button>
                  {/* BOTÃO DELETAR */}
                  <Button variant="outline-danger" size="sm" onClick={() => handleDeletar(curso.id)}>
                    <FaTrash />
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr><td colSpan={3} className="text-center py-4 text-muted">Nenhum curso cadastrado.</td></tr>
          )}
        </tbody>
      </Table>
    );
  };

  return (
    <Container fluid>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h1 className="mb-0">Gerenciar Cursos</h1>
            <p className="text-muted mb-0">Crie, edite e gerencie os cursos.</p>
        </div>
        <Button variant="primary" onClick={handleOpenCreate}>
          + Novo Curso
        </Button>
      </div>
      
      <Card className="shadow-sm">
        <Card.Body className="p-0">
          {renderContent()}
        </Card.Body>
      </Card>

      <Modal show={showModal} onHide={() => setShowModal(false)} centered>
        <Modal.Header closeButton>
          {/* Título dinâmico */}
          <Modal.Title>{editingCursoId ? 'Editar Curso' : 'Adicionar Novo Curso'}</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSalvar}>
          <Modal.Body>
            <Form.Group controlId="formCursoNome">
              <Form.Label>Nome do Curso</Form.Label>
              <Form.Control
                type="text"
                placeholder="Ex: Engenharia de Software"
                value={nomeCurso}
                onChange={(e) => setNomeCurso(e.target.value)}
                required
                autoFocus
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)} disabled={isSaving}>
              Cancelar
            </Button>
            <Button variant="primary" type="submit" disabled={isSaving}>
              {isSaving ? <Spinner size="sm" animation="border"/> : (editingCursoId ? 'Salvar Alterações' : 'Criar Curso')}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </Container>
  );
}

export default GerenciarCursosPage;