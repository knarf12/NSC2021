
public class MyMain {

	public static void main(String[] args) {
		MyGUI app = new MyGUI();
		app.setExtendedState(app.MAXIMIZED_BOTH);
		app.setLocationRelativeTo(null);
		app.setUndecorated(true);
		app.setDefaultCloseOperation(app.EXIT_ON_CLOSE);
		app.setVisible(true);
		
//		app.setIconImage(Toolkit.getDefaultToolkit().getImage(app.getClass().getResource("icon.png")));
//		app.setExtendedState(app.MAXIMIZED_BOTH);
//		app.setLocationRelativeTo(null);
//		app.setUndecorated(true);
//		app.setDefaultCloseOperation(app.EXIT_ON_CLOSE);
//		app.setSize(1024, 768);
		
//		app.setLocationRelativeTo(null);
//		app.setVisible(true);
	}

}
