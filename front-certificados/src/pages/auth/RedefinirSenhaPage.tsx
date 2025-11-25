import React, { useState, useEffect } from 'react';
import { Container, Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import api from '../../services/api';

function RedefinirSenhaPage() {
  const navigate = useNavigate();
  const location = useLocation();
  
  const [formData, setFormData] = useState({
    email: location.state?.email || '', 
    code: '',
    novaSenha: '',
    confirmarSenha: ''
  });

  const [isLoading, setIsLoading] = useState(false);
  const [message, setMessage] = useState<{ type: 'success' | 'danger', text: string } | null>(null);

  useEffect(() => {
    if (location.state?.email) {
      setFormData(prev => ({ ...prev, email: location.state.email }));
    }
  }, [location.state]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);

    if (formData.novaSenha !== formData.confirmarSenha) {
      setMessage({ type: 'danger', text: 'As senhas não coincidem.' });
      return;
    }

    if (formData.novaSenha.length < 6) {
      setMessage({ type: 'danger', text: 'A senha deve ter pelo menos 6 caracteres.' });
      return;
    }

    setIsLoading(true);

    try {
      await api.post('/auth/redefinir-senha', {
        email: formData.email,
        code: formData.code,
        novaSenha: formData.novaSenha 
      });

      setMessage({ type: 'success', text: 'Senha redefinida com sucesso! Redirecionando...' });
      
      setTimeout(() => {
        navigate('/auth/login');
      }, 3000);

    } catch (err: any) {
      console.error(err);
      const errorMsg = err.response?.data || 'Erro ao redefinir senha. Verifique os dados.';
      setMessage({ type: 'danger', text: errorMsg });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
      <Card style={{ width: '100%', maxWidth: '400px' }} className="shadow-sm">
        <Card.Body className="p-4">
          <h2 className="text-center mb-4">Criar Nova Senha</h2>
          
          {message && <Alert variant={message.type}>{message.text}</Alert>}

          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>E-mail</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
                placeholder="Confirme seu e-mail"
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Código de Verificação</Form.Label>
              <Form.Control
                type="text"
                name="code"
                value={formData.code}
                onChange={handleChange}
                required
                placeholder="Ex: 123456"
              />
              <Form.Text className="text-muted">
                Insira o código que chegou no seu e-mail.
              </Form.Text>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Nova Senha</Form.Label>
              <Form.Control
                type="password"
                name="novaSenha"
                value={formData.novaSenha}
                onChange={handleChange}
                required
                placeholder="Mínimo 6 caracteres"
              />
            </Form.Group>

            <Form.Group className="mb-4">
              <Form.Label>Confirmar Nova Senha</Form.Label>
              <Form.Control
                type="password"
                name="confirmarSenha"
                value={formData.confirmarSenha}
                onChange={handleChange}
                required
                placeholder="Repita a senha"
              />
            </Form.Group>

            <div className="d-grid gap-2">
              <Button variant="primary" type="submit" disabled={isLoading}>
                {isLoading ? <Spinner animation="border" size="sm" /> : 'Redefinir Senha'}
              </Button>
            </div>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
}

export default RedefinirSenhaPage;