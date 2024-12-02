# Technical assessment

## Objective

> Create a Clojure-based web app that allows users to sign up via Facebook, retrieves user details (first name, last name, email, and profile picture), and saves this data to a local database. The profile picture should be retrieved from Facebook and uploaded to Cloudinary, and the Cloudinary URL should be stored in the local database.


## Requirements and Key Features

- User Signup with Facebook
  - Implement Facebook Login using the Facebook API
  - Retrieve user information such as:
    - First Name
    - Last Name
    - Email Address
    - Profile Picture URL
  - This will require setting up a Facebook Developer Account and registering an application in development mode to obtain credentials (App ID, Secret)
- Data Storage
  - Save the retrieved user details in a database
  - The database can be any preferred option
- Profile Picture processing
  - Retrieve the user’s profile picture from Facebook using the URL provided via the Facebook API
  - Upload the profile picture to Cloudinary
  - Save the Cloudinary URL of the uploaded image to the user’s record in the database
  - This will require setting up a Cloudinary account to create an API key and secret
- Consider error handling and testing where appropriate



## Development environment

### Start a REPL

To start a local nREPL (with CIDER and refactor-nrepl enabled) use the following
commmand in a new terminal:

* Note - see the [Environment Variables Documentation](https://github.com/TenDaysOfClojure/technical-assessment?tab=readme-ov-file#environment-variables-documentation) section for required
environment variables for integration configuration.

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

* Note - see the [Environment Variables Documentation](https://github.com/TenDaysOfClojure/technical-assessment?tab=readme-ov-file#environment-variables-documentation) section for required
environment variables for integration configuration.

```
clj -M -m technical-assessment.core
```

### Running tests

To run unit tests, from a new terminal use the following command:

```
clj -A:test
```

### Environment Variables Documentation

The following environment variables are supported:

- **FACEBOOK_AUTH_APP_ID** - The App ID for Facebook authentication

- **FACEBOOK_AUTH_SECRET** - The client secret for Facebook authentication

- **FACEBOOK_AUTH_REDIRECT_URL** The redirect URI for Facebook authentication. Use "http://localhost:3000/auth/facebook/callback" for local development

- **CLOUDINARY_CLOUD_NAME** The Cloudinary cloud name

- **CLOUDINARY_API_KEY** - The API key for Cloudinary

- **CLOUDINARY_API_SECRET** - The API secret for Cloudinary

- **HTTP_PORT** - Indicates if port that the HTTP server should run on, defaults to 3000

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
