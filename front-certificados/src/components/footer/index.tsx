import React from 'react';

function Footer() {
  return (
    <footer className="bg-light text-center text-muted py-3 border-top">
      <p className="mb-0">&copy; {new Date().getFullYear()} Maria Laura. Todos os direitos reservados.</p>
    </footer>
  );
}

export default Footer;