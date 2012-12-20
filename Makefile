runjs: RunJS.java lib/*.java lib/objects/*.java node_natives/*
	javac -cp deps/rhino/js.jar -Xlint:unchecked -d classes *.java lib/*.java lib/objects/*.java
	@cd classes && jar xvf ../deps/rhino/js.jar > /dev/null
	@cd classes && echo "Main-Class: RunJS" > Manifest.txt
	@cd classes && cp -r ../node_natives .
	@cp node.js classes
	@cd classes && jar cfm ../node.jar Manifest.txt .

runtest: runjs bb.js
	java -jar node.jar bb.js

runnode: runjs node.js
	java -jar node.jar t.js

runt: runjs node.js
	java -jar node.jar test-buffer.js

clean: 
	/bin/rm -rf classes/* node.jar

test: runjs
	$(PYTHON) tools/test.py --mode=release simple message
