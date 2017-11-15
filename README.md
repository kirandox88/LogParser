# LogParser

1.Install MySQL in mac using HomeBrew:

Install Homebrew:
Open Terminal and enter

	$ /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
	Note: Homebrew will download and install Command Line Tools for Xcode 8.0 as part of the installation process.

Install MySQL:
	Enter the following command :
	$ brew info mysql
	-Expected output: mysql: stable 5.7.15 (bottled)
	To install MySQL enter : $ brew install mysql

Additional configuration
Homebrew:
		Install brew services first : $ brew tap homebrew/services

	Load and start the MySQL service : $ brew services start mysql.
		- Expected output : Successfully started mysql (label: homebrew.mxcl.mysql)

	Check of the MySQL service has been loaded : $ brew services list 1
	Verify the installed MySQL instance : $ mysql -V.
		- Expected output : Ver 14.14 Distrib 5.7.15, for osx10.12 (x86_64)

MySQL:
	Open Terminal and execute the following command to set the root password:
	mysqladmin -u root password 'yourpassword'


2.Place /lib/mysql-connector.jar in /Library/Java/Extensions path.
cp /lib/mysql-connector.jar /Library/Java/Extensions/

3.java -cp "Parser.jar" com.ef.Parser --accesslog=/Users/Kiran/Documents/Workspaces/LoggingChallenge/LogParserChallenge/Resources/access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
