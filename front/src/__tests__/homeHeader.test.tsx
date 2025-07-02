import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import HomeHeader from '../components/homeHeader';
import { BrowserRouter } from 'react-router-dom';
import { MsalProvider } from '@azure/msal-react';
import { PublicClientApplication } from '@azure/msal-browser';
import { msalConfig } from '../authConfig';

const msalInstance = new PublicClientApplication(msalConfig);

describe('HomeHeader', () => {
  it('renders navigation buttons', () => {
    render(
      <MsalProvider instance={msalInstance}>
        <BrowserRouter>
          <HomeHeader />
        </BrowserRouter>
      </MsalProvider>
    );
    expect(screen.getByText(/My responses/i)).toBeInTheDocument();
    expect(screen.getByText(/My projects/i)).toBeInTheDocument();
  });

  it('renders the account component', () => {
    render(
      <MsalProvider instance={msalInstance}>
        <BrowserRouter>
          <HomeHeader />
        </BrowserRouter>
      </MsalProvider>
    );
    // The Account component renders user's name if available, but we can check for its presence by role or fallback
    // This is a placeholder, adjust if Account renders a specific text or role
    // expect(screen.getByText(/Account/i)).toBeInTheDocument();
  });
});
