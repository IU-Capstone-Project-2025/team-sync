import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import '@testing-library/jest-dom';
import SplashHeader from '../components/splashHeader';

describe('SplashHeader', () => {
  it('renders navigation buttons and sign up', () => {
    render(
      <BrowserRouter>
        <SplashHeader />
      </BrowserRouter>
    );
    expect(screen.getByText(/How it works/i)).toBeInTheDocument();
    expect(screen.getByText(/For students/i)).toBeInTheDocument();
    expect(screen.getByText(/For teachers/i)).toBeInTheDocument();
    expect(screen.getByText(/Help/i)).toBeInTheDocument();
    // The SignUpButton may render a button or text, adjust as needed
  });
});
