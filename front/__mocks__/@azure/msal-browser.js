class PublicClientApplication {
  constructor() {
    this.acquireTokenSilent = jest.fn();
    this.acquireTokenPopup = jest.fn();
    this.loginPopup = jest.fn();
    this.logout = jest.fn();
  }
}

module.exports = {
  PublicClientApplication,
};
