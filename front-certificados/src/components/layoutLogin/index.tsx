import React from "react";
import { Outlet } from "react-router-dom";

function LayoutLogin() {
  return (
    <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
      <div className="bg-white p-4 rounded shadow" style={{ minWidth: "320px", maxWidth: "400px", width: "100%" }}>
        <Outlet />
      </div>
    </div>
  );
}

export default LayoutLogin;
