import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Navbar, Container, Nav, NavDropdown } from 'react-bootstrap';
import { useSelector, useDispatch } from 'react-redux';
import { logout } from '../../redux/authSlice';
import type { RootState } from '../../redux/store';

function Header() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const isAutenticado = useSelector((state: RootState) => state.auth.isAutenticado);
  const usuario = useSelector((state: RootState) => state.auth.usuario);
  
  const pageTitle = useSelector((state: RootState) => state.ui.pageTitle);

  const handleLogout = () => {
    dispatch(logout());
    navigate('/auth/login');
  };

  const displayName = usuario?.nome || usuario?.email || null;

  return (
    <Navbar bg="light" expand="lg" className="shadow-sm">
      <Container fluid>
        <Navbar.Brand as={Link} to={isAutenticado ? "/meus-certificados" : "/"} className="fw-bold fs-4" style={{ fontFamily: 'Montserrat, sans-serif' }}>
          {pageTitle} 
        </Navbar.Brand>

        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto">
            {isAutenticado && displayName ? (
              <NavDropdown title={`Olá, ${displayName}`} id="basic-nav-dropdown" align="end">
                <NavDropdown.Item onClick={handleLogout}>Sair</NavDropdown.Item>
              </NavDropdown>
            ) : (
              <Nav.Link as={Link} to="/auth/login">Login</Nav.Link>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default Header;