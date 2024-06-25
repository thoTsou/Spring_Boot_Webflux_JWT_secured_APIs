# WebFlux+Kotlin+coroutines JWT secured APIs

- This project could be taken under consideration when setting up reactive(webflux) API stateless security based on JWTs.

- It's purpose is to act as a starting point and/or template to build on.

- This authentication flow follows the Two-Legged OAuth Flow which is
  essential for server to server communication. As stated by IBM:

_**"Two-legged OAuth processing involves three parties: OAuth client, authorization server,
and resource server. The OAuth client can be either the resource owner or the trusted entity that knows
about the credentials of the resource owner. In other words,
two-legged OAuth processing does not involve additional resource owner interaction."**_

- If this auth flow is combined with an OTP validation process, it could be pretty secured for server to client communication too... 

More information about OAuth flows --> [IBM docs](https://www.ibm.com/docs/en/datapower-gateway/10.0.1?topic=support-oauth-flows)

## More specific about this APIs Auth flow:

- Client logs in with username and password and receive access and refresh tokens.

- Client uses access token for making multiple requests.
  When access token expires, client uses refresh token to retrieve a new pair of tokens.
  Client uses the new access token to make API request.

With proper changes/add-ons this could go on production. So you're welcome :)
