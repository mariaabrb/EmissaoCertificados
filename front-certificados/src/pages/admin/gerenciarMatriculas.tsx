import React, { useState, useEffect } from 'react';
import { Container, Card, Form, ListGroup, Button, Spinner, Alert, Badge } from 'react-bootstrap'; // Adicionado Badge
import api from '../../services/api';

interface Curso {
  id: number;
  nome: string;
}

interface Usuario {
  id: number;
  nome: string;
  email: string;
  role: string;
}

interface UsuarioResponseDto {
    id: number;
    nome: string;
    email: string;
    role: string;
}

function GerenciarMatriculasPage() {
  const [cursos, setCursos] = useState<Curso[]>([]);
  const [selectedCursoId, setSelectedCursoId] = useState<string>('');
  const [todosUsuarios, setTodosUsuarios] = useState<Usuario[]>([]);
  const [idsAlunosMatriculados, setIdsAlunosMatriculados] = useState<Set<number>>(new Set()); 
  
  const [isLoadingCursos, setIsLoadingCursos] = useState(true);
  const [isLoadingUsuarios, setIsLoadingUsuarios] = useState(false);
  const [isLoadingAction, setIsLoadingAction] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchCursos = async () => {
      setIsLoadingCursos(true);
      setError(null);
      try {
        const response = await api.get('/api/cursos');
        setCursos(response.data);
      } catch (err) {
        setError('Erro ao carregar a lista de cursos.');
      } finally {
        setIsLoadingCursos(false);
      }
    };
    fetchCursos();
  }, []);

  useEffect(() => {
    if (!selectedCursoId) {
      setTodosUsuarios([]);
      setIdsAlunosMatriculados(new Set());
      return;
    }

    const fetchDadosCurso = async () => {
      setIsLoadingUsuarios(true);
      setError(null);
      setIdsAlunosMatriculados(new Set());
      
     try {
        const [alunosRes, usuariosRes] = await Promise.all([
          api.get(`/api/cursos/${selectedCursoId}/alunos`),
          api.get('/api/usuarios')
        ]);

        const alunosData: UsuarioResponseDto[] = Array.isArray(alunosRes.data) ? alunosRes.data : [];
        const matriculadosIdArray: number[] = alunosData.map((dto: UsuarioResponseDto): number => dto.id);
        const matriculadosIdsSet = new Set(matriculadosIdArray);

        setIdsAlunosMatriculados(matriculadosIdsSet);
     
        const usuariosData: UsuarioResponseDto[] = Array.isArray(usuariosRes.data) ? usuariosRes.data : [];
        const users = usuariosData
            .map((dto: UsuarioResponseDto): Usuario => ({ id: dto.id, nome: dto.nome, email: dto.email, role: dto.role }))
            .filter((user: Usuario) => user.role === 'USER');
        setTodosUsuarios(users);

      } catch (err) {
        setError('Erro ao carregar dados dos alunos.');
      } finally {
        setIsLoadingUsuarios(false);
      }
    };

    fetchDadosCurso();
  }, [selectedCursoId]);

  const handleMatricular = async (alunoId: number) => {
    if (!selectedCursoId) return;
    setIsLoadingAction(alunoId);
    setError(null);
    try {
      await api.post(`/api/cursos/${selectedCursoId}/alunos/${alunoId}`);
      setIdsAlunosMatriculados(prevIds => new Set(prevIds).add(alunoId));
    } catch (err) {
      setError('Erro ao matricular aluno.');
    } finally {
      setIsLoadingAction(null);
    }
  };

  const handleDesmatricular = async (alunoId: number) => {
    if (!selectedCursoId) return;
    setIsLoadingAction(alunoId);
    setError(null);
    try {
      await api.delete(`/api/cursos/${selectedCursoId}/alunos/${alunoId}`);
      setIdsAlunosMatriculados(prevIds => {
          const newIds = new Set(prevIds);
          newIds.delete(alunoId);
          return newIds;
      });
    } catch (err) {
      setError('Erro ao desmatricular aluno.');
    } finally {
      setIsLoadingAction(null);
    }
  };

  return (
    <Container fluid>
      <h1>Gerenciar Matrículas</h1>
      <p>Associe usuários (alunos) aos cursos disponíveis.</p>

      {error && <Alert variant="danger" onClose={() => setError(null)} dismissible>{error}</Alert>}

      <Form.Group className="mb-4">
        <Form.Label>Selecione um Curso:</Form.Label>
        <Form.Select 
          value={selectedCursoId} 
          onChange={(e) => setSelectedCursoId(e.target.value)}
          disabled={isLoadingCursos}
          aria-label="Selecionar curso"
        >
          <option value="">{isLoadingCursos ? 'Carregando cursos...' : '-- Escolha um curso --'}</option>
          {cursos.map(curso => (
            <option key={curso.id} value={curso.id}>{curso.nome}</option>
          ))}
        </Form.Select>
      </Form.Group>

      {selectedCursoId && (
        <Card>
          <Card.Header as="h5">Alunos</Card.Header>
          <Card.Body style={{ maxHeight: '60vh', overflowY: 'auto' }}>
            {isLoadingUsuarios ? (
              <div className="d-flex justify-content-center align-items-center h-100 py-5">
                <Spinner animation="border" variant="primary" /> Carregando alunos...
              </div>
            ) : (
              <ListGroup variant="flush">
                {todosUsuarios.length > 0 ? todosUsuarios.map(user => {
                  const isMatriculado = idsAlunosMatriculados.has(user.id);
                  
                  return (
                    <ListGroup.Item key={user.id} className="d-flex justify-content-between align-items-center flex-wrap gap-2">
                      <div>
                        <strong>{user.nome}</strong> <br />
                        <small className="text-muted">{user.email}</small>
                      </div>
                      <div>
                        {isMatriculado ? (
                          <Badge bg="success" className="me-2">Matriculado</Badge>
                        ) : (
                          <Badge bg="secondary" className="me-2">Não Matriculado</Badge>
                        )}
                        <Button
                          variant={isMatriculado ? "outline-danger" : "outline-success"}
                          size="sm"
                          onClick={() => isMatriculado ? handleDesmatricular(user.id) : handleMatricular(user.id)}
                          disabled={isLoadingAction === user.id}
                          style={{ minWidth: '110px' }}
                        >
                          {isLoadingAction === user.id ? (
                            <Spinner animation="border" size="sm" />
                          ) : (
                            isMatriculado ? 'Remover' : 'Matricular'
                          )}
                        </Button>
                      </div>
                    </ListGroup.Item>
                  );
                }) : (
                  <div className="text-muted text-center py-5">Nenhum usuário (com perfil 'USER') encontrado no sistema.</div>
                )}
              </ListGroup>
            )}
          </Card.Body>
        </Card>
      )}
    </Container>
  );
}

export default GerenciarMatriculasPage;