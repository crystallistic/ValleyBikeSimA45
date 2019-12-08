/**
 * @author maingo, Jemimah Charles, Maggie Burkart, Emma Tanur
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
		// load data in model
		model.readData();
		controller.start(); // display welcome menu
	}

}
