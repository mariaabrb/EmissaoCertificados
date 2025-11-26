import React, { useState, useEffect } from 'react';
import { Container, Card, Table, Spinner, Alert, Badge } from 'react-bootstrap';
import api from '../../services/api'; 
import { useDispatch } from 'react-redux';
import { setPageTitle } from '../../redux/uiSlice';

interface Usuario {
    id: number;
    nome: string;
    email: string;
    role: string;
    nomeInstituicao: string;
}

function GerenciarUsuariosPage() {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(setPageTitle("Gerenciar Usuários"));
    }, [dispatch]);
    const [usuarios, setUsuarios] = useState<Usuario[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchUsuarios = async () => {
            setIsLoading(true);
            setError(null);
            try {
                const response = await api.get('api/usuarios'); 
                setUsuarios(response.data);
            } catch (err: any) {
                console.error(err);
                setError('Erro ao carregar a lista de usuários.');
            } finally {
                setIsLoading(false);
            }
        };
        fetchUsuarios();
    }, []);

    const formatRole = (role: string) => {
        if (role.includes('ROLE_ADMIN')) return 'Administrador';
        if (role.includes('ROLE_USER')) return 'Aluno';
        return role;
    };

    const getRoleVariant = (role: string) => {
        if (role.includes('ROLE_ADMIN')) return 'dark';
        return 'primary';
    };

    if (isLoading) {
        return (
            <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '50vh' }}>
                <Spinner animation="border" variant="primary" /> 
                <span className="ms-3">Carregando Usuários...</span>
            </Container>
        );
    }

    return (
        <Container fluid>
            <h1 className="mb-4">Gerenciar Usuários</h1>
            <p className="text-muted">Visualize os alunos e administradores da sua instituição.</p>

            {error && <Alert variant="danger">{error}</Alert>}

            <Card className="shadow-sm">
                <Card.Header className="bg-white fw-bold">
                    Lista de Usuários
                </Card.Header>
                <Card.Body>
                    <Table striped bordered hover responsive className="mb-0">
                        <thead className="bg-light">
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>E-mail</th>
                                <th>Perfil</th>
                                <th>Instituição</th>
                            </tr>
                        </thead>
                        <tbody>
                            {usuarios.length > 0 ? usuarios.map(user => (
                                <tr key={user.id} className="align-middle">
                                    <td>{user.id}</td>
                                    <td>{user.nome}</td>
                                    <td>{user.email}</td>
                                    <td>
                                        <Badge bg={getRoleVariant(user.role)}>
                                            {formatRole(user.role)}
                                        </Badge>
                                    </td>
                                    <td>{user.nomeInstituicao || '-'}</td>
                                </tr>
                            )) : (
                                <tr>
                                    <td colSpan={5} className="text-center py-4 text-muted">
                                        Nenhum usuário encontrado.
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
        </Container>
    );
}
export default GerenciarUsuariosPage;