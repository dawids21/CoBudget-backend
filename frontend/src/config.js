const {CLIENT_ID, ISSUER} = process.env

const REDIRECT_URI = `${window.location.origin}/login/callback`

export default {
    oidc: {
        clientId: CLIENT_ID,
        issuer: ISSUER,
        redirectUri: REDIRECT_URI,
        scopes: ['openid'],
        pkce: true,
    },
}
