# Technical assessment

## Development environment

### Start a REPL

To start a local nREPL (with CIDER and refactor-nrepl enabled) use the following
commmand in a new terminal:

* Note - see the Environment Variables Documentation section for how to provide the required
Environment Variables.

```bash
clj -M:repl
```

You can then connect to the local REPL with your preferred editor e.g. Emacs,
VSCode, IntelliJ IDEA etc

For CSS assests see the README in the `css` directory.

### Managing the HTTP server from the REPL

A `dev/scratch.clj` file is provided with code that can start/stop
the HTTP server and perform general tasks.

When connected to the REPL, open the file and evaluate the relevant code.

To confirm if the server is up and running, open a http://localhost:3000 in your browser.

### Non REPL HTTP server

To start the HTTP server on the default port (3000),  from a new terminal use the following command:

* Note - see the Environment Variables Documentation section for how to provide the required
Environment Variables.

```
clj -M -m technical-assessment.core
```

### Running tests

To run unit tests, from a new terminal use the following command:

```
clj -X:test
```

### Environment Variables Documentation

The following environment variables are supported:

- **FACEBOOK_AUTH_APP_ID** - The App ID for Facebook authentication

- **FACEBOOK_AUTH_SECRET** - The client secret for Facebook authentication

- **FACEBOOK_AUTH_REDIRECT_URL** The redirect URI for Facebook authentication. Use "http://localhost:3000/auth/facebook/callback" for local development

- **CLOUDINARY_CLOUD_NAME** The Cloudinary cloud name

- **CLOUDINARY_API_KEY** - The API key for Cloudinary

- **CLOUDINARY_API_SECRET** - The API secret for Cloudinary

- **PRODUCTION_ENV** - Indicates if the environment is production

### Example of Starting a REPL with Environment Variables

To start a REPL with the required environment variables, you can use the following command:

```sh
FACEBOOK_AUTH_APP_ID=your_facebook_app_id \
FACEBOOK_AUTH_SECRET=your_facebook_secret \
FACEBOOK_AUTH_REDIRECT_URL="http://localhost:3000/auth/facebook/callback" \
CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name \
CLOUDINARY_API_KEY=your_cloudinary_api_key \
CLOUDINARY_API_SECRET=your_cloudinary_api_secret \
clj -M:repl
```


```sh
FACEBOOK_AUTH_APP_ID=your_facebook_app_id \
FACEBOOK_AUTH_SECRET=your_facebook_secret \
FACEBOOK_AUTH_REDIRECT_URL="http://localhost:3000/auth/facebook/callback" \
CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name \
CLOUDINARY_API_KEY=your_cloudinary_api_key \
CLOUDINARY_API_SECRET=your_cloudinary_api_secret \
clj -M -m technical-assessment.core
```