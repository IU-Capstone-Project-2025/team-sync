module.exports = {
  useMsal: () => ({
    instance: {},
    accounts: [],
    inProgress: 'none',
  }),
  MsalProvider: ({ children }) => children,
  AuthenticatedTemplate: ({ children }) => children,
  UnauthenticatedTemplate: ({ children }) => children,
};
