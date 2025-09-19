import { Link } from "react-router-dom";

function Header() {
  return (
    <header className="bg-dark py-3">
      <nav className="container d-flex justify-content-center gap-4">
        <Link to="/" className="text-white text-decoration-none fw-bold">Home</Link>
        <Link to="/usuarios" className="text-white text-decoration-none fw-bold">Usuários</Link>
        <Link to="/auth/login" className="text-white text-decoration-none fw-bold">Login</Link>
      </nav>
    </header>
  );
}

export default Header;
