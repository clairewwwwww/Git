//import java.io.IOException;

public class Tester {
    public static void main(String args[]) throws Exception {
        Index tester = new Index();
        tester.addBlob("testCase1");
        tester.addBlob("testCase2");
        tester.removeBlob("testCase2");
        tester.addBlob("testCase3");
        tester.removeBlob("testCase1");
        tester.addBlob("testCase1");
        tester.addBlob("testCase2");
        tester.removeBlob("testCase1");
        tester.removeBlob("testCase2");
        tester.removeBlob("testCase3");
        tester.addBlob("testCase2");
    }

}
