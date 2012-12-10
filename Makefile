runjs: RunJS.java lib/*.java lib/objects/*.java node_natives/*
	javac -cp deps/rhino/js.jar -Xlint:unchecked -d classes *.java lib/*.java lib/objects/*.java
	@cd classes && jar xvf ../deps/rhino/js.jar > /dev/null
	@cd classes && echo "Main-Class: RunJS" > Manifest.txt
	@cd classes && cp -r ../node_natives ../lib .
	@cd classes && jar cfm ../node.jar Manifest.txt .

runtest: runjs test.js
	java -jar node.jar test.js

runnode: runjs node.js
	java -jar node.jar node.js

clean: 
	/bin/rm -rf classes/* node.jar
