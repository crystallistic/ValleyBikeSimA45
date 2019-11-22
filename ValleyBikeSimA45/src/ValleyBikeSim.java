/**
 * 
 */

/**
 * @author maingo
 *
 */
public class ValleyBikeSim {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// create model, view, controller, and tell control
		ValleyBikeSimView view = new ValleyBikeSimView();
		ValleyBikeSimModel model = new ValleyBikeSimModel();
		ValleyBikeSimController controller = new ValleyBikeSimController(view, model);
		
		controller.start(); // display login menu
	}

}
