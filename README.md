# Override Testray Results
## Required Software and Prerequisites
1. Clone this repository
2. [ChromeDriver](https://chromedriver.chromium.org/)
3. [Perimeter 81 VPN](https://liferay.perimeter81.com/)

## Setup Your Environment
1. Download Chrome WebDriver which is match your Chrome Version.
2. Extract chromeDriver to your machine and copy the path of the driver.
3. Make sure you have stable network that able to login testray and navigate.I tested with Perimeter 81 connected which works well.
4. Go to testrayHelperGradle root directory.
5. Edit src/test/resources/testray.properties
   1. Update the properties to your own value

## Run application
1. Open Terminal and go to testrayHelperGradle root directory.
2. Run the following command.
   1. `gradle clean test`
3. :coffee: Move your hand away from the keyboard and drink a cup of coffee. 
4. The application will run minutes until all the test are overriding.