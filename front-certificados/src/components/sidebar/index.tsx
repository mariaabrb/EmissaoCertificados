import { Link } from "react-router-dom";

function Sidebar() {
    return (
      <div className="bg-dark vh-100 p-3" style={{ width: "250px" }}>
        <ul className="list-unstyled">
          <li className="mb-2">
            <Link to="/" className="text-decoration-none text-white fw-semibold d-block py-2 px-3 rounded">
              Home
            </Link>
          </li>
          <li>
            <a
              href="#submenucertificados"
              className="text-decoration-none text-white fw-semibold d-block py-2 px-3 rounded"
              data-bs-toggle="collapse"
            >
              Certificados
            </a>
            <ul className="collapse list-unstyled ps-3 mt-2" id="submenucertificados">
              <li className="mb-2">
                <Link to="/certificados/novo" className="nav-link text-white">Cadastrar Novo</Link>
              </li>
              <li>
                <Link to="/certificados" className="nav-link text-white">Meus Certificados</Link>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    );
  }
  
  export default Sidebar;