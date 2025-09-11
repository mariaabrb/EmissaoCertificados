function Header() {
    return (
      <header className="bg-dark py-3">
        <nav className="container d-flex justify-content-center gap-4">
          <a href="/" className="text-white text-decoration-none fw-bold">Home</a>
          <a href="/usuarios" className="text-white text-decoration-none fw-bold">Usuários</a>
        </nav>
      </header>
    );
  }
  
  export default Header;
  