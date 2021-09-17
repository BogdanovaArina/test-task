# How to run the tests
1. Run the application for testing.

2. Clone this repository to your local machine using the command below: 
   <br>`git clone https://github.com/BogdanovaArina/test-task.git`
3. Run the tests using command
`./gradlew clean test allureReport allureServe -DadminUserLogin=<admin_user_login> -DadminUserPassword=<admin_user_password> -DapplicationBaseUrl=<application_url> allureReport allureServe`
<br> Where

|argument       | description           |
   | ------------- |:-------------:| 
| _admin_user_login_      | login of with admin rights | 
| _admin_user_password_     | password of user with admin rights     |  
|  _application_url_ |url of application     |

For example: 
`./gradlew clean test allureReport allureServe -DadminUserLogin=admin -DadminUserPassword=111 -DapplicationBaseUrl=http://localhost:8080`
   
Before running tests make sure that there is an admin user available on the test environment. 
<br>By default, the credentials of the admin user are: _login="admin", password="111"_ and _application url is http://localhost:8080_ 
<br>

Execute the command to run all tests in the project for the first time with default parameters

`./gradlew clean test allureReport allureServe`

In the following runs to run tests with default parameters and generate report can be used this command:

`./gradlew clean test allureServe`
