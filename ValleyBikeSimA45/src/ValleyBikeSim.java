/**
 * @author maingo
 *
 */
public class ValleyBikeSim {

	/**
	 * Main function. Starts ValleyBike program.
	 * @param args 		unused
	 */
	public static void main(String[] args) {
		
		// create model, view, controller, and tell controller about model and view
		ValleyBikeSimView view = new ValleyBikeSimView();
		ValleyBikeSimModel model = new ValleyBikeSimModel();
		ValleyBikeSimController controller = new ValleyBikeSimController(view, model);
		model.readData();
		
		controller.start(); // display welcome menu
	}

}
