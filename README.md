# git-app

> Application listing all github repositories for given user

## Documentation

> Link to documentation on swagger: http://localhost:8080/swagger-ui.html <br/><br>
> List user's gihbub repositories that are not forked with branchers  <br/><br/>
`GET http://localhost:8080/api/users/lukaszz86/repositories
Content-Type: application/json` <br/><br/>
> This API return 404 when  user has no repositories in github <br/><br/>
`GET http://localhost:8080/api/users/lukaszz860099/repositories
Content-Type: application/json`<br/><br/>
> This API return 406 when content-type other that application/json is used <br/><br/>
`GET http://localhost:8080/api/users/lukaszz86/repositories
Content-Type: application/xml`<br/><br/>
> *Note: in the main application root folder there is a **gitapp.http** file which allows to run the example requests as well*
## Error handling


> | Status code | Description |
> | --- | --- | 
> | 200 | SUCCESS - Successfully retrieved repositories for user | 
> | 400 | BAD_REQUEST - Incorrect input parameters, headers or path | 
> | 404 | USER_NOT_FOUND - User does not exist in Github repository | 
> | 406 | NOT_ACCEPTABLE - Incorrect media type used in request | 
> | 500 | INTERNAL_SERVER_ERROR - user does not exist in Github repository | 
> | 500 | INTERNAL_SERVER_ERROR - user does not exist in Github repository | 

## Docker

> In order to run application you need the following commands
> <br/><br/>
1) Building an image <br/><br/>
> `docker build . -t git-app` <br/><br/>
2) Publishing and running application <br/><br/>
> `docker run --publish 8080:8080 git-app` <br/>

 