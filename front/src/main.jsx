import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom';
import './index.css'
import App from './App.jsx'
import { PublicClientApplication } from '@azure/msal-browser'
import { msalConfig } from './authConfig.js'
import { MsalProvider } from '@azure/msal-react'

const msalInstance = new PublicClientApplication(msalConfig);

createRoot(document.getElementById('root')).render(
  <BrowserRouter>
  <MsalProvider instance={msalInstance}>
  <StrictMode>
    <App />
  </StrictMode>
  </MsalProvider>
  </BrowserRouter>
);
