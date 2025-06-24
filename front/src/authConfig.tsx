export const msalConfig = {
  auth: {
    clientId: "738c4ab9-f5a7-4777-84c1-5c6f3814ace4",
    authority: "https://login.microsoftonline.com/8b33a0fd-354e-48f9-b6fa-b85f0b6e3e55/v2.0",
    redirectUri: "http://localhost:80/",
  },
  cache: {
    cacheLocation: "sessionStorage",
    storeAuthStateInCookie: false,
  },
  system: {
    pollIntervalMilliseconds: 0,
  }
};


export const loginRequest = {
   scopes:["738c4ab9-f5a7-4777-84c1-5c6f3814ace4/.default"]
};                                                                                     