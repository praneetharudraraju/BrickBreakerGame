public class Main {
    public static void main(String[] args) {
        javax.swing.JFrame obj = new javax.swing.JFrame();
        BreakoutGame gamePlay = new BreakoutGame();
        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Breakout Ball");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
    }
}
