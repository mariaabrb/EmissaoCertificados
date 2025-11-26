import React, { useState, useEffect } from 'react';
import { Form, Button, Spinner, Alert, Card } from 'react-bootstrap';
import api from '../../services/api';
import { useDispatch } from 'react-redux';
import { setPageTitle } from '../../redux/uiSlice';

interface Curso {
  id: number;
  nome: string;
}

interface Aluno {
  id: number;
  nome: string;
  email: string;
}

interface UsuarioResponseDto {
    id: number;
    nome: string;
    email: string;
    role: string;
}


function EmitirCertificadoPage() {
  const dispatch = useDispatch();
    useEffect(() => {
        dispatch(setPageTitle("Emitir Certificados"));
    }, [dispatch]);
  const [cursos, setCursos] = useState<Curso[]>([]);
  const [alunosDoCurso, setAlunosDoCurso] = useState<Aluno[]>([]);

  const [selectedCursoId, setSelectedCursoId] = useState('');
  const [selectedAlunoId, setSelectedAlunoId] = useState('');
  
  const [isLoadingCursos, setIsLoadingCursos] = useState(true);
  const [isLoadingAlunos, setIsLoadingAlunos] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

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
      setAlunosDoCurso([]);
      setSelectedAlunoId('');
      return; 
    }

    const fetchAlunosDoCurso = async () => {
      setIsLoadingAlunos(true);
      setError(null);
      setAlunosDoCurso([]);
      setSelectedAlunoId('');

      try {
        const response = await api.get(`/api/cursos/${selectedCursoId}/alunos`);
        const alunos = response.data.map((dto: UsuarioResponseDto): Aluno => ({ id: dto.id, nome: dto.nome, email: dto.email }));
        setAlunosDoCurso(alunos);
      } catch (err) {
        setError('Erro ao carregar alunos para este curso. Verifique se há alunos matriculados.');
      } finally {
        setIsLoadingAlunos(false);
      }
    };

    fetchAlunosDoCurso();
  }, [selectedCursoId]);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!selectedAlunoId || !selectedCursoId) {
      setError('Por favor, selecione um curso e um aluno.');
      return;
    }

    setIsSubmitting(true);
    setError(null);
    setSuccess(null);

    try {
      const body = {
        alunoId: parseInt(selectedAlunoId, 10),
        cursoId: parseInt(selectedCursoId, 10)
      };
      await api.post('/api/certificados', body);
      setSuccess('Certificado emitido com sucesso!');
      setSelectedAlunoId('');
    } catch (err) {
      setError('Não foi possível emitir o certificado.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <h1>Emissão de Certificado</h1>
      <Card>
        <Card.Body>
          <Form onSubmit={handleSubmit}>
            {error && <Alert variant="danger" onClose={() => setError(null)} dismissible>{error}</Alert>}
            {success && <Alert variant="success" onClose={() => setSuccess(null)} dismissible>{success}</Alert>}

            <Form.Group className="mb-3" controlId="cursoSelect">
              <Form.Label>1. Selecione o Curso</Form.Label>
              <Form.Select 
                value={selectedCursoId} 
                onChange={e => setSelectedCursoId(e.target.value)} 
                required
                disabled={isLoadingCursos || isSubmitting}
                aria-label="Selecionar curso"
              >
                <option value="">{isLoadingCursos ? 'Carregando...' : '-- Escolha um curso --'}</option>
                {cursos.map(curso => (
                  <option key={curso.id} value={curso.id}>{curso.nome}</option>
                ))}
              </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3" controlId="alunoSelect">
              <Form.Label>2. Selecione o Aluno Matriculado</Form.Label>
              <Form.Select 
                value={selectedAlunoId} 
                onChange={e => setSelectedAlunoId(e.target.value)} 
                required
                disabled={!selectedCursoId || isLoadingAlunos || isSubmitting} 
                aria-label="Selecionar aluno"
              >
                <option value="">
                  {isLoadingAlunos ? 'Carregando alunos...' : (selectedCursoId ? '-- Escolha um aluno --' : 'Selecione um curso primeiro')}
                </option>
                {alunosDoCurso.map(aluno => (
                  <option key={aluno.id} value={aluno.id}>{aluno.nome} ({aluno.email})</option> 
                ))}
              </Form.Select>
              {selectedCursoId && !isLoadingAlunos && alunosDoCurso.length === 0 && (
                <Form.Text className="text-muted">
                  Nenhum aluno matriculado neste curso. Matricule alunos na <Link to="/admin/matriculas">página de matrículas</Link>.
                </Form.Text>
              )}
            </Form.Group>

            <Button 
              type="submit" 
              variant="primary" 
              disabled={!selectedAlunoId || isSubmitting} 
            >
              {isSubmitting ? <Spinner as="span" animation="border" size="sm" /> : 'Emitir Certificado'}
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
}
import { Link } from 'react-router-dom'; 

export default EmitirCertificadoPage;