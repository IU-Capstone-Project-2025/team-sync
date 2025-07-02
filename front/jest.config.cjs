module.exports = {
  transform: {
    '^.+\\.(ts|tsx)$': 'babel-jest',
  },
  testEnvironment: 'jsdom',
  moduleFileExtensions: ['js', 'jsx', 'ts', 'tsx'],
  setupFilesAfterEnv: ['@testing-library/jest-dom'],
  setupFiles: ['./jest.setup.js'],
  moduleNameMapper: {
    '^@azure/msal-browser$': '<rootDir>/__mocks__/@azure/msal-browser.js',
    '^@azure/msal-react$': '<rootDir>/__mocks__/@azure/msal-react.js',
  },
};