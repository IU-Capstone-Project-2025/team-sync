import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import Footer from '../components/footer';

describe('Footer', () => {
  it('renders copyright and help', () => {
    render(<Footer />);
    expect(screen.getByText(/Innopolis University/i)).toBeInTheDocument();
    expect(screen.getByText(/Help/i)).toBeInTheDocument();
  });
});
