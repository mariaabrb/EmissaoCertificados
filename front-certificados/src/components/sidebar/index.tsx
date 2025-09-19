import { Link } from "react-router-dom";

function Sidebar() {
    return (
      <div className="bg-dark vh-100 p-3" style={{ width: "250px" }}>
        <div className="mb-4 text-center">
          <img
            src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIHZpZXdCb3g9IjAgMCA2NCA2NCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjY0IiBoZWlnaHQ9IjY0IiByeD0iMTIiIGZpbGw9IiM2ZjQyYzEiLz48L3N2Zz4="
            alt="logo"
            className="img-fluid"
            style={{ maxHeight: "60px", cursor: "pointer" }}
          />
        </div>
  
        <ul className="list-unstyled">
          <li className="mb-2">
            <a href="/" className="text-decoration-none text-white fw-semibold d-block py-2 px-3 rounded">
              Home
            </a>
          </li>
  
          <li>
            <a
              href="#submenucadastro"
              className="text-decoration-none text-white fw-semibold d-block py-2 px-3 rounded"
              data-bs-toggle="collapse"
              aria-expanded="false"
              aria-controls="submenucadastro"
            >
              Cadastro
            </a>
            <ul className="collapse list-unstyled ps-3 mt-2" id="submenucadastro">
              <li className="mb-2">
                <Link to="/usuario" className="nav-link text-white">Usuário</Link>
              </li>
              <li>
                <Link to="/carrinho" className="nav-link text-white">Carrinho</Link>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    );
  }
  
  export default Sidebar;
  