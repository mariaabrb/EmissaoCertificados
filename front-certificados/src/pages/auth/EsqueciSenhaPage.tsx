import React, { useState } from 'react';
import { Container, Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import api from '../../services/api';

function EsqueciSenhaPage() {
  const [email, setEmail] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [message, setMessage] = useState<{ type: 'success' | 'danger', text: string } | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setMessage(null);

    try {
      await api.post('/auth/esqueci-senha', { email });
      setMessage({ type: 'success', text: 'Se o e-mail existir, você receberá um código.' });
      setTimeout(() => {
        navigate('/auth/redefinir-senha', { state: { email } });
      }, 2500);
    } catch (err) {
        console.log
      setMessage({ type: 'danger', text: 'Erro ao solicitar código.' });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
      <Card style={{ width: '100%', maxWidth: '400px' }}>
        <Card.Body className="p-4">
          <h2 className="text-center mb-4">Recuperar Senha</h2>
          {message && <Alert variant={message.type}>{message.text}</Alert>}
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>E-mail</Form.Label>
              <Form.Control type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </Form.Group>
            <Button className="w-100" variant="primary" type="submit" disabled={isLoading}>
              {isLoading ? <Spinner size="sm" /> : 'Enviar Código'}
            </Button>
            <div className="text-center mt-3">
                <Link to="/auth/login">Voltar</Link>
            </div>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
}

export default EsqueciSenhaPage;