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
clj -M -m technical-assessment.core {}
```

To start the HTTP server on the specific port (7777), from a new terminal use the following command:

```
clj -M -m technical-assessment.core http-port 7777
```

### Running tests

To run unit tests, from a new terminal use the following command:

```
clj -X:test
```

### Environment Variables Documentation

The `technical-assessment.config` namespace uses the following environment variables:

1. **FACEBOOK_AUTH_APP_ID**
   - **Description**: The App ID for Facebook authentication.
   - **Usage**: Used in `facebook-auth-config` to set the `:app-id`.

2. **FACEBOOK_AUTH_SECRET**
   - **Description**: The client secret for Facebook authentication.
   - **Usage**: Used in `facebook-auth-config` to set the `:client-secret`.

3. **FACEBOOK_AUTH_REDIRECT_URL**
   - **Description**: The redirect URI for Facebook authentication.
   - **Usage**: Used in `facebook-auth-config` to set the `:redirect-uri`.

4. **CLOUDINARY_CLOUD_NAME**
   - **Description**: The Cloudinary cloud name.
   - **Usage**: Used in `default-cloudinary-config` to set the `:cloud-name`.

5. **CLOUDINARY_API_KEY**
   - **Description**: The API key for Cloudinary.
   - **Usage**: Used in `default-cloudinary-config` to set the `:api-key`.

6. **CLOUDINARY_API_SECRET**
   - **Description**: The API secret for Cloudinary.
   - **Usage**: Used in `default-cloudinary-config` to set the `:api-secret`.

7. **PROD**
   - **Description**: Indicates if the environment is production.
   - **Usage**: Used in `production-environment?` to determine if the environment is production.

### Example of Starting a REPL with Environment Variables

To start a REPL with the required environment variables, you can use the following command:

```sh
FACEBOOK_AUTH_APP_ID=your_facebook_app_id \
FACEBOOK_AUTH_SECRET=your_facebook_secret \
FACEBOOK_AUTH_REDIRECT_URL=your_facebook_redirect_url \
CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name \
CLOUDINARY_API_KEY=your_cloudinary_api_key \
CLOUDINARY_API_SECRET=your_cloudinary_api_secret \
clj -M:repl
```
