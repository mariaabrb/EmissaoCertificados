import React from "react";

function Cadastro() {
  return (
    <>
      <h2 className="mb-4 text-center">Cadastre-se</h2>
      <form>
        <div className="mb-3">
          <label htmlFor="nome" className="form-label">Nome</label>
          <input type="text" className="form-control" id="nome" placeholder="Seu nome" required />
        </div>
        <div className="mb-3">
          <label htmlFor="email" className="form-label">E-mail</label>
          <input type="email" className="form-control" id="email" placeholder="seu@email.com" required />
        </div>
        <div className="mb-3">
          <label htmlFor="senha" className="form-label">Senha</label>
          <input type="password" className="form-control" id="senha" placeholder="********" required />
        </div>
        <div className="d-grid">
          <button type="submit" className="btn btn-primary">Cadastrar</button>
        </div>
      </form>
    </>
  );
}

export default Cadastro;
