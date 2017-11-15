# LogParser

The goal is to write a parser in Java that parses web server access log file, loads the log to MySQL and checks if a given IP makes more than a certain number of requests for the given duration.

Java :
(1) Create a java tool that can parse and load the given log file to MySQL. The delimiter of the log file is pipe (|)
(2) The tool takes "startDate", "duration" and "threshold" as command line arguments. "startDate" is of "yyyy-MM-dd.HH:mm:ss" format, "duration" can take only "hourly", "daily" as inputs and "threshold" can be an integer.
(3) This is how the tool works:
    
    java -cp "parser.jar" com.ef.Parser --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
	
	The tool will find any IPs that made more than 100 requests starting from 2017-01-01.13:00:00 to 2017-01-01.14:00:00 (one hour) and print them to console AND also load them to another MySQL table with comments on why it's blocked.

    java -cp "parser.jar" com.ef.Parser --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250

	The tool will find any IPs that made more than 250 requests starting from 2017-01-01.13:00:00 to 2017-01-02.13:00:00 (24 hours) and print them to console AND also load them to another MySQL table with comments on why it's blocked.

Log Format:
Date, IP, Request, Status, User Agent (pipe delimited, open the example file in text editor)
Date Format: "yyyy-MM-dd HH:mm:ss.SSS"

TechStack : Java 8, MySQL

Steps for exucuting the code:

1.Install MySQL in mac using HomeBrew:

Install Homebrew:
    Open Terminal and enter
	$ /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
	Note: Homebrew will download and install Command Line Tools for Xcode 8.0 as part of the installation process.

Install MySQL:
	Enter the following command :
	$ brew info mysql
	-Expected output: mysql: stable 5.7.15 (bottled)
	$ brew install mysql

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


2. Java expects jars to be present in /Library/Java/Extensions path if we are adding them externally.
cp /lib/mysql-connector.jar /Library/Java/Extensions/

3. Execute below instructions in terminal.

java -cp "Parser.jar" com.ef.Parser --accesslog=/Users/Kiran/Documents/Workspaces/LoggingChallenge/LogParserChallenge/Resources/access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100

java -cp "Parser.jar" com.ef.Parser --accesslog=/Users/Kiran/Documents/Workspaces/LoggingChallenge/LogParserChallenge/Resources/access.log --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250
