echo setting up codepage 65001 for UTF-8
chcp 65001
java -Dfile.encoding=UTF-8 -cp target\test-classes;target\lib\junit-4.12.jar;target\Game2048-0.0.1-SNAPSHOT.jar;target/lib/hamcrest-core-1.3.jar;target/classes;target/lib/mockito-core-2.8.47.jar;target/lib/jansi-1.16.jar;target/lib/byte-buddy-1.6.14.jar;target/lib/objenesis-2.5.jar;target/lib/jna-4.4.0.jar      org.junit.runner.JUnitCore bence.game2048.Game2048Test 
