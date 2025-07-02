import { msalConfig, loginRequest } from '../authConfig';

describe('msalConfig', () => {
  it('should have the correct clientId', () => {
    expect(msalConfig.auth.clientId).toBe('738c4ab9-f5a7-4777-84c1-5c6f3814ace4');
  });

  it('should have the correct authority', () => {
    expect(msalConfig.auth.authority).toBe('https://login.microsoftonline.com/8b33a0fd-354e-48f9-b6fa-b85f0b6e3e55/v2.0');
  });

  it('should set redirectUri to window.location.origin + "/"', () => {
    expect(msalConfig.auth.redirectUri).toBe(window.location.origin + '/');
  });

  it('should use sessionStorage for cache', () => {
    expect(msalConfig.cache.cacheLocation).toBe('sessionStorage');
  });
});

describe('loginRequest', () => {
  it('should have the correct scopes', () => {
    expect(loginRequest.scopes).toContain('738c4ab9-f5a7-4777-84c1-5c6f3814ace4/.default');
  });
});
