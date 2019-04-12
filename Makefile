

reversi: reversi.java
	javac reversi.java

test:
	java reversi < sample_input.txt

clean:
	rm -rf *.class
