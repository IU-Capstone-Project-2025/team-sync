
const { TextEncoder, TextDecoder } = require('util');
if (typeof global.TextEncoder === 'undefined') {
  global.TextEncoder = TextEncoder;
}
if (typeof global.TextDecoder === 'undefined') {
  global.TextDecoder = TextDecoder;
}

// Polyfill for global crypto (minimal, for MSAL)
if (typeof global.crypto === 'undefined') {
  global.crypto = {
    getRandomValues: (arr) => require('crypto').randomFillSync(arr),
    subtle: {}, // Add more mocks if needed
  };
}

// Mock @azure/msal-react
jest.mock('@azure/msal-react', () => ({
  useMsal: () => ({
    instance: {},
    accounts: [],
  }),
  MsalProvider: ({ children }) => children,
}));

// Mock @azure/msal-browser
jest.mock('@azure/msal-browser', () => ({
  PublicClientApplication: function () {
    return {
      loginPopup: jest.fn(),
      loginRedirect: jest.fn(),
      acquireTokenSilent: jest.fn(),
      acquireTokenPopup: jest.fn(),
      acquireTokenRedirect: jest.fn(),
      logout: jest.fn(),
    };
  },
}));
