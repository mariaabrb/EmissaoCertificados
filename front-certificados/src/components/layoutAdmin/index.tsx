import { Outlet } from "react-router-dom";
import Footer from "../footer/index";
import Header from "../header/index";
import Sidebar from "../sidebar/index";

function LayoutAdmin() {
  return (
    <div className="d-flex flex-column vh-100">
      <Header />
      <div className="d-flex flex-grow-1 overflow-hidden">
        <Sidebar />
        <main className="flex-grow-1 p-4 overflow-auto">
          <Outlet />
        </main>
      </div>
      <Footer />
    </div>
  );
}

export default LayoutAdmin;
