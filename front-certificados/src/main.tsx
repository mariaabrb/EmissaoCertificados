import React from 'react';
import ReactDOM from 'react-dom/client';
import AppRoutes from './route';
import { BrowserRouter } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';
import { Provider } from 'react-redux';
import { store } from './redux/store';

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

root.render(
  <React.StrictMode>
    <Provider store={store}>
    <BrowserRouter>
      <AppRoutes />
    </BrowserRouter>
    </Provider>
  </React.StrictMode>
);