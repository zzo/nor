runjs: RunJS.java lib/*
	javac -cp deps/rhino/js.jar -Xlint:unchecked  -d classes *.java lib/*.java

runtest: runjs test.js
	java -cp deps/rhino/js.jar:. RunJS test.js

runnode: runjs node.js
	java -cp deps/rhino/js.jar:. RunJS node.js
