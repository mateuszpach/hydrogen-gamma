class Program {
    public static void main(String[] args) {
        Parser parser = new Parser();
        parser.load("a:1.5;b:-3.5;c:4;abba:-3;d:[1,2,3|2,3,4];e:(-2.3,44455,333.35,-6);text:\"text\"", "sin(+(a,*(b,c)))");

    }
}
