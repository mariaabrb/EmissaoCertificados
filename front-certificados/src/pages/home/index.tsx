
import { Link } from 'react-router-dom';
import { Container, Row, Col, Button, Card, Navbar, Nav } from 'react-bootstrap';
import './home.css'; 
const PublicHeader = () => (

  <Navbar bg="transparent" expand="lg" variant="light" className="py-3">
    <Container>
      <Navbar.Brand as={Link} to="/" className="fw-bold fs-4" style={{ fontFamily: 'Montserrat, sans-serif' }}>
        Certify Pro
      </Navbar.Brand>
      <Nav className="ms-auto">
        <Link to="/auth/login">
          <Button variant="primary" className="me-2">Entrar</Button>
        </Link>
        <Link to="/auth/cadastro">
          <Button variant="outline-primary">Cadastre-se</Button>
        </Link>
      </Nav>
    </Container>
  </Navbar>
);

const PublicFooter = () => (
  <footer className="text-center text-muted py-3">
    <p className="mb-0">&copy; {new Date().getFullYear()} Maria Laura. Todos os direitos reservados.</p>
  </footer>
);


function Home() {
    return (
        <div className="home-container d-flex flex-column">
            <PublicHeader />
            
            <Container as="main" className="my-auto py-5"> 
                <Row className="justify-content-center text-center">
                    <Col lg={8}>
                        <div className="hero-section p-5 mb-5 shadow-sm">
                            <h1 className="display-4 fw-bold text-dark mb-3">
                                Certify Pro
                            </h1>
                            <p className="lead text-secondary mb-4">
                                A plataforma definitiva para criar, gerenciar e validar seus certificados digitais com fluidez e segurança.
                            </p>
            
                        </div>
                    </Col>
                </Row>


                <Row className="text-center">
                    <Col md={4} className="mb-4">
                        <Card className="feature-card shadow-sm h-100">
                            <Card.Body className="p-4">
                                <Card.Title as="h3" className="mb-3">Validação Rápida</Card.Title>
                                <Card.Text className="text-secondary">
                                    Qualquer pessoa pode verificar a autenticidade de um certificado em segundos usando um código de validação único.
                                </Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={4} className="mb-4">
                        <Card className="feature-card shadow-sm h-100">
                            <Card.Body className="p-4">
                                <Card.Title as="h3" className="mb-3">Seu Portfólio Digital</Card.Title>
                                <Card.Text className="text-secondary">
                                    Acesse os seus certificados emitidos em um só lugar, prontos para compartilhar e impulsionar sua carreira.
                                </Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={4} className="mb-4">
                        <Card className="feature-card shadow-sm h-100">
                            <Card.Body className="p-4">
                                <Card.Title as="h3" className="mb-3">Gestão para Instituições</Card.Title>
                                <Card.Text className="text-secondary">
                                    Um painel de controle completo para emitir e gerenciar certificados com total controle e organização.
                                </Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>

            <PublicFooter />
        </div>
    );
}

export default Home;